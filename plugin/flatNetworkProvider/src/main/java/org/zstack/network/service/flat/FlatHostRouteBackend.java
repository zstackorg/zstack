package org.zstack.network.service.flat;

import org.springframework.beans.factory.annotation.Autowired;
import org.zstack.core.Platform;
import org.zstack.core.cloudbus.CloudBus;
import org.zstack.core.cloudbus.CloudBusCallBack;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.core.db.Q;
import org.zstack.core.db.SQL;
import org.zstack.core.db.SimpleQuery;
import org.zstack.header.core.Completion;
import org.zstack.header.managementnode.PrepareDbInitialValueExtensionPoint;
import org.zstack.header.message.MessageReply;
import org.zstack.header.network.l3.L3NetworkHostRouteVO;
import org.zstack.header.network.l3.L3NetworkHostRouteVO_;
import org.zstack.header.network.l3.UsedIpInventory;
import org.zstack.header.network.service.*;
import org.zstack.utils.Utils;
import org.zstack.utils.logging.CLogger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FlatHostRouteBackend implements NetworkServiceHostRouteBackend, DhcpServerExtensionPoint {
    private static final CLogger logger = Utils.getLogger(FlatHostRouteBackend.class);

    @Autowired
    private CloudBus bus;
    @Autowired
    private DatabaseFacade dbf;

    @Override
    public NetworkServiceProviderType getProviderType() {
        return FlatNetworkServiceConstant.FLAT_NETWORK_SERVICE_TYPE;
    }

    @Override
    public void addHostRoute(String l3Uuid, List<AddHostRouteMsg> routes, Completion completion) {
        /** hostroute will not install to vm immediately, but change the dnsmasq
         * once vm reboot or use restart dhcp process, host route will be installed to vm */
        L3NetworkUpdateDhcpMsg msg = new L3NetworkUpdateDhcpMsg();
        msg.setL3NetworkUuid(l3Uuid);
        bus.makeTargetServiceIdByResourceUuid(msg, FlatNetworkServiceConstant.SERVICE_ID, l3Uuid);
        bus.send(msg, new CloudBusCallBack(completion) {
            @Override
            public void run(MessageReply reply) {
                if (reply.isSuccess()) {
                    completion.success();
                } else {
                    completion.fail(reply.getError());
                }
            }
        });
    }

    @Override
    public void removeHostRoute(String l3Uuid, List<RemoveHostRouteMsg> routes, Completion completion) {
        /** hostroute will not install to vm immediately, but change the dnsmasq
         * once vm reboot or use restart dhcp process, host route will be installed to vm */
        L3NetworkUpdateDhcpMsg msg = new L3NetworkUpdateDhcpMsg();
        msg.setL3NetworkUuid(l3Uuid);
        bus.makeTargetServiceIdByResourceUuid(msg, FlatNetworkServiceConstant.SERVICE_ID, l3Uuid);
        bus.send(msg, new CloudBusCallBack(completion) {
            @Override
            public void run(MessageReply reply) {
                if (reply.isSuccess()) {
                    completion.success();
                } else {
                    completion.fail(reply.getError());
                }
            }
        });
    }

    @Override
    public void afterAllocateDhcpServerIP(String L3NetworkUuid, UsedIpInventory dhcpSererIp) {
        L3NetworkHostRouteVO vo = Q.New(L3NetworkHostRouteVO.class).eq(L3NetworkHostRouteVO_.l3NetworkUuid, L3NetworkUuid)
                .eq(L3NetworkHostRouteVO_.prefix, NetworkServiceConstants.METADATA_HOST_PREFIX).find();
        if (vo != null) {
            if (vo.getNexthop().equals(dhcpSererIp.getIp())) {
                return;
            }
            vo.setNexthop(dhcpSererIp.getIp());
            dbf.update(vo);
            return;
        }

        vo = new L3NetworkHostRouteVO();
        vo.setL3NetworkUuid(L3NetworkUuid);
        vo.setPrefix(NetworkServiceConstants.METADATA_HOST_PREFIX);
        vo.setNexthop(dhcpSererIp.getIp());
        dbf.persist(vo);
    }

    @Override
    public void afterRemoveDhcpServerIP(String L3NetworkUuid, UsedIpInventory dhcpSererIp) {
        SQL.New(L3NetworkHostRouteVO.class).eq(L3NetworkHostRouteVO_.l3NetworkUuid, L3NetworkUuid)
                .eq(L3NetworkHostRouteVO_.prefix, NetworkServiceConstants.METADATA_HOST_PREFIX).delete();
    }
}
