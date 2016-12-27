package scripts

import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
import org.zstack.header.errorcode.ErrorCode
import org.zstack.header.exception.CloudRuntimeException
import org.zstack.header.identity.SuppressCredentialCheck
import org.zstack.header.message.APIParam
import org.zstack.header.rest.APINoSee
import org.zstack.header.rest.RestRequest
import org.zstack.header.rest.RestResponse
import org.zstack.header.vm.APIStartVmInstanceEvent
import org.zstack.header.zone.APIChangeZoneStateMsg
import org.zstack.rest.RestConstants
import org.zstack.rest.sdk.DocumentGenerator
import org.zstack.utils.FieldUtils
import org.zstack.utils.ShellUtils
import org.zstack.utils.Utils
import org.zstack.utils.gson.JSONObjectUtil
import org.zstack.utils.logging.CLogger
import org.zstack.utils.path.PathUtil

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Created by xing5 on 2016/12/21.
 */
class RestDocumentationGenerator implements DocumentGenerator {
    CLogger logger = Utils.getLogger(RestDocumentationGenerator.class)

    String rootPath

    List<Doc> docs = []
    Map<String, File> sourceFiles = [:]

    def MUTUAL_FIELDS = [
            "lastOpDate": "最后一次修改时间",
            "createDate": "创建时间",
            "uuid": "资源的UUID，唯一标示该资源",
            "name": "资源名称",
            "description": "资源的详细描述",
            "primaryStorageUuid": "主存储UUID",
            "vmInstanceUuid": "云主机UUID",
            "imageUuid": "镜像UUID",
            "backupStorageUuid": "镜像存储UUID",
            "volumeUuid": "云盘UUID",
            "zoneUuid": "区域UUID",
            "clusterUuid": "集群UUID",
            "hostUuid": "物理机UUID",
            "l2NetworkUuid": "二层网络UUID",
            "l3NetworkUuid": "三层网络UUID",
            "accountUuid": "账户UUID",
            "policyUuid": "权限策略UUID",
            "userUuid": "用户UUID",
            "diskOfferingUuid": "云盘规格UUID",
            "volumeSnapshotUuid": "云盘快照UUID",
            "ipRangeUuid": "IP段UUID",
            "instanceOfferingUuid": "计算规格UUID",
            "vipUuid": "VIP UUID",
            "vmNicUuid": "云主机网卡UUID",
            "networkServiceProviderUuid": "网络服务提供模块UUID",
            "virtualRouterUuid": "云路由UUID",
            "securityGroupUuid": "安全组UUID",
            "eipUuid": "弹性IP UUID",
            "loadBalancerUuid": "负载均衡器UUID"
    ]

    def installClosure(ExpandoMetaClass emc, Closure c) {
        c(emc)
    }

    class RequestParamColumn {
        private String _name
        private String _desc
        private boolean _optional
        private List _values
        private String _since
        private String _type

        def since(String v) {
            _since = v
        }

        def type(String v) {
            _type = v
        }

        def name(String v) {
            _name = v
        }

        def desc(String v) {
            _desc = v
        }

        def optional(boolean v) {
            _optional = v
        }

        def values(Object...args) {
            _values = args as List
        }
    }

    class RequestParamRef extends RequestParam {
        String ref
    }

    class RequestParam {
        private List<RequestParamColumn> _cloumns = []
        private String _desc

        def column(Closure c) {
            def rc = c.delegate = new RequestParamColumn()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _cloumns.add(rc)
        }

        def desc(String v) {
            _desc = v
        }
    }

    class Rest {
        private Request _request
        private Response _response

        def response(Closure c) {
            c.delegate = new Response()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _response = c.delegate as Response
        }

        def request(Closure c) {
            c.delegate = new Request()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _request = c.delegate as Request
        }
    }

    class Response {
        private Class _clz
        private String _desc

        def clz(Class v)  {
            _clz = v
        }

        def desc(String v) {
            _desc = v
        }
    }

    class Header {
        String key
        String value
        String comment
    }

    class Url {
        String url
        String comment
    }

    class Request {
        private List<Url> _urls = []
        private List<Header> _headers = []
        private String _desc
        private RequestParam _params
        private LinkedHashMap _body
        private Class _clz

        def url(String v, String comment) {
            def u = new Url()
            u.url = v
            u.comment = comment
            _urls.add(u)
        }

        def url(String v) {
            url(v, null)
        }

        def header(Map v) {
            header(v, null)
        }

