package scripts

import groovy.json.JsonBuilder
import org.zstack.core.Platform
import org.zstack.header.exception.CloudRuntimeException
import org.zstack.header.identity.SuppressCredentialCheck
import org.zstack.header.message.APIParam
import org.zstack.header.rest.APINoSee
import org.zstack.header.rest.RestRequest
import org.zstack.rest.RestConstants
import org.zstack.rest.sdk.DocumentGenerator
import org.zstack.utils.FieldUtils
import org.zstack.utils.Utils
import org.zstack.utils.gson.JSONObjectUtil
import org.zstack.utils.logging.CLogger

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Created by xing5 on 2016/12/21.
 */
class RestDocumentationGenerator implements DocumentGenerator {
    CLogger logger = Utils.getLogger(RestDocumentationGenerator.class)

    String rootPath

    List<Doc> docs = []

    def installClosure(ExpandoMetaClass emc, Closure c) {
        c(emc)
    }

    class RequestParamColumn {
        private String _name
        private String _desc
        private boolean _optional
        private List _values

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

    class RequestParam {
        private List<RequestParamColumn> _cloumns = []

        def column(Closure c) {
            def rc = c.delegate = new RequestParamColumn()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _cloumns.add(rc)
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
        private LinkedHashMap _body
        private Class _clz

        def body(Closure c) {
            def j = new JsonBuilder()
            j.call(c)

            _body = JSONObjectUtil.toObject(j.toString(), LinkedHashMap.class)
        }

        def clz(Class v)  {
            _clz = v
        }
    }

    class Request {
        private String _url
        private Map _header
        private String _desc
        private RequestParam _params
        private LinkedHashMap _body
        private Class _clz

        def url(String v) {
            _url = v
        }

        def header(Map v) {
            _header = v
        }

        def desc(String v) {
            _desc = v
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

    def loadDocTemplates() {
        Script script = new GroovyShell().parse(new File("/root/zstack/header/src/main/java/org/zstack/header/zone/APICreateZoneMsgDoc_.groovy"))
        ExpandoMetaClass emc = new ExpandoMetaClass(script.getClass(), false, true)

        installClosure(emc, { ExpandoMetaClass e ->
            e.doc = { Closure cDoc ->
                cDoc.delegate = new Doc()
                cDoc.resolveStrategy = DELEGATE_FIRST

                cDoc()

                return  cDoc.delegate
            }
        })

        emc.initialize()

        script.setMetaClass(emc)
        docs.add(script.run() as Doc)

        System.out.println("xxxxxxxxxxxxxxxx ${JSONObjectUtil.toJsonString(docs[0])}")
    }

    void generate() {
        generateDocMetaTemplates()
    }

    def generateDocMetaTemplates() {
        Map<String, File> files = [:]

        File root = new File(rootPath)
        root.traverse { f ->
            files[f.name] = f
        }

        Set<Class> apiClasses = Platform.getReflections().getTypesAnnotatedWith(RestRequest.class)
        apiClasses.each {
            def filename = "${it.simpleName}.java"
            File f = files[filename]
            if (f == null) {
                throw new CloudRuntimeException("cannot find the source file of the class ${it.name}")
            }

            RestRequest at = it.getAnnotation(RestRequest.class)

            def self = it
            def headers = {
                if (self.isAnnotationPresent(SuppressCredentialCheck.class)) {
                    return ""
                }

                return """header (${RestConstants.HEADER_OAUTH}: 'the-session-uuid')"""
            }

            def params = {
                def apiFields = []
                FieldUtils.getAllFields(self).each {
                    if (it.isAnnotationPresent(APINoSee.class)) {
                        return
                    }

                    if (Modifier.isStatic(it.modifiers)) {
                        return
                    }

                    apiFields.add(it)
                }

                if (apiFields.isEmpty()) {
                    return ""
                }

                def cols = []
                for (Field af : apiFields) {
                    APIParam ap = af.getAnnotation(APIParam.class)

                    def values = {
                        if (ap == null || ap.validValues().length == 0) {
                            return "()"
                        }

                        def l = []
                        ap.validValues().each {
                            l.add("\"${it}\"")
                        }

                        return l.join(",")
                    }

                    cols.add("""\t\t\t\tcolumn {
\t\t\t\t\tname "${af.name}"
\t\t\t\t\tdesc ""
\t\t\t\t\toptional ${ap == null ? false : ap.required()}
\t\t\t\t\tvalues ${values}
}""")
                }
            }


            String DOC_TEMPLATE = """${it.package.name}

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "${at.method().toString()} ${at.path()}"

            ${headers()}

            clz ${it.simpleName}.class

            desc ""
            
            ${params()}
                    
                }
        }
    }
}"""
        }
    }

    @Override
    void generate(String scanPath) {
        rootPath = scanPath
        generate()
    }
}
