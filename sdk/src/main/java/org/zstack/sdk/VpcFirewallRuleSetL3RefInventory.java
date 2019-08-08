package org.zstack.sdk;

import org.zstack.sdk.PacketsForwardType;

public class VpcFirewallRuleSetL3RefInventory  {

    public long id;
    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return this.id;
    }

    public java.lang.String ruleSetUuid;
    public void setRuleSetUuid(java.lang.String ruleSetUuid) {
        this.ruleSetUuid = ruleSetUuid;
    }
    public java.lang.String getRuleSetUuid() {
        return this.ruleSetUuid;
    }

    public java.lang.String l3Uuid;
    public void setL3Uuid(java.lang.String l3Uuid) {
        this.l3Uuid = l3Uuid;
    }
    public java.lang.String getL3Uuid() {
        return this.l3Uuid;
    }

    public PacketsForwardType packetsForwardType;
    public void setPacketsForwardType(PacketsForwardType packetsForwardType) {
        this.packetsForwardType = packetsForwardType;
    }
    public PacketsForwardType getPacketsForwardType() {
        return this.packetsForwardType;
    }

    public java.sql.Timestamp createDate;
    public void setCreateDate(java.sql.Timestamp createDate) {
        this.createDate = createDate;
    }
    public java.sql.Timestamp getCreateDate() {
        return this.createDate;
    }

    public java.sql.Timestamp lastOpDate;
    public void setLastOpDate(java.sql.Timestamp lastOpDate) {
        this.lastOpDate = lastOpDate;
    }
    public java.sql.Timestamp getLastOpDate() {
        return this.lastOpDate;
    }

}
