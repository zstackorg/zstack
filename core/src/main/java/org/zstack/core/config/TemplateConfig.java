package org.zstack.core.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.zstack.core.Platform;
import org.zstack.core.cloudbus.EventCallback;
import org.zstack.core.cloudbus.EventFacade;
import org.zstack.core.config.TemplateConfigCanonicalEvents.UpdateEvent;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.core.db.Q;
import org.zstack.core.db.SimpleQuery;
import org.zstack.core.db.SimpleQuery.Op;
import org.zstack.header.exception.CloudRuntimeException;
import org.zstack.utils.CollectionUtils;
import org.zstack.utils.DebugUtils;
import org.zstack.utils.TypeUtils;
import org.zstack.utils.Utils;
import org.zstack.utils.data.StringTemplate;
import org.zstack.utils.function.ForEachFunction;
import org.zstack.utils.gson.JSONObjectUtil;
import org.zstack.utils.logging.CLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.zstack.utils.CollectionDSL.e;
import static org.zstack.utils.CollectionDSL.map;
import static org.zstack.utils.StringDSL.s;

/**
 */
@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE)
public class TemplateConfig {
    private static final CLogger logger = Utils.getLogger(TemplateConfig.class);

    private String name;
    private String category;
    private String templateUuid;
    private String validatorRegularExpression;
    private String defaultValue;
    private volatile String value;
    private boolean linked;
    private transient List<TemplateConfigUpdateExtensionPoint> updateExtensions = new ArrayList<>();
    private transient List<TemplateConfigBeforeUpdateExtensionPoint> beforeUpdateExtensions = new ArrayList<>();
    private transient List<TemplateConfigValidatorExtensionPoint> validators = new ArrayList<>();
    private transient List<TemplateConfigUpdateExtensionPoint> localUpdateExtensions = new ArrayList<>();
    private transient List<TemplateConfigBeforeUpdateExtensionPoint> localBeforeUpdateExtensions = new ArrayList<>();
    private TemplateConfigDef configDef;

    private static Map<String, String> propertiesMap = new HashMap<>();
    static {
        boolean noTrim = System.getProperty("DoNotTrimPropertyFile") != null;
        for (final String name : System.getProperties().stringPropertyNames()) {
            String value = System.getProperty(name);
            if (!noTrim) {
                value = value.trim();
            }
            propertiesMap.put(name, value);
        }
    }

    @Autowired
    private DatabaseFacade dbf;
    @Autowired
    private EventFacade evtf;

    public EventFacade getEvtf() {
        return evtf;
    }

    @Override
    public String toString() {
        return JSONObjectUtil.toJsonString(map(
                e("name", name),
                e("category", category),
                e("templateUuid", templateUuid),
                e("defaultValue", defaultValue),
                e("value", value),
                e("validatorRegularExpression", validatorRegularExpression)
        ));
    }

    public void normalize() {
        if (type == null) {
            // means String, no need to normalize
            return;
        }

        // for bug: http://dev.zstack.io/browse/ZSTAC-8753
        DebugUtils.Assert(value != null, String.format("value cannot be null, category: %s, name: %s", category, name));

        try {
            Class clz = Class.forName(type);
            Object v = TypeUtils.stringToValue(value, clz);
            value = v.toString();
        } catch (ClassNotFoundException e) {
            throw new CloudRuntimeException(e);
        }
    }

    public TemplateConfig(String category, String name) {
        this.category = category;
        this.name = name;
    }

    TemplateConfig() {
    }

    TemplateConfig copy(TemplateConfig g){
        setName(g.getName());
        setCategory(g.getCategory());
        setDescription(g.getDescription());
        setType(g.getType());
        setValidatorRegularExpression(g.getValidatorRegularExpression());
        setDefaultValue(g.getDefaultValue());
        setValue(g.value());
        setLinked(g.isLinked());

        validators = new ArrayList<>();
        updateExtensions = new ArrayList<>();
        localUpdateExtensions = new ArrayList<>();
        beforeUpdateExtensions = new ArrayList<>();

        updateExtensions.addAll(g.getUpdateExtensions());
        beforeUpdateExtensions.addAll(g.getBeforeUpdateExtensions());
        localUpdateExtensions.addAll(g.getLocalUpdateExtensions());
        validators.addAll(g.getValidators());
        configDef = g.getConfigDef();
        return this;
    }

