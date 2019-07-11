package org.zstack.test.integration.core.config

import org.zstack.core.config.TemplateConfigVO
import org.zstack.core.config.TemplateConfigVO_
import org.zstack.core.db.Q
import org.zstack.kvm.KVMGlobalConfig
import org.zstack.test.integration.kvm.KvmTest
import org.zstack.sdk.TemplateConfigInventory
import org.zstack.sdk.UpdateTemplateConfigAction
import org.zstack.sdk.GlobalConfigInventory
import org.zstack.sdk.GlobalConfigTemplateInventory
import org.zstack.testlib.EnvSpec
import org.zstack.testlib.SubCase
import org.zstack.testlib.SpringSpec

class TemplateConfigCase extends SubCase{
    EnvSpec env

    @Override
    void clean() {
        env.delete()
    }

    @Override
    void setup() {
        useSpring(KvmTest.springSpec)
    }

    @Override
    void environment() {
        env = env {
            zone {
                name = "zone"
            }
        }
    }

    @Override
    void test() {
        env.create {
            testResetTemplateConfig()
            testUpdateTemplateConfig()
            testQueryTemplateConfig()
            testQueryGlobalConfigTemplate()
            testApplyTemplateConfig()
            testBooleanValidator()
        }
    }

    void testResetTemplateConfig(){
        TemplateConfigVO tv = getTemplateConfigFromDb(TemplateConfigForTest.RESERVED_MEMORY_CAPACITY.category, TemplateConfigForTest.RESERVED_MEMORY_CAPACITY.name)
        updateTemplateConfig {
            templateUuid = tv.templateUuid
            category = tv.category
            name = tv.name
            value = "10G"
        }

        TemplateConfigInventory tci = queryTemplateConfig {
            conditions = ["templateUuid=${tv.templateUuid}", "category=${tv.category}", "name=${tv.name}"]
        }[0]
        assert tci.getValue() == "10G"


        resetTemplateConfig {
            templateUuid = tv.templateUuid
        }

        tci = queryTemplateConfig {
            conditions = ["templateUuid=${tv.templateUuid}", "category=${tv.category}", "name=${tv.name}"]
        }[0]
        assert tci.getValue() == "16G"
    }

    void testUpdateTemplateConfig(){
        TemplateConfigVO tv = getTemplateConfigFromDb(TemplateConfigForTest.RESERVED_MEMORY_CAPACITY.category, TemplateConfigForTest.RESERVED_MEMORY_CAPACITY.name)
        updateTemplateConfig {
            category = tv.category
            name = tv.name
            templateUuid = tv.templateUuid
            value = "20G"
        }
        TemplateConfigInventory tci = queryTemplateConfig {
            conditions = ["templateUuid=${tv.templateUuid}", "category=${tv.category}","name=${tv.name}" ]
        }[0]
        assert tci.getValue() == "20G"
    }

    void testQueryTemplateConfig(){
        TemplateConfigVO tv = getTemplateConfigFromDb(TemplateConfigForTest.PING_INTERVAL.category, TemplateConfigForTest.PING_INTERVAL.name)
        updateTemplateConfig {
            category = tv.category
            name = tv.name
            templateUuid = tv.templateUuid
            value = 5
        }
        TemplateConfigInventory tci = queryTemplateConfig {
            conditions = ["templateUuid=${tv.templateUuid}", "category=${tv.category}","name=${tv.name}"]
        }[0]
        assert tci.getValue().toInteger() == 5
    }

    void testQueryGlobalConfigTemplate(){
        GlobalConfigTemplateInventory gcti = queryGlobalConfigTemplate {}[0]
        assert gcti.getName() in ["ProductionRecommended", "VmPerformanceOptimised", "HaFastRecovery"]
        assert gcti.getType() == "system"
    }

    void testApplyTemplateConfig(){
        TemplateConfigVO tv = getTemplateConfigFromDb(TemplateConfigForTest.RESERVED_MEMORY_CAPACITY.category, TemplateConfigForTest.RESERVED_MEMORY_CAPACITY.name)
        updateTemplateConfig {
            category = tv.category
            name = tv.name
            templateUuid = tv.templateUuid
            value = "30G"
        }
        applyTemplateConfig {
            templateUuid = tv.templateUuid
        }
        GlobalConfigInventory gci = queryGlobalConfig {
            conditions = ["category=${KVMGlobalConfig.RESERVED_MEMORY_CAPACITY.category}", "name=${KVMGlobalConfig.RESERVED_MEMORY_CAPACITY.name}"]
        }[0]
        assert KVMGlobalConfig.RESERVED_MEMORY_CAPACITY.value == "30G"
    }

    void testBooleanValidator() {
        TemplateConfigVO tv = getTemplateConfigFromDb(TemplateConfigForTest.EMULATE_HYPERV.category, TemplateConfigForTest.EMULATE_HYPERV.name)

        UpdateTemplateConfigAction action = new UpdateTemplateConfigAction()
        action.category = tv.category
        action.name = tv.name
        action.templateUuid = tv.templateUuid
        action.value = "test"
        action.sessionId = adminSession()
        UpdateTemplateConfigAction.Result result = action.call()
        assert result.error != null
    }

    private TemplateConfigVO getTemplateConfigFromDb(String category, String name){
        return Q.New(TemplateConfigVO.class)
                .eq(TemplateConfigVO_.category, category)
                .eq(TemplateConfigVO_.name, name)
                .list()[0]
    }
}