        def header(Map v, String comment) {
            Map.Entry e = v.entrySet().iterator().next()
            def h = new Header()
            h.key = e.key
            h.value = e.value
            h.comment = comment
            _headers.add(h)
        }

        def desc(String v) {
            _desc = v
        }

        def params(String v) {
            def p = new RequestParamRef()
            p.ref = v

            _params = p
        }

        def params(Closure c) {
            c.delegate = new RequestParam()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _params = c.delegate
        }

        def body(Closure c) {
            def j = new JsonBuilder()
            j.call(c)
            _body = JSONObjectUtil.toObject(j.toString(), LinkedHashMap.class)
        }

        def clz(Class v){
            _clz = v
        }
    }

    class Field {
        String _name
        String _desc
        String _type

        def name(String v) {
            _name = v
        }

        def desc(String v) {
            _desc = v
        }

        def type(String v) {
            _type = v
        }
    }

    class FieldRef {
        String _name
        String _type
        String _path
        String _desc
        Class _clz

        def name(String v) {
            _name = v
        }

        def type(String v) {
            _type = v
        }

        def path(String v) {
            _path = v
        }

        def desc(String v) {
            _desc = v
        }

        def clz(Class v) {
            _clz = v
        }
    }

    class Doc {
        private String _title
        private String _desc
        private Rest _rest
        private List<Field> _fields = []
        private List<FieldRef> _refs = []

        def title(String v) {
            _title = v
        }

        def desc(String v) {
            _desc = v
        }

        def rest(Closure c) {
            c.delegate = new Rest()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _rest = c.delegate
        }

        def field(Closure c) {
            c.delegate = new Field()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _fields.add(c.delegate as Field)
        }

        def ref(Closure c) {
            c.delegate = new FieldRef()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _refs.add(c.delegate as FieldRef)
        }
    }

    Doc createDoc(String docTemplatePath) {
        Script script = new GroovyShell().parse(new File(docTemplatePath))
        ExpandoMetaClass emc = new ExpandoMetaClass(script.getClass(), false, true)

        installClosure(emc, { ExpandoMetaClass e ->
            e.doc = { Closure cDoc ->
                cDoc.delegate = new Doc()
                cDoc.resolveStrategy = DELEGATE_FIRST

                cDoc()

                return cDoc.delegate
            }
        })

        emc.initialize()

        script.setMetaClass(emc)

        return script.run() as Doc
    }

    class MarkDown {
        Doc doc

        MarkDown(String docTemplatePath) {
            doc = createDoc(docTemplatePath)
        }

        String url() {
            def urls = doc._rest._request._urls
            if (urls == null || urls.isEmpty()) {
                throw new CloudRuntimeException("cannot find urls for the class[${doc._rest._request._clz.name}]")
            }

            def txts = urls.collect {
                return it.comment == null ? it.url : "${it.url} #${it.comment}"
            }

            return """\
**URLs**

```
${txts.join("\n")}
```
"""
        }

        String headers() {
            if (doc._rest._request._headers.isEmpty()) {
                return ""
            }

            def hs = []
            doc._rest._request._headers.each { h ->
                if (h.comment != null) {
                    hs.add("${h.key}: ${h.value} #${h.comment}")
                } else {
                    hs.add("${h.key}: ${h.value}")
                }
            }

            return """\
**Headers**
```
${hs.join("\n")}
```
"""
        }

        String params() {
            if (doc._rest._request._params == null) {
                return ""
            }

            if (doc._rest._request._params instanceof RequestParamRef) {
                String ref = (doc._rest._request._params as RequestParamRef).ref
                String fname = ref - "Doc_.groovy"
                File srcFile = getSourceFile(fname)

                String docFilePath = PathUtil.join(srcFile.parent, ref)
                Doc refDoc = createDoc(docFilePath)

                doc._rest._request._params = refDoc._rest._request._params
            }

            def cols = doc._rest._request._params?._cloumns

            if (cols == null || cols.isEmpty()) {
                return ""
            }

            def table = ["|名字|类型|描述|默认可选值|起始版本|"]
            table.add("|---|---|---|---|---|")
            cols.each {
                def col = []
                col.add(it._optional ? "${it._name} (可选)" : "${it._name}")
                col.add(it._type)
                col.add(it._desc)
                if (it._values == null || it._values.isEmpty()) {
                    col.add("")
                } else {
                    def vals = it._values.collect { v ->
                        return "<li>${v}</li>"
                    }
                    col.add("<ul>${vals.join("")}</ul>")
                }
                col.add(it._since)

                table.add("|${col.join("|")}|")
            }

            return """\
**参数列表**

${doc._rest._request._params._desc == null ? "" : doc._rest._request._params._desc}

${table.join("\n")}
"""
        }

