package org.zstack.sdk;

public class VxlanNetworkVO extends L2NetworkVO {

    public java.lang.Integer vni;
    public void setVni(java.lang.Integer vni) {
        this.vni = vni;
    }
    public java.lang.Integer getVni() {
        return this.vni;
    }

    public java.lang.String poolUuid;
    public void setPoolUuid(java.lang.String poolUuid) {
        this.poolUuid = poolUuid;
    }
    public java.lang.String getPoolUuid() {
        return this.poolUuid;
    }

}
