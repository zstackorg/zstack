package org.zstack.core.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zstack.core.Platform;
import org.zstack.core.cloudbus.CloudBus;
import org.zstack.core.cloudbus.MessageSafe;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.core.db.GLock;
import org.zstack.core.db.SQL;
import org.zstack.core.errorcode.ErrorFacade;
import org.zstack.core.asyncbatch.While;
import org.zstack.header.AbstractService;
import org.zstack.header.core.NoErrorCompletion;
import org.zstack.header.errorcode.ErrorCode;
import org.zstack.header.exception.CloudRuntimeException;
import org.zstack.header.message.Message;
import org.zstack.utils.BeanUtils;
import org.zstack.utils.DebugUtils;
import org.zstack.utils.TypeUtils;
import org.zstack.utils.Utils;
import org.zstack.utils.data.StringTemplate;
import org.zstack.utils.logging.CLogger;
import org.zstack.utils.path.PathUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.zstack.core.Platform.argerr;

public class TemplateConfigFacadeImpl extends AbstractService implements TemplateConfigFacade {
    private static final CLogger logger = Utils.getLogger(GlobalConfigFacadeImpl.class);

    @Autowired
    private CloudBus bus;
    @Autowired
    private DatabaseFacade dbf;
    @Autowired
    private ErrorFacade errf;
    @Autowired
    GlobalConfigFacade gcf;



    private JAXBContext context;
    private Map<String, TemplateConfig> allConfig = new ConcurrentHashMap<>();
    private static final String TEMPLATE_FOLDER = "template";
    private static final String TEMPLATE_CONFIG_FOLDER = "templateConfig";
    private static final String OTHER_CATEGORY = "Others";

    @Override
    @MessageSafe
    public void handleMessage(Message msg) {
        if (msg instanceof APIUpdateTemplateConfigMsg) {
            handle((APIUpdateTemplateConfigMsg) msg);
        }else if (msg instanceof APIApplyTemplateConfigMsg){
            handle((APIApplyTemplateConfigMsg) msg);
        }else if (msg instanceof APIResetTemplateConfigMsg) {
            handle((APIResetTemplateConfigMsg) msg);
        } else {
            bus.dealWithUnknownMessage(msg);
        }
    }

    private void handle(APIResetTemplateConfigMsg msg) {
        APIResetTemplateConfigEvent evt = new APIResetTemplateConfigEvent(msg.getId());
        Map<String, TemplateConfig> configs = getConfigsByTemplateUuid(msg.getTemplateUuid());
        for(TemplateConfig templateConfig: configs.values()) {
            templateConfig.updateValue(templateConfig.getDefaultValue());
        }

        logger.info("Reset the template configurations.");
        bus.publish(evt);
    }

    private void handle(APIUpdateTemplateConfigMsg msg) {
        APIUpdateTemplateConfigEvent evt = new APIUpdateTemplateConfigEvent(msg.getId());
        TemplateConfig templateConfig = allConfig.get(msg.getIdentity());
        if (templateConfig == null) {
            ErrorCode err = argerr("Unable to find TemplateConfig[category: %s, name: %s, templateUuid: %s]", msg.getCategory(), msg.getName(), msg.getTemplateUuid());
            evt.setError(err);
            bus.publish(evt);
            return;
        }

        try {
            templateConfig.updateValue(msg.getValue());
            TemplateConfigInventory inv = TemplateConfigInventory.valueOf(templateConfig.reload());
            evt.setInventory(inv);
        } catch (TemplateConfigException e) {
            evt.setError(argerr(e.getMessage()));
            logger.warn(e.getMessage(), e);
        }
        bus.publish(evt);
    }