        LinkedHashMap getApiExampleOfTheClass(Class clz) {
            Method m = clz.getMethod("__example__")
            def example = m.invoke(null)

            LinkedHashMap map = JSONObjectUtil.rehashObject(example, LinkedHashMap.class)
            def apiFieldNames = []
            getApiFieldsOfClass(clz).each { apiFieldNames.add(it.name) }

            LinkedHashMap paramMap = [:]
            map.each { k, v ->
                if (apiFieldNames.contains(k)) {
                    paramMap[k] = v
                }
            }

            return paramMap
        }

        String requestExample() {
            if (doc._rest._request._clz == null) {
                return ""
            }

            Class clz = doc._rest._request._clz

            RestRequest at = clz.getAnnotation(RestRequest.class)
            if ((at.parameterName() == "" && !at.isAction()) || at.parameterName() == "null") {
                // no body
                return ""
            }

            String paramName
            if (!at.isAction()) {
                paramName = at.parameterName()
            } else {
                paramName = StringUtils.removeStart(clz.simpleName, "API")
                paramName = StringUtils.removeEnd(paramName, "Msg")
                paramName = StringUtils.uncapitalize(paramName)
            }

            try {
                def apiMap = [
                        (paramName): getApiExampleOfTheClass(clz),
                        systemTags: [],
                        userTags: []
                ]

                return """\
**Body**

```
${JSONObjectUtil.dumpPretty(apiMap)}
```

> 上述示例中`systemTags`、`userTags`字段可以省略。列出是为了表示body中可以包含这两个字段。

"""
            } catch (NoSuchMethodException e) {
                throw new CloudRuntimeException("class[${clz.name}] doesn't have static __example__ method", e)
            }
        }

        String responseDesc() {
            return doc._rest._response._desc == null ? "" : doc._rest._response._desc
        }

        String responseExample() {
            Class clz = doc._rest._response._clz
            if (clz == null) {
                throw new CloudRuntimeException("${doc._rest} doesn't have 'clz' specified in the response body")
            }

            File sourceFile = getSourceFile(clz.simpleName - ".java")
            String docFilePath = PathUtil.join(sourceFile.parent, classToDocFileName(clz))
            Doc doc = createDoc(docFilePath)

            LinkedHashMap map = getApiFieldsOfClass(clz)

            def cols = []
            if (map.isEmpty()) {
                cols.add("""\
该API成功时返回一个空的JSON结构`{}`，出错时返回的JSON结构包含一个error字段，例如：

```
{
\t"error": {
\t\t"code": "SYS.1001",
\t\t"description": "A message or a operation timeout",
\t\t"details": "Create VM on KVM timeout after 300s"
\t}
}
```


""")
            } else {
                cols.add("""\
### 返回示例

```
${JSONObjectUtil.dumpPretty(map)}
```

""")
            }


        }

        void write() {
            def template = """\
## ${doc._title}

${doc._desc}

## API请求

${url()}

${headers()}

${requestExample()}

${params()}

## APIf返回

${responseDesc()}
"""
            System.out.println(template)
        }
    }

    class DataStructMarkDown {
        Doc doc

        DataStructMarkDown(Doc doc) {
            this.doc = doc
        }

        String generate() {
            def tables = []

            def rows = []
            def table = ["|名字|类型|描述|起始版本|"]
            table.add("|---|---|---|---|")

            doc._fields.each {
                def row = []
                row.add(it._name)
                row.add(it._type)
                row.add(it._desc)

                rows.add("|${row.join("|")}|")
            }

            doc._refs.each {
                def row = []
                row.add(it._name)
                row.add(it._type)
                row.add("${it._desc}, 详情参考[这里](#${it._path})")

                rows.add("|${row.join("|")}|")
            }

            tables.add(rows.join("\n"))

            // generate dependent tables

            doc._refs.each {
                String path = getDocTemplatePathFromClass(it._clz)
                Doc refDoc = createDoc(path)

                tables.add("""\


<a name="#${it._path}">**${doc._title} #${it._name}**<a/>
${new DataStructMarkDown(refDoc).generate()}
""")
            }

            return tables.join("\n")
        }
    }

