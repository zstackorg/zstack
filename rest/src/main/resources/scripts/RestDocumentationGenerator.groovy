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

    void generate() {
        //generateDocMetaFiles()


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
