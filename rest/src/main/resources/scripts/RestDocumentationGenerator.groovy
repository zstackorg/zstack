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

        def clz(Class v)  {
            _clz = v
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

    class Doc {
        private String _title
        private String _desc
        private Rest _rest

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

                def apiMap = [
                        (paramName): paramMap,
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

        void write() {
            def template = """\
## ${doc._title}

${doc._desc}

## API请求

${url()}

${headers()}

${requestExample()}

${params()}
"""
            System.out.println(template)
        }
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
            return """\tfield {
\t\tname "${n}"
\t\tdesc "${desc == null ? "" : desc}"
\t\ttype "${type}
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
                            fieldStrings.add(createRef("${responseClass.name}.${v.name}", null, v.type))
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

        def tmp = new ApiResponseDocTemplate(APIStartVmInstanceEvent.class)
        System.out.println(tmp.generate())
        todo.addAll(tmp.laterResolveClasses)

        while (!todo.isEmpty()) {
            Set<Class> set = []
            todo.each {
                def t = new ApiResponseDocTemplate(it)
                System.out.println(t.generate())
                set.addAll(t.laterResolveClasses)
            }

            todo = set
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
