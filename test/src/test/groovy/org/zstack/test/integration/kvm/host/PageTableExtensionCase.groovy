package org.zstack.test.integration.kvm.host

import org.springframework.http.HttpEntity
import org.zstack.compute.host.HostSystemTags
import org.zstack.header.host.HostVO
import org.zstack.kvm.KVMAgentCommands
import org.zstack.kvm.KVMConstant
import org.zstack.sdk.ClusterInventory
import org.zstack.sdk.KVMHostInventory
import org.zstack.test.integration.kvm.KvmTest
import org.zstack.testlib.EnvSpec
import org.zstack.testlib.SubCase
import org.zstack.utils.gson.JSONObjectUtil

/**
 * @author: kefeng.wang
 * @date: 2018-11-13
 * */
class PageTableExtensionCase extends SubCase {

    EnvSpec env

    @Override
    void setup() {
        useSpring(KvmTest.springSpec)
    }

    @Override
    void environment() {
        env = env {
            zone {
                name = "zone-1"

                cluster {
                    name = "cluster-1"
                    hypervisorType = "KVM"
                }
            }
        }
    }

    @Override
    void clean() {
        env.delete()
    }

    @Override
    void test() {
        env.create {
            testPageTableExtension()
        }
    }

    void testPageTableExtension() {
        KVMAgentCommands.PageTableExtensionCmd cmd = null
        env.simulator(KVMConstant.KVM_SET_PAGE_TABLE_EXTENSION) { HttpEntity<String> e, EnvSpec espec ->
            def rsp = new KVMAgentCommands.PageTableExtensionResponse()
            rsp.success = true
            return rsp
        }
        env.afterSimulator(KVMConstant.KVM_SET_PAGE_TABLE_EXTENSION) { rsp, HttpEntity<String> e ->
            cmd = JSONObjectUtil.toObject(e.body, KVMAgentCommands.PageTableExtensionCmd.class)
            return rsp
        }
        ClusterInventory cluster1 = env.inventoryByName("cluster-1") as ClusterInventory

        KVMHostInventory host1 = addKVMHost {
            name = "host-1"
            managementIp = "127.0.0.1"
            username = "root"
            password = "password"
            clusterUuid = cluster1.uuid
        }
        assert !HostSystemTags.PAGE_TABLE_EXTENSION_DISABLED.hasTag(host1.uuid, HostVO.class)

        KVMHostInventory host2 = addKVMHost {
            name = "host-2"
            managementIp = "127.0.0.2"
            username = "root"
            password = "password"
            clusterUuid = cluster1.uuid
            systemTags = ["pageTableExtensionDisabled"]
        }
        assert HostSystemTags.PAGE_TABLE_EXTENSION_DISABLED.hasTag(host2.uuid, HostVO.class)
    }
}
