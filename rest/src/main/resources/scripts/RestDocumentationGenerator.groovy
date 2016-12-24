package scripts

import groovy.json.JsonBuilder
import org.zstack.rest.sdk.DocumentGenerator
import org.zstack.utils.Utils
import org.zstack.utils.gson.JSONObjectUtil
import org.zstack.utils.logging.CLogger

/**
 * Created by xing5 on 2016/12/21.
 */
class RestDocumentationGenerator implements DocumentGenerator {
    CLogger logger = Utils.getLogger(RestDocumentationGenerator.class)

    String rootPath

    List<Doc> docs = []

    def installClosuer(ExpandoMetaClass emc, Closure c) {
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

        def request(Closure c) {
            c.delegate = new Request()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _request = c.delegate as Request
        }
    }

    class RequestBody {
        def methodMissing(String name, args) {
            System.out.println("============ ${name} ${args}")
        }
    }

    class Request {
        private String _url
        private Map _header
        private String _desc
        private RequestParam _params
        private RequestBody _body

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
            c.delegate = new RequestBody()
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c()

            _body = c.delegate
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

    void generate() {
        //generateDocMetaFiles()

        Script script = new GroovyShell().parse(new File("/root/zstack/header/src/main/java/org/zstack/header/zone/APICreateZoneMsgDoc_.groovy"))
        ExpandoMetaClass emc = new ExpandoMetaClass(script.getClass(), false)

        installClosuer(emc, { ExpandoMetaClass e ->
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

        def json = new JsonBuilder(docs[0])
        System.out.println("xxxxxxxxxxxxxxxx ${JSONObjectUtil.toJsonString(docs[0])}")
    }

    def generateDocMetaFiles() {
        File root = new File(rootPath)
        //logger.debug("yyyyyyyyyyyyyyyyyyyyyyyyy ${root.absolutePath}")
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyy ${root.absolutePath}")
        root.traverse { f ->
            //logger.debug("xxxxxxxxxxxxxxxxxxxx ${f.absolutePath}")
            System.out.println("xxxxxxxxxxxxxxxxxxxx ${f.absolutePath}")
        }
    }

    @Override
    void generate(String scanPath) {
        rootPath = scanPath
        generate()
    }

    def methodMissing(String name, args) {
        System.out.println("------------ ${name} ${args}")
    }
}
