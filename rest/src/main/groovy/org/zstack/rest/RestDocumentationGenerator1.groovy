package org.zstack.rest

import org.zstack.header.exception.CloudRuntimeException
import org.zstack.utils.Utils
import org.zstack.utils.logging.CLogger

/**
 * Created by xing5 on 2016/12/21.
 */
class RestDocumentationGenerator1 {
    CLogger logger = Utils.getLogger(RestDocumentationGenerator1.class)

    String rootPath

    RestDocumentationGenerator1(String rootPath) {
        this.rootPath = rootPath
    }

    void generate() {
        generateDocMetaFiles()
    }

    def generateDocMetaFiles() {
        File root = new File(rootPath)
        logger.debug("yyyyyyyyyyyyyyyyyyyyyyyyy ${root.absolutePath}")
        root.traverse { f ->
            logger.debug("xxxxxxxxxxxxxxxxxxxx ${f.absolutePath}")
        }
    }
}