    String getDocTemplatePathFromClass(Class clz) {
        File srcFile = getSourceFile(clz.simpleName - ".java")
        String docName = classToDocFileName(clz)
        return PathUtil.join(srcFile.parent, docName)
    }

    String classToDocFileName(Class clz) {
        return "${clz.simpleName}Doc_.groovy"
    }

    void generateDocTemplates() {
        /*
Map<String, File> files = [:]

File root = new File(rootPath)
root.traverse { f ->
    files[f.name] = f
}

Set<Class> apiClasses = Platform.getReflections().getTypesAnnotatedWith(RestRequest.class)
apiClasses.each {
    String srcName = "${it.simpleName}.java"
    File srcFile = files[srcName]
    if (srcFile == null) {
        throw new CloudRuntimeException("cannot find the source file for the class[${it.name}]")
    }

    def tmp = new DocTemplate(it, srcFile)
    System.out.println(tmp.generate())
}
*/

        def tmp = new ApiRequestDocTemplate(APIChangeZoneStateMsg.class, new File("/root/zstack/header/src/main/java/org/zstack/header/zone/APIChangeZoneStateMsg.java"))
        tmp.generateDocFile()
    }

    def PRIMITIVE_TYPES = [
            int.class,
            long.class,
            float .class,
            boolean .class,
            double.class,
            short.class,
            char.class,
            String.class
    ]

    class ApiResponseDocTemplate {
        Set<Class> laterResolveClasses = []

        Class responseClass
        File sourceFile
        RestResponse at

        List<String> imports = []
        Map<String, Field> fsToAdd = [:]
        List<String> fieldStrings = []

        ApiResponseDocTemplate(Class responseClass) {
            this.responseClass = responseClass
            sourceFile = getSourceFile(responseClass.simpleName)

            at = responseClass.getAnnotation(RestResponse.class)
            if (at != null) {
                findFieldsForRestResponse()
            } else {
                findFieldsForNormalClass()
            }
        }

        def findFieldsForNormalClass() {
            List<Field> allFields = FieldUtils.getAllFields(responseClass)
            allFields = allFields.findAll { !it.isAnnotationPresent(APINoSee.class) && !Modifier.isStatic(it.modifiers) }
            allFields.each { fsToAdd[it.name] = it }
        }

        def findFieldsForRestResponse() {
            if (at.allTo() == null && at.fieldsTo().length == 0) {
                return ""
            }

            if (at.allTo() != "") {
                fsToAdd[at.allTo()] = responseClass.getDeclaredField(at.allTo())
            } else if (at.fieldsTo().length == 1 && at.fieldsTo()[0] == "all") {
                findFieldsForNormalClass()
            } else {
                at.fieldsTo().each {
                    def kv = it.split("=")
                    def k = kv[0].trim()
                    def v = kv[1].trim()
                    fsToAdd[k] = FieldUtils.getField(v, responseClass)
                }
            }

            fieldStrings.add(createRef("error", "错误码，如果不为null，则表示操作失败", ErrorCode.class))
        }

        String createField(String n, String desc, String type) {
            if (MUTUAL_FIELDS.containsKey(n)) {
                desc = "${desc == null || desc.isEmpty() ? MUTUAL_FIELDS[n] : MUTUAL_FIELDS[n] + "," + desc}"
            }

            return """\tfield {
\t\tname "${n}"
\t\tdesc "${desc == null ? "" : desc}"
\t\ttype "${type}"
\t}"""
        }

        String createRef(String path, String desc, Class clz) {
            laterResolveClasses.add(clz)
            imports.add("import ${clz.package.name}.${clz.simpleName}")

            desc = desc == null || desc == "" ? "结构字段，参考[这里](#${path})获取详细信息" : "${desc}。结构字段，参考[这里](#${path})获取详细信息"

            return """\tref {
\t\tpath "${path}"
\t\tdesc "${desc}"
\t\tclz ${clz.simpleName}.class
\t}"""
        }

        String fields() {
            fsToAdd.each { k, v ->
                if (PRIMITIVE_TYPES.contains(v.type)) {
                    fieldStrings.add(createField(k, "", v.type.simpleName))
                } else if (v.type.name.startsWith("java.")) {
                    if (Collection.class.isAssignableFrom(v.type) || Map.class.isAssignableFrom(v.type)) {
                        Class gtype = FieldUtils.getGenericType(v)

                        if (gtype == null) {
                            fieldStrings.add(createField(k, "", v.type.simpleName))
                        } else {
                            fieldStrings.add(createRef("${responseClass.name}.${v.name}", null, gtype))
                        }
                    } else {
                        // java time but not primitive, needs import
                        imports.add("import ${v.type.name}")
                        fieldStrings.add(createField(k, "", v.type.simpleName))
                    }
                } else {
                    fieldStrings.add(createRef("${responseClass.name}.${v.name}", "", v.type))
                }
            }

            return fieldStrings.join("\n")
        }

