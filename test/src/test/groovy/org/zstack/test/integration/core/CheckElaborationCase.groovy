package org.zstack.test.integration.core

import org.zstack.core.Platform
import org.zstack.testlib.EnvSpec
import org.zstack.testlib.SubCase
import org.zstack.utils.path.PathUtil
/**
 * Created by mingjian.deng on 2018/12/26.*/
class CheckElaborationCase extends SubCase {
    EnvSpec env

    @Override
    void clean() {
        env.delete()
    }

    @Override
    void setup() {
        INCLUDE_CORE_SERVICES = false
    }

    @Override
    void environment() {
        env = new EnvSpec()
    }

    @Override
    void test() {
        env.create {
            testCheckElaboration()
        }
    }

    String getFilePath(String file) {
        File absPath = PathUtil.findFileOnClassPath(file)
        return absPath.toPath().toString()
    }

    void testCheckElaboration() {
        checkNonExisted()
        checkFolder("elaborations")
    }

    void checkNonExisted() {
        expect(AssertionError.class) {
            checkElaborationContent {
                elaborateFile = "/tmp-${Platform.uuid}"
            }
        }
    }

    void checkFolder(String folder) {
        String path = getFilePath(folder)
        checkElaborationContent {
            elaborateFile = path
        }
    }
}