    private  void handle(APIApplyTemplateConfigMsg msg) {
        APIApplyTemplateConfigEvent evt = new APIApplyTemplateConfigEvent(msg.getId());
        List<String> errorList = new ArrayList<>();
        Map<String, TemplateConfig> configs = getConfigsByTemplateUuid(msg.getTemplateUuid());

        new While<>(configs.values()).all((c, compl) -> {
            try {
                String gcKey = GlobalConfig.produceIdentity(c.getCategory(), c.getName());
                GlobalConfig globalConfig = gcf.getAllConfig().get(gcKey);
                globalConfig.updateValue(c.getValue());
                GlobalConfigInventory.valueOf(globalConfig.reload());

            } catch (GlobalConfigException e) {
                errorList.add(e.getMessage());
                logger.warn(e.getMessage(), e);
            }
        }).run(new NoErrorCompletion() {
            @Override
            public void done() {
                if (errorList.size() > 0) {
                    evt.setError(argerr(StringUtils.join(errorList, "\n")));
                } else {
                    evt.setSuccess(true);
                }
            }
        });
        bus.publish(evt);
    }

    private Map<String, TemplateConfig> getConfigsByTemplateUuid(String templateUuid) {
        Map<String, TemplateConfig> configs = new HashMap<>();
        for (Map.Entry<String, TemplateConfig> entry : allConfig.entrySet()) {
            if (templateUuid.equals(entry.getValue().getTemplateUuid())) {
                configs.put(entry.getKey(), entry.getValue());
            }
        }
        return configs;
    }

    @Override
    public String getId() {
        return bus.makeLocalServiceId(TemplateConfigConstant.SERVICE_ID);
    }

