package scripts

import org.zstack.rest.sdk.DocumentGenerator
import org.zstack.utils.Utils
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

    class Doc {
        private String titleValue

        def title(String v) {
            titleValue = v
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

        System.out.println("xxxxxxxxxxxxxxxx ${docs[0].titleValue}")
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
}
