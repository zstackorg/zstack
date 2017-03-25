package org.zstack.hybrid;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zstack.core.cloudbus.CloudBus;
import org.zstack.core.componentloader.ComponentLoader;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.header.datacenter.APIAddDataCenterFromRemoteEvent;
import org.zstack.header.datacenter.APIAddDataCenterFromRemoteMsg;
import org.zstack.header.datacenter.DataCenterVO;
import org.zstack.header.identity.SessionInventory;
import org.zstack.header.identityzone.*;
import org.zstack.hybrid.core.HybridType;
import org.zstack.test.Api;
import org.zstack.test.ApiSender;
import org.zstack.test.ApiSenderException;
import org.zstack.test.DBUtil;
import org.zstack.test.deployer.Deployer;
import org.zstack.utils.Utils;
import org.zstack.utils.logging.CLogger;

/**
 * Created by mingjian.deng on 17/2/8.
 */
public class TestCreateIdentityZone {
    Deployer deployer;
    Api api;
    ComponentLoader loader;
    CloudBus bus;
    DatabaseFacade dbf;
    private SessionInventory adminSession;

    protected static final CLogger logger = Utils.getLogger(TestCreateIdentityZone.class);

    @Before
    public void setUp() throws Exception {
        DBUtil.reDeployDB();
        deployer = new Deployer("deployerXml/vm/TestCreateVm.xml");
        deployer.addSpringConfig("mevocoRelated.xml");
        deployer.build();
        api = deployer.getApi();
        loader = deployer.getComponentLoader();
        bus = loader.getComponent(CloudBus.class);
        dbf = loader.getComponent(DatabaseFacade.class);
        adminSession = api.loginAsAdmin();
    }

    @Test
    public void test() throws ApiSenderException {
        APIAddDataCenterFromRemoteMsg msg = new APIAddDataCenterFromRemoteMsg();
        msg.setRegionId("cn-hangzhou");
        msg.setType(HybridType.aliyun.toString());
        APIAddDataCenterFromRemoteEvent event = createDataCenter(msg);
        Assert.assertTrue(event.isSuccess());
        Assert.assertNotNull(dbf.findByUuid(event.getInventory().getUuid(), DataCenterVO.class));


        APIAddIdentityZoneFromRemoteMsg msg1 = new APIAddIdentityZoneFromRemoteMsg();
        msg1.setType(HybridType.aliyun.toString());
        msg1.setDataCenterUuid(event.getInventory().getUuid());
        APIAddIdentityZoneFromRemoteEvent event1 = createIdentityZone(msg1);
        Assert.assertTrue(event1.isSuccess());
        Assert.assertNotNull(dbf.findByUuid(event1.getInventory().getUuid(), IdentityZoneVO.class));

        APIDeleteIdentityZoneInLocalMsg msg2 = new APIDeleteIdentityZoneInLocalMsg();
        msg2.setUuid(event1.getInventory().getUuid());
        APIDeleteIdentityZoneInLocalEvent event2 = deleteIdentityZone(msg2);
        Assert.assertTrue(event2.isSuccess());
        Assert.assertNull(dbf.findByUuid(event1.getInventory().getUuid(), IdentityZoneVO.class));


        msg1.setZoneId("cn-hangzhou-e");
        event1 = createIdentityZone(msg1);
        Assert.assertTrue(event1.isSuccess());
        Assert.assertNotNull(dbf.findByUuid(event1.getInventory().getUuid(), IdentityZoneVO.class));
    }

    private APIAddDataCenterFromRemoteEvent createDataCenter(APIAddDataCenterFromRemoteMsg msg) throws ApiSenderException {
        msg.setSession(adminSession);
        ApiSender sender = new ApiSender();
        sender.setTimeout(10);
        APIAddDataCenterFromRemoteEvent event = sender.send(msg, APIAddDataCenterFromRemoteEvent.class);
        return event;
    }

    private APIAddIdentityZoneFromRemoteEvent createIdentityZone(APIAddIdentityZoneFromRemoteMsg msg) throws ApiSenderException {
        msg.setSession(adminSession);
        ApiSender sender = new ApiSender();
        sender.setTimeout(10);
        APIAddIdentityZoneFromRemoteEvent event = sender.send(msg, APIAddIdentityZoneFromRemoteEvent.class);
        return event;
    }

    private APIDeleteIdentityZoneInLocalEvent deleteIdentityZone(APIDeleteIdentityZoneInLocalMsg msg) throws ApiSenderException {
        msg.setSession(adminSession);
        ApiSender sender = new ApiSender();
        sender.setTimeout(10);
        APIDeleteIdentityZoneInLocalEvent event = sender.send(msg, APIDeleteIdentityZoneInLocalEvent.class);
        return event;
    }
}