    @Override
    public boolean start() {
        class TemplateConfigInitializer {
            Map<String, GlobalConfigTemplate> templatesFromXml = new HashMap<String, GlobalConfigTemplate>();
            Map<String, GlobalConfigTemplate> templatesFromDatabase = new HashMap<String, GlobalConfigTemplate>();
            Map<String, TemplateConfig> configsFromXml = new HashMap<String, TemplateConfig>();
            Map<String, TemplateConfig> configsFromDatabase = new HashMap<String, TemplateConfig>();
            Map<String, String> templateNameUuid = new HashMap<String, String>();
            List<Field> TemplateConfigFields = new ArrayList<Field>();
            Map<String, String> propertiesMap = new HashMap<>();

            void init() {
                GLock lock = new GLock(TemplateConfigConstant.LOCK, 320);
                lock.lock();
                try {
                    parseTemplateConfigFields();
                    loadTemplateFromXml();
                    loadTemplateFromDatabase();
                    persistTemplateInXmlButNotInDatabase();

                    loadTemplateConfigFromXml();
                    loadTemplateConfigFromDatabase();
                    createValidatorForBothXmlAndDatabase();
                    validateConfigFromXml();
                    validateConfigFromDatabase();
                    persistConfigInXmlButNotInDatabase();
                    mergeXmlDatabase();
                    link();
                    allConfig.putAll(configsFromXml);
                    // re-validate after merging xml's with db's
                    validateAll();
                } catch (IllegalArgumentException ie) {
                    throw ie;
                } catch (Exception e) {
                    throw new CloudRuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }

            private void parseTemplateConfigFields() {
                Set<Class<?>> definitionClasses = BeanUtils.reflections.getTypesAnnotatedWith(TemplateConfigDefinition.class);
                for (Class def : definitionClasses) {
                    for (Field field : def.getDeclaredFields()) {
                        if (Modifier.isStatic(field.getModifiers()) && TemplateConfig.class.isAssignableFrom(field.getType())) {
                            field.setAccessible(true);

                            try {
                                TemplateConfig config = (TemplateConfig) field.get(null);
                                if (config == null) {
                                    throw new CloudRuntimeException(String.format("TemplateConfigDefinition[%s] defines a null TemplateConfig[%s]." +
                                                    "You must assign a value to it using new TemplateConfig(category, name)",
                                            def.getName(), field.getName()));
                                }

                                TemplateConfigFields.add(field);
                            } catch (IllegalAccessException e) {
                                throw new CloudRuntimeException(e);
                            }
                        }
                    }
                }
            }


            private void mergeXmlDatabase() {
                for (GlobalConfigTemplate t : templatesFromDatabase.values()) {
                    GlobalConfigTemplate x = templatesFromXml.get(t.getIdentity());
                    if (x == null) {
                        templatesFromXml.put(t.getIdentity(), t);
                    }
                }

                for (TemplateConfig t : configsFromDatabase.values()) {
                    TemplateConfig x = configsFromXml.get(t.getIdentity());
                    if (x == null) {
                        configsFromXml.put(t.getIdentity(), t);
                    } else {
                        x.setValue(t.getValue());
                        x.setDefaultValue(t.getDefaultValue());
                    }
                }
            }

            private void validateAll() {
                for (TemplateConfig t : allConfig.values()) {
                    t.normalize();

                    try {
                        t.validate();
                    } catch (Exception e) {
                        throw new IllegalArgumentException(String.format("exception happened when validating global config:\n%s", t.toString()), e);
                    }
                }
            }

            private void validateConfigFromDatabase() {
                logger.debug("validating global config loaded from database");
                for (TemplateConfig t : configsFromDatabase.values()) {
                    t.validate();
                }
            }

            private void validateConfigFromXml() {
                logger.debug("validating global config loaded from XML files");
                for (TemplateConfig t : configsFromXml.values()) {
                    t.validate();
                }
            }

            private void persistConfigInXmlButNotInDatabase() {
                List<TemplateConfigVO> toSave = new ArrayList<TemplateConfigVO>();  // new config options
                List<TemplateConfig> toRemove = new ArrayList<>(); // obsolete config options
                List<TemplateConfig> toUpdate = new ArrayList<>(); // configs with changed default value
                List<TemplateConfig> toUpdate2 = new ArrayList<>(); // configs with changed default value
                List<TemplateConfig> toUpdate3 = new ArrayList<>(); // configs' value is not match type (normally the values were from an old zstack version)

                for (TemplateConfig config : configsFromXml.values()) {
                    TemplateConfig dbcfg = configsFromDatabase.get(config.getIdentity());
                    if (dbcfg != null) {
                        if (!dbcfg.getDefaultValue().equals(config.getDefaultValue())) {
                            logger.debug(String.format("Will update a template config to database: %s", config.toString()));

                            if (dbcfg.getDefaultValue().equals(dbcfg.getValue())) {
                                toUpdate.add(config);
                            } else {
                                toUpdate2.add(config);
                            }
                        }

                        continue;
                    }

                    logger.debug(String.format("Add a new template config to database: %s", config.toString()));
                    toSave.add(config.toVO());
                }

                for (TemplateConfig config : configsFromDatabase.values()) {
                    if (!configsFromXml.containsKey(config.getIdentity())) {
                        toRemove.add(config);
                    } else {
                        config.setType(configsFromXml.get(config.getIdentity()).getType());
                        String oldValue = config.getValue();
                        config.normalize();
                        if (!oldValue.equals(config.getValue())) {
                            logger.warn(String.format("[%s] found value: [%s] not matched the type: [%s], update to: [%s]",
                                    config.getIdentity(), oldValue, config.getType(), config.getValue()));
                            toUpdate3.add(config);
                        }
                    }
                }

                if (!toSave.isEmpty()) {
                    dbf.persistCollection(toSave);
                }

                for (TemplateConfig config : toRemove) {
                    logger.debug(String.format("Will remove an old template config from database: %s", config.toString()));
                    SQL.New(TemplateConfigVO.class)
                            .eq(TemplateConfigVO_.category, config.getCategory())
                            .eq(TemplateConfigVO_.name, config.getName())
                            .delete();
                }

                for (TemplateConfig config : toUpdate) {
                    SQL.New(TemplateConfigVO.class)
                            .eq(TemplateConfigVO_.category, config.getCategory())
                            .eq(TemplateConfigVO_.name, config.getName())
                            .set(TemplateConfigVO_.defaultValue, config.getDefaultValue())
                            .set(TemplateConfigVO_.value, config.getValue())
                            .update();
                }

                for (TemplateConfig config : toUpdate2) {
                    SQL.New(TemplateConfigVO.class)
                            .eq(TemplateConfigVO_.category, config.getCategory())
                            .eq(TemplateConfigVO_.name, config.getName())
                            .set(TemplateConfigVO_.defaultValue, config.getDefaultValue())
                            .update();
                }

                for (TemplateConfig config : toUpdate3) {
                    SQL.New(TemplateConfigVO.class)
                            .eq(TemplateConfigVO_.category, config.getCategory())
                            .eq(TemplateConfigVO_.name, config.getName())
                            .set(TemplateConfigVO_.value, config.getValue())
                            .update();
                }
            }

            private void persistTemplateInXmlButNotInDatabase() {
                List<GlobalConfigTemplateVO> toSave = new ArrayList<GlobalConfigTemplateVO>();  // new template options
                List<GlobalConfigTemplate> toRemove = new ArrayList<>(); // obsolete config options
                List<GlobalConfigTemplate> toUpdate = new ArrayList<>(); // configs with changed default value

                for (GlobalConfigTemplate template : templatesFromXml.values()) {
                    GlobalConfigTemplate dbgct = templatesFromDatabase.get(template.getIdentity());
                    if (dbgct != null) {
                        if (!dbgct.getName().equals(template.getName())) {
                            logger.debug(String.format("Will update a template config to database: %s", template.toString()));

                        }

                        continue;
                    }

                    logger.debug(String.format("Add a new template config to database: %s", template.toString()));
                    toSave.add(template.toVO());
                }

                if (!toSave.isEmpty()) {
                    dbf.persistCollection(toSave);
                }

                for (GlobalConfigTemplate template : toRemove) {
                    logger.debug(String.format("Will remove an old template from database: %s", template.toString()));
                    SQL.New(GlobalConfigTemplateVO.class)
                            .eq(GlobalConfigTemplateVO_.uuid, template.getUuid())
                            .delete();
                }

                for (GlobalConfigTemplate template : toUpdate) {
                    SQL.New(GlobalConfigTemplateVO.class)
                            .eq(GlobalConfigTemplateVO_.uuid, template.getUuid())
                            .set(GlobalConfigTemplateVO_.name, template.getName())
                            .set(GlobalConfigTemplateVO_.type, template.getType())
                            .update();
                }

            }

            private void loadTemplateFromDatabase() {
                List<GlobalConfigTemplateVO> vos = dbf.listAll(GlobalConfigTemplateVO.class);
                for (GlobalConfigTemplateVO vo : vos) {
                    GlobalConfigTemplate t = GlobalConfigTemplate.valueOf(vo);
                    templatesFromDatabase.put(t.getIdentity(), t);
                }
            }

            private void loadTemplateConfigFromDatabase() {
                List<TemplateConfigVO> vos = dbf.listAll(TemplateConfigVO.class);
                for (TemplateConfigVO vo : vos) {
                    TemplateConfig t = TemplateConfig.valueOf(vo);
                    configsFromDatabase.put(t.getIdentity(), t);
                }
            }

            private void loadTemplateFromXml() throws JAXBException {
                context = JAXBContext.newInstance("org.zstack.core.config.schema");
                List<String> filePaths = PathUtil.scanFolderOnClassPath(TEMPLATE_FOLDER);
                for (String path : filePaths) {
                    File f = new File(path);
                    parseTemplate(f);
                }
            }
            private void loadTemplateConfigFromXml() throws JAXBException {
                context = JAXBContext.newInstance("org.zstack.core.config.schema");
                List<String> filePaths = PathUtil.scanFolderOnClassPath(TEMPLATE_CONFIG_FOLDER);
                for (String path : filePaths) {
                    File f = new File(path);
                    parseConfig(f);
                }
            }

            private void createValidator(final TemplateConfig tc) throws ClassNotFoundException {
                tc.installValidateExtension(new TemplateConfigValidatorExtensionPoint() {
                    Class<?> typeClass;
                    Method typeClassValueOfMethod;
                    String regularExpression;
                    {
                        if (tc.getType() != null) {
                            typeClass = Class.forName(tc.getType());

                            try {
                                typeClassValueOfMethod = typeClass.getMethod("valueOf", String.class);
                            } catch (Exception e) {
                                String err = String.format("TemplateConfig[category:%s, name:%s, templateUuid:%s, specifies type[%s] which doesn't have a static valueOf() method, ignore this type",
                                        tc.getCategory(), tc.getName(), tc.getTemplateUuid(), tc.getType());
                                logger.warn(err);
                            }
                        }

                        regularExpression = tc.getValidatorRegularExpression();
                    }

                    @Override
                    public void validateTemplateConfig(String category, String name, String templateUuid, String oldValue, String newValue) throws TemplateConfigException {
                        if (typeClassValueOfMethod != null) {
                            try {
                                typeClassValueOfMethod.invoke(typeClass, newValue);
                            } catch (Exception e) {
                                String err = String.format("TemplateConfig[category:%s, name:%s] is of type %s, the value[%s] cannot be converted to that type, %s",
                                        tc.getCategory(), tc.getName(), typeClass.getName(), newValue, e.getMessage());
                                throw new TemplateConfigException(err, e);
                            }

                            try {
                                typeClassValueOfMethod.invoke(typeClass, tc.getDefaultValue());
                            } catch (Exception e) {
                                String err = String.format("TemplateConfig[category:%s, name:%s] is of type %s, the default value[%s] cannot be converted to that type, %s",
                                        tc.getCategory(), tc.getName(), typeClass.getName(), tc.getDefaultValue(), e.getMessage());
                                throw new TemplateConfigException(err, e);
                            }
                        }

                        if (typeClass != null && (Boolean.class).isAssignableFrom(typeClass)) {
                            if (newValue == null ||
                                    (!newValue.equalsIgnoreCase("true") &&
                                            !newValue.equalsIgnoreCase("false"))
                            ) {
                                String err = String.format("TemplateConfig[category:%s, name:%s]'s value[%s] is not a valid boolean string[true, false].",
                                        tc.getCategory(), tc.getName(), newValue);
                                throw new TemplateConfigException(err);
                            }

                            if (tc.getDefaultValue() == null ||
                                    (!tc.getDefaultValue().equalsIgnoreCase("true") &&
                                            !tc.getDefaultValue().equalsIgnoreCase("false"))
                            ) {
                                String err = String.format("TemplateConfig[category:%s, name:%s]'s default value[%s] is not a valid boolean string[true, false].",
                                        tc.getCategory(), tc.getName(), tc.getDefaultValue());
                                throw new TemplateConfigException(err);

                            }
                        }

                        if (regularExpression != null) {
                            Pattern p = Pattern.compile(regularExpression);
                            if (newValue != null) {
                                Matcher mt = p.matcher(newValue);
                                if (!mt.matches()) {
                                    String err = String.format("TemplateConfig[category:%s, name:%s]'s value[%s] doesn't match validatorRegularExpression[%s]",
                                            tc.getCategory(), tc.getName(), newValue, regularExpression);
                                    throw new TemplateConfigException(err);
                                }
                            }
                            if (tc.getDefaultValue() != null) {
                                Matcher mt = p.matcher(tc.getDefaultValue());
                                if (!mt.matches()) {
                                    String err = String.format("TemplateConfig[category:%s, name:%s]'s default value[%s] doesn't match validatorRegularExpression[%s]",
                                            tc.getCategory(), tc.getName(), tc.getDefaultValue(), regularExpression);
                                    throw new TemplateConfigException(err);
                                }
                            }
                        }
                    }
                });


            }

            private void createValidatorForBothXmlAndDatabase() throws ClassNotFoundException {
                for (TemplateConfig g : configsFromXml.values()) {
                    createValidator(g);
                }
                for (TemplateConfig g : configsFromDatabase.values()) {
                    createValidator(g);
                }
            }

            private void parseTemplate(File file) throws JAXBException {
                if (!file.getName().endsWith("xml")) {
                    logger.warn(String.format("file[%s] in template folder is not end with .xml, skip it", file.getAbsolutePath()));
                    return;
                }

                Unmarshaller unmarshaller = context.createUnmarshaller();
                org.zstack.core.config.schema.GlobalConfigTemplate t = (org.zstack.core.config.schema.GlobalConfigTemplate) unmarshaller.unmarshal(file);
                for (org.zstack.core.config.schema.GlobalConfigTemplate.Config c : t.getConfig()) {
                    String name = c.getName();
                    name = name == null ? OTHER_CATEGORY : name;
                    c.setName(name);
                    if (c.getType() == null) {
                        throw new IllegalArgumentException(String.format("GlobalConfigTemplate[name:%s] must have a default type", c.getName()));
                    }
                    String uuid = Platform.getUuid();
                    c.setUuid(uuid);
                    c.setDescription(c.getDescription());
                    templateNameUuid.put(name, uuid);
                    GlobalConfigTemplate template = GlobalConfigTemplate.valueOf(c);
                    if (templatesFromXml.containsKey(template.getIdentity())) {
                        throw new IllegalArgumentException(String.format("duplicate GlobalConfigTemplate[type: %s, name: %s]", template.getType(), template.getName()));
                    }
                    templatesFromXml.put(template.getIdentity(), template);
                }
            }

            private void parseConfig(File file) throws JAXBException {
                if (!file.getName().endsWith("xml")) {
                    logger.warn(String.format("file[%s] in global config folder is not end with .xml, skip it", file.getAbsolutePath()));
                    return;
                }

                Unmarshaller unmarshaller = context.createUnmarshaller();
                org.zstack.core.config.schema.TemplateConfig gb = (org.zstack.core.config.schema.TemplateConfig) unmarshaller.unmarshal(file);
                for (org.zstack.core.config.schema.TemplateConfig.Config c : gb.getConfig()) {
                    String category = c.getCategory();
                    String name = c.getName();
                    String gcKey = GlobalConfig.produceIdentity(category, name);
                    if (gcf.getAllConfig().get(gcKey) == null){
                        continue;
                    }
                    c.setCategory(category);
                    String templateUuid = templateNameUuid.get(c.getTemplateName());
                    c.setTemplateUuid(templateUuid);
                    // substitute system properties in value and defaultValue
                    if (c.getDefaultValue() == null) {
                        throw new IllegalArgumentException(String.format("TemplateConfig[category:%s, name:%s] must have a default value", c.getCategory(), c.getName()));
                    } else {
                        c.setDefaultValue(StringTemplate.substitute(c.getDefaultValue(), propertiesMap));
                    }
                    if (c.getValue() == null) {
                        c.setValue(c.getDefaultValue());
                    } else {
                        c.setValue(StringTemplate.substitute(c.getValue(), propertiesMap));
                    }
                    TemplateConfig config = TemplateConfig.valueOf(c);
                    if (configsFromXml.containsKey(config.getIdentity())) {
                        throw new IllegalArgumentException(String.format("duplicate TemplateConfig[category: %s, name: %s]", config.getCategory(), config.getName()));
                    }
                    configsFromXml.put(config.getIdentity(), config);
                }
            }

            private void link() {
                for (Field field : TemplateConfigFields) {
                    field.setAccessible(true);
                    try {
                        TemplateConfig config = (TemplateConfig) field.get(null);
                        if (config == null) {
                            throw new CloudRuntimeException(String.format("TemplateConfigDefinition[%s] defines a null TemplateConfig[%s]." +
                                            "You must assign a value to it using new TemplateConfig(category, name)",
                                    field.getDeclaringClass().getName(), field.getName()));
                        }

                        link(field, config);
                    } catch (IllegalAccessException e) {
                        throw new CloudRuntimeException(e);
                    }
                }

                for (TemplateConfig c : configsFromXml.values()) {
                    if (!c.isLinked()) {
                        logger.warn(String.format("TemplateConfig[category: %s, name: %s] is not linked to any definition", c.getCategory(), c.getName()));
                    }
                }
            }

            private void link(Field field, final TemplateConfig old) throws IllegalAccessException {
                TemplateConfig xmlConfig = configsFromXml.get(old.getIdentity());
                DebugUtils.Assert(xmlConfig != null, String.format("unable to find TemplateConfig[category:%s, name:%s] for linking to %s.%s",
                        old.getCategory(), old.getName(), field.getDeclaringClass().getName(), field.getName()));
                final TemplateConfig config = old.copy(xmlConfig);
                field.set(null, config);
                // all global config base on Field allConfig which is origin from configsFromXml, so update its value
                configsFromXml.put(old.getIdentity(), config);

                final TemplateConfigValidation at = field.getAnnotation(TemplateConfigValidation.class);
                if (at != null) {
                    config.installValidateExtension(new TemplateConfigValidatorExtensionPoint() {
                        @Override
                        public void validateTemplateConfig(String category, String name, String templateUuid, String oldValue, String value) throws TemplateConfigException {
                            if (at.notNull() && value == null) {
                                throw new TemplateConfigException(String.format("%s cannot be null", config.getCanonicalName()));
                            }
                        }
                    });

                    config.installValidateExtension(new TemplateConfigValidatorExtensionPoint() {
                        @Override
                        public void validateTemplateConfig(String category, String name, String templateUuid, String oldValue, String newValue) throws TemplateConfigException {
                            if (at.notEmpty() && newValue.trim().equals("")) {
                                throw new TemplateConfigException(String.format("%s cannot be empty string", config.getCanonicalName()));
                            }
                        }
                    });

                    if (at.inNumberRange().length > 0 || at.numberGreaterThan() != Long.MIN_VALUE || at.numberLessThan() != Long.MAX_VALUE) {
                        if (config.getType() != null && TypeUtils.isTypeOf(config.getType(), Long.class, Integer.class)) {
                            throw new CloudRuntimeException(String.format("%s has @TemplateConfigValidation defined on field[%s.%s] which indicates its numeric type, but its type is neither Long nor Integer, it's %s",
                                    config.getCanonicalName(), field.getDeclaringClass(), field.getName(), config.getType()));
                        }
                        if (config.getType() == null) {
                            logger.warn(String.format("%s has @TemplateConfigValidation defined on field[%s.%s] which indicates it's numeric type, but its is null, assume it's Long type",
                                    config.getCanonicalName(), field.getDeclaringClass(), field.getName()));
                            config.setType(Long.class.getName());
                        }
                    }

                    if (at.numberLessThan() != Long.MAX_VALUE) {
                        config.installValidateExtension(new TemplateConfigValidatorExtensionPoint() {
                            @Override
                            public void validateTemplateConfig(String category, String name, String templateUuid, String oldValue, String value) throws TemplateConfigException {
                                try {
                                    long num = Long.valueOf(value);
                                    if (num > at.numberLessThan()) {
                                        throw new TemplateConfigException(String.format("%s should not greater than %s, but got %s",
                                                config.getCanonicalName(), at.numberLessThan(), num));
                                    }
                                } catch (NumberFormatException e) {
                                    throw new TemplateConfigException(String.format("%s is not a number or out of range of a Long type", value), e);
                                }
                            }
                        });
                    }

                    if (at.numberGreaterThan() != Long.MIN_VALUE) {
                        config.installValidateExtension(new TemplateConfigValidatorExtensionPoint() {
                            @Override
                            public void validateTemplateConfig(String category, String name, String templateUuid, String oldValue, String value) throws TemplateConfigException {
                                try {
                                    long num = Long.valueOf(value);
                                    if (num < at.numberGreaterThan()) {
                                        throw new TemplateConfigException(String.format("%s should not less than %s, but got %s",
                                                config.getCanonicalName(), at.numberGreaterThan(), num));
                                    }
                                } catch (NumberFormatException e) {
                                    throw new TemplateConfigException(String.format("%s is not a number or out of range of a Long type", value), e);
                                }
                            }
                        });
                    }

                    if (at.inNumberRange().length > 0) {
                        DebugUtils.Assert(at.inNumberRange().length == 2, String.format("@TemplateConfigValidation.inNumberRange defined on field[%s.%s] must have two elements, where the first one is lower bound and the second one is upper bound",
                                field.getDeclaringClass(), field.getName()));

                        config.installValidateExtension(new TemplateConfigValidatorExtensionPoint() {
                            @Override
                            public void validateTemplateConfig(String category, String name, String templateUuid, String oldValue, String value) throws TemplateConfigException {
                                try {
                                    long num = Long.valueOf(value);
                                    long lowBound = at.inNumberRange()[0];
                                    long upBound = at.inNumberRange()[1];
                                    if (!(num >= lowBound && num <= upBound)) {
                                        throw new TemplateConfigException(String.format("%s must in range of [%s, %s]",
                                                config.getCanonicalName(), lowBound, upBound));
                                    }
                                } catch (NumberFormatException e) {
                                    throw new TemplateConfigException(String.format("%s is not a number or out of range of a Long type", value), e);
                                }
                            }
                        });
                    }

                    if (at.validValues().length > 0) {
                        final List<String> validValues = new ArrayList<String>();
                        Collections.addAll(validValues, at.validValues());
                        config.installValidateExtension(new TemplateConfigValidatorExtensionPoint() {
                            @Override
                            public void validateTemplateConfig(String category, String name, String templateUuid, String oldValue, String newValue) throws TemplateConfigException {
                                if (!validValues.contains(newValue)) {
                                    throw new TemplateConfigException(String.format("%s is not a valid value. Valid values are %s", newValue, validValues));
                                }
                            }
                        });
                    }
                }

                config.setConfigDef(field.getAnnotation(TemplateConfigDef.class));
                config.setLinked(true);
                if (logger.isTraceEnabled()) {
                    logger.trace(String.format("linked TemplateConfig[category:%s, name:%s, value:%s] to %s.%s",
                            config.getCategory(), config.getName(), config.getDefaultValue(), field.getDeclaringClass().getName(), field.getName()));
                }
            }
        }

        TemplateConfigInitializer initializer = new TemplateConfigInitializer();
        initializer.init();
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }


    public Map<String, TemplateConfig> getAllConfig() {
        return allConfig;
    }

    @Override
    public String updateTemplateConfig(String templateUuid, String category, String name , String value) {
        return null;
    }

    @Override
    public Map<String, TemplateConfig> getAllConfig(String templateUuid) {
        return null;
    }

    public <T> T getConfigValue( String templateUuid, String category, String name, Class<T> clz) {
        TemplateConfig c = allConfig.get(TemplateConfig.produceIdentity(templateUuid, category, name ));
        DebugUtils.Assert(c!=null, String.format("cannot find TemplateConfig[category:%s, name:%s]", category, name));
        return c.value(clz);
    }


    @Deprecated
    public TemplateConfig createTemplateConfig(TemplateConfigVO vo) {
        vo = dbf.persistAndRefresh(vo);
        TemplateConfig c = TemplateConfig.valueOf(vo);
        allConfig.put(TemplateConfig.produceIdentity(vo.getTemplateUuid(), vo.getCategory(), vo.getName()), c);
        return c;
    }


    @Deprecated
    public String updateConfig(String templateUuid, String category, String name , String value) {
        TemplateConfig c = allConfig.get(TemplateConfig.produceIdentity(templateUuid, category,name));
        DebugUtils.Assert(c != null, String.format("cannot find TemplateConfig[category:%s, name:%s, templateUuid:%s]", category, name, templateUuid));
        c.updateValue(value);
        return c.getValue();
    }
}
