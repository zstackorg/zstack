package org.zstack.kvm;

import org.springframework.beans.factory.annotation.Autowired;
import org.zstack.compute.host.HostSystemTags;
import org.zstack.core.cloudbus.CloudBus;
import org.zstack.core.cloudbus.CloudBusCallBack;
import org.zstack.core.timeout.ApiTimeoutManager;
import org.zstack.header.core.workflow.Flow;
import org.zstack.header.core.workflow.FlowTrigger;
import org.zstack.header.core.workflow.NoRollbackFlow;
import org.zstack.header.host.HostConstant;
import org.zstack.header.host.HostVO;
import org.zstack.header.message.MessageReply;

import java.util.Map;

public class KVMHostPageTableExtension implements KVMHostConnectExtensionPoint {
    @Autowired
    private CloudBus bus;

    @Autowired
    private ApiTimeoutManager timeoutMgr;

    @Override
    public Flow createKvmHostConnectingFlow(final KVMHostConnectedContext context) {
        return new NoRollbackFlow() {
            String __name__ = "set-page-table-extension";

            @Override
            public void run(FlowTrigger trigger, Map data) {
                String uuid = context.getInventory().getUuid();
                KVMAgentCommands.PageTableExtensionCmd cmd = new KVMAgentCommands.PageTableExtensionCmd();
                cmd.setDisabled(HostSystemTags.PAGE_TABLE_EXTENSION_DISABLED.hasTag(uuid, HostVO.class));

                KVMHostAsyncHttpCallMsg msg = new KVMHostAsyncHttpCallMsg();
                msg.setHostUuid(uuid);
                msg.setCommand(cmd);
                msg.setCommandTimeout(timeoutMgr.getTimeout(cmd.getClass(), "5m"));
                msg.setNoStatusCheck(true);
                msg.setPath(KVMConstant.KVM_SET_PAGE_TABLE_EXTENSION);
                bus.makeTargetServiceIdByResourceUuid(msg, HostConstant.SERVICE_ID, context.getInventory().getUuid());
                bus.send(msg, new CloudBusCallBack(trigger) {
                    @Override
                    public void run(MessageReply reply) {
                        if (reply.isSuccess()) {
                            trigger.next();
                        } else {
                            trigger.fail(reply.getError());
                        }
                    }
                });
            }
        };
    }
}