        String generate() {
            String fieldStr = fields()

            return """${responseClass.package}

${imports.join("\n")}

doc {
${fieldStr}
}
"""
        }
    }

    class ApiRequestDocTemplate {
        Class apiClass
        File sourceFile
        RestRequest at

        ApiRequestDocTemplate(Class apiClass, File src) {
            this.apiClass = apiClass
            this.sourceFile = src
            at = apiClass.getAnnotation(RestRequest.class)
        }

        String headers() {
            if (apiClass.isAnnotationPresent(SuppressCredentialCheck.class)) {
                return ""
            }

            return """header (${RestConstants.HEADER_OAUTH}: 'the-session-uuid')"""
        }

        String params() {
            def apiFields = getApiFieldsOfClass(apiClass)

            if (apiFields.isEmpty()) {
                return ""
            }

            def cols = []
            for (Field af : apiFields) {
                APIParam ap = af.getAnnotation(APIParam.class)

                String values = null
                if (ap != null && ap.validValues().length != 0) {
                    def l = []
                    ap.validValues().each {
                        l.add("\"${it}\"")
                    }
                    values = "values (${l.join(",")})"
                }

                cols.add("""\t\t\t\tcolumn {
\t\t\t\t\tname "${af.name}"
\t\t\t\t\tdesc ""
\t\t\t\t\ttype "${af.type.simpleName}"
\t\t\t\t\toptional ${ap == null ? false : ap.required()}
\t\t\t\t\tsince "0.6"
\t\t\t\t\t${values == null ? "" : values}
\t\t\t\t}""")
            }

            if (cols.isEmpty()) {
                return ""
            }

            return """\t\t\tparams {

${cols.join("\n")}
\t\t\t}"""
        }

        String generate() {
            return """package ${apiClass.package.name}

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "${at.method().toString()} ${at.path()}"

            ${headers()}

            clz ${apiClass.simpleName}.class

            desc ""
            
${params()}
        }

        response {
            clz ${at.responseClass().simpleName}.class
        }
    }
}"""
        }

        void generateDocFile() {
            def docFileName = "${apiClass.simpleName}Doc_.groovy"
            def docFilePath = [sourceFile.parent, docFileName].join("/")
            System.out.println(docFilePath)
            new File(docFilePath).write generate()
        }
    }

    def getApiFieldsOfClass(Class apiClass) {
        def apiFields = []

        FieldUtils.getAllFields(apiClass).each {
            if (it.isAnnotationPresent(APINoSee.class)) {
                return
            }

            if (Modifier.isStatic(it.modifiers)) {
                return
            }

            apiFields.add(it)
        }

        return apiFields
    }

    void generateResponseDocTemplates() {
        Set<Class> todo = []
        Set<Class> resolved = []

        def tmp = new ApiResponseDocTemplate(APIStartVmInstanceEvent.class)
        System.out.println(tmp.generate())
        todo.addAll(tmp.laterResolveClasses)

        while (!todo.isEmpty()) {
            Set<Class> set = []
            todo.each {
                def t = new ApiResponseDocTemplate(it)
                System.out.println(t.generate())
                resolved.add(it)
                set.addAll(t.laterResolveClasses)
            }

            todo = set - resolved
        }
    }

    @Override
    void generateDocTemplates(String scanPath) {
        rootPath = scanPath
        scanJavaSourceFiles()
        //generateDocTemplates()

        def md = new MarkDown("/root/zstack/header/src/main/java/org/zstack/header/zone/APIChangeZoneStateMsgDoc_.groovy")
        md.write()

        generateResponseDocTemplates()
    }

    File getSourceFile(String name) {
        File f = sourceFiles[name]
        if (f == null) {
            throw new CloudRuntimeException("cannot find source file ${name}.java")
        }

        return f
    }

    def scanJavaSourceFiles() {
        String output = ShellUtils.run("find ${rootPath} -name *.java")
        List<String> paths = output.split("\n")
        paths = paths.findAll { !(it - "\n" - "\r" - "\t").trim().isEmpty()}

        paths.each {
            def f = new File(it)
            sourceFiles[f.name - ".java"] = f
        }
    }
}