    private String makeUpdateEventPath() {
        return s(TemplateConfigCanonicalEvents.UPDATE_EVENT_PATH).formatByMap(map(
                e("nodeUuid", Platform.getManagementServerId()),
                e("category", category),
                e("name", name)
        ));
    }

    public TemplateConfigVO reload() {
        SimpleQuery<TemplateConfigVO> q = dbf.createQuery(TemplateConfigVO.class);
        q.add(TemplateConfigVO_.category, Op.EQ, category);
        q.add(TemplateConfigVO_.name, Op.EQ, name);
        return q.find();
    }

    public void installLocalUpdateExtension(TemplateConfigUpdateExtensionPoint ext) {
        localUpdateExtensions.add(ext);
    }

    public void installUpdateExtension(TemplateConfigUpdateExtensionPoint ext) {
        updateExtensions.add(ext);
    }

    public void installBeforeUpdateExtension(TemplateConfigBeforeUpdateExtensionPoint ext) {
        beforeUpdateExtensions.add(ext);
    }

    public void installLocalBeforeUpdateExtension(TemplateConfigBeforeUpdateExtensionPoint ext) {
        localBeforeUpdateExtensions.add(ext);
    }

    public void installValidateExtension(TemplateConfigValidatorExtensionPoint ext) {
        validators.add(ext);
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    public String getValidatorRegularExpression() {
        return validatorRegularExpression;
    }

    void setValidatorRegularExpression(String validatorRegularExpression) {
        this.validatorRegularExpression = validatorRegularExpression;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String value() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    public <T> T value(Class<T> clz) {
        return TypeUtils.stringToValue(value(), clz);
    }

    public <T> T defaultValue(Class<T> clz) {
        return TypeUtils.stringToValue(defaultValue, clz);
    }

    public static TemplateConfig valueOf(TemplateConfigVO vo) {
        TemplateConfig conf = new TemplateConfig();
        conf.setName(vo.getName());
        conf.setCategory(vo.getCategory());
        conf.setDefaultValue(vo.getDefaultValue());
        conf.setDescription(vo.getDescription());
        conf.setValue(vo.getValue());
        return conf;
    }

    public static TemplateConfig valueOf(TemplateConfig old) {
        TemplateConfig ng = new TemplateConfig();
        ng.setName(old.getName());
        ng.setValue(old.value());
        ng.setCategory(old.getCategory());
        ng.setDescription(old.getDescription());
        ng.setDefaultValue(old.getDefaultValue());
        ng.setValidatorRegularExpression(old.getValidatorRegularExpression());
        return ng;
    }

    public TemplateConfigVO toVO() {
        TemplateConfigVO vo = new TemplateConfigVO();
        vo.setCategory(category);
        vo.setValue(value);
        vo.setDescription(description);
        vo.setDefaultValue(defaultValue);
        vo.setName(name);
        return vo;
    }

    public static TemplateConfig valueOf(org.zstack.core.config.schema.TemplateConfig.Config c) {
        TemplateConfig conf = new TemplateConfig();
        conf.setName(c.getName());
        conf.setCategory(c.getCategory());
        conf.setDefaultValue(c.getDefaultValue());
        conf.setDescription(c.getDescription());
        conf.setValue(c.getValue());
        conf.setType(c.getType());
        return conf;
    }

    public String getIdentity() {
        return produceIdentity(category, name);
    }

    public static String produceIdentity(String category, String name) {
        return String.format("%s.%s", category, name);
    }

    void validate() {
        validate(value);
    }

    public void validate(String newValue) {
        for  (TemplateConfigValidatorExtensionPoint ext : validators) {
            ext.validateTemplateConfig(category, name, value, newValue);
        }
    }

    void init() {
        evtf.on(s(TemplateConfigCanonicalEvents.UPDATE_EVENT_PATH).formatByMap(map(
                e("category", category),
                e("name", name)
        )), new EventCallback() {
            @Override
            public void run(Map tokens, Object data) {
                String nodeUuid = (String) tokens.get("nodeUuid");
                if (Platform.getManagementServerId().equals(nodeUuid)) {
                    return;
                }

                String newValue = Q.New(TemplateConfigVO.class).select(TemplateConfigVO_.value)
                        .eq(TemplateConfigVO_.category, category)
                        .eq(TemplateConfigVO_.name, name)
                        .findValue();
                update(newValue, false);

                UpdateEvent evt = (UpdateEvent)data;
                logger.info(String.format("TemplateConfig[category: %s, name: %s] was updated in other management node[uuid:%s]," +
                        "in line with that change, updated ours. %s --> %s", category, name, nodeUuid, evt.getOldValue(), value));
            }
        });
    }

    private void update(String newValue, boolean localUpdate) {
        // substitute system properties in newValue
        newValue = StringTemplate.substitute(newValue, propertiesMap);

        validate(newValue);

        SimpleQuery<TemplateConfigVO> q = dbf.createQuery(TemplateConfigVO.class);
        q.add(TemplateConfigVO_.category, Op.EQ, category);
        q.add(TemplateConfigVO_.name, Op.EQ, name);
        TemplateConfigVO vo = q.find();
        final TemplateConfig origin = valueOf(vo);

        for (TemplateConfigBeforeUpdateExtensionPoint ext : beforeUpdateExtensions) {
            try {
                ext.beforeUpdateExtensionPoint(origin, newValue);
            } catch (Throwable t) {
                logger.warn(String.format("unhandled exception when calling %s", ext.getClass()), t);
            }
        }

        if (localUpdate) {
            for (TemplateConfigBeforeUpdateExtensionPoint ext : localBeforeUpdateExtensions) {
                try {
                    ext.beforeUpdateExtensionPoint(origin, newValue);
                } catch (Throwable t) {
                    logger.warn(String.format("unhandled exception when calling %s", ext.getClass()), t);
                }
            }
        }

        value = newValue;

        if (localUpdate) {
            vo.setValue(newValue);
            dbf.update(vo);

            final TemplateConfig self = this;
            CollectionUtils.safeForEach(localUpdateExtensions, new ForEachFunction<TemplateConfigUpdateExtensionPoint>() {
                @Override
                public void run(TemplateConfigUpdateExtensionPoint ext) {
                    ext.updateTemplateConfig(origin, self);
                }
            });
        }

        for (TemplateConfigUpdateExtensionPoint ext : updateExtensions) {
            try {
                ext.updateTemplateConfig(origin, this);
            } catch (Throwable t) {
                logger.warn(String.format("unhandled exception when calling %s", ext.getClass()), t);
            }
        }

        if (localUpdate) {
            UpdateEvent evt = new UpdateEvent();
            evt.setOldValue(origin.value());
            evt.setNewValue(newValue);
            evtf.fire(makeUpdateEventPath(), evt);
        }

        logger.debug(String.format("updated global config[category:%s, name:%s]: %s to %s", category, name, origin.value(), value));
    }

    public void updateValue(Object val) {
        if (TypeUtils.nullSafeEquals(value, val)) {
            return;
        }

        String newValue = val == null ? null : val.toString();
        update(newValue, true);
    }

    boolean isLinked() {
        return linked;
    }

    void setLinked(boolean linked) {
        this.linked = linked;
    }

    public boolean isMe(TemplateConfig other) {
        return category.equals(other.getCategory()) && name.equals(other.getName());
    }

    public TemplateConfigDef getConfigDef() {
        return configDef;
    }

    public void setConfigDef(TemplateConfigDef configDef) {
        this.configDef = configDef;
    }

    public String getCanonicalName() {
        return String.format("Global config[category: %s, name: %s]", category, name);
    }

    void setValidators(List<TemplateConfigValidatorExtensionPoint> validators) {
        this.validators = validators;
    }

    void setUpdateExtensions(List<TemplateConfigUpdateExtensionPoint> updateExtensions) {
        this.updateExtensions = updateExtensions;
    }

    void setLocalUpdateExtensions(List<TemplateConfigUpdateExtensionPoint> localUpdateExtensions) {
        this.localUpdateExtensions = localUpdateExtensions;
    }

    public List<TemplateConfigValidatorExtensionPoint> getValidators() {
        return validators;
    }

    public List<TemplateConfigUpdateExtensionPoint> getUpdateExtensions() {
        return updateExtensions;
    }

    public List<TemplateConfigBeforeUpdateExtensionPoint> getBeforeUpdateExtensions() {
        return beforeUpdateExtensions;
    }

    public List<TemplateConfigUpdateExtensionPoint> getLocalUpdateExtensions() {
        return localUpdateExtensions;
    }

    public List<TemplateConfigBeforeUpdateExtensionPoint> getLocalBeforeUpdateExtensions() {
        return localBeforeUpdateExtensions;
    }
}
