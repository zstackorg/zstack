package org.zstack.test.integration.core

import org.zstack.core.Platform
import org.zstack.header.errorcode.ErrorCode
import org.zstack.testlib.EnvSpec
import org.zstack.testlib.SubCase

/**
 * Created by mingjian.deng on 2019/7/13.*/
class RegexElaborationCase extends SubCase {
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
        testElaboration1()
        testElaboration2()
        testElaboration3()
        testElaboration4()
        testElaboration5()
    }

    void testElaboration1() {
        def err = Platform.operr("Fn::Join must be array and contain 2 params, array[0] must be String, array[1] must be array!") as ErrorCode
        assert err.messages != null
        assert err.messages.message_cn == "Fn::Join后面的参数出错，该参数应包含2个参数，第一个为String,第二个为数组"
    }

    void testElaboration2() {
        def err = Platform.operr("Param [%s] has no value or default value found!", "ImageUuid") as ErrorCode
        assert err.messages != null
        assert err.messages.message_cn == "Parameter中的[ImageUuid]字段没有输入，并且也没有缺省值"
    }

    void testElaboration3() {
        def err = Platform.operr("unable to allocate hosts; due to pagination is enabled, there might be several allocation failures happened before; the error list is [{no host having cpu %s, memory %s found}]", 4, 8589934592) as ErrorCode
        assert err.messages != null
        assert err.messages.message_cn == "找不到合适的host来启动vm, 因为可以用于分配vm的host都没有足够的资源: cpu [4], 内存 [8589934592]"
    }

    void testElaboration4() {
        def err = Platform.operr("unable to allocate hosts; due to pagination is enabled, there might be several allocation failures happened before; the error list is [{no Connected hosts found in the [%s] candidate hosts having the hypervisor type [%s]}]", 4, "KVM") as ErrorCode
        assert err.messages != null
        assert err.messages.message_cn == "找不到合适的host来启动vm, 因为满足分配条件的4个hosts都不是KVM的虚拟化类型"
    }

    void testElaboration5() {
        def err = Platform.operr("unable to allocate hosts; due to pagination is enabled, there might be several allocation failures happened before; the error list is [{no Connected hosts found in the [%s] candidate hosts}]", 2) as ErrorCode
        assert err.messages != null
        assert err.messages.message_cn == "找不到合适的host来启动vm, 因为满足分配条件的2个hosts都不处于Connected状态"
    }
}
