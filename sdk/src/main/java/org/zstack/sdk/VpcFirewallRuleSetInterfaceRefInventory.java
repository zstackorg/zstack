package org.zstack.sdk;

import org.zstack.sdk.PacketsForwardType;

public class VpcFirewallRuleSetInterfaceRefInventory  {

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

    public java.lang.String interfaceUuid;
    public void setInterfaceUuid(java.lang.String interfaceUuid) {
        this.interfaceUuid = interfaceUuid;
    }
    public java.lang.String getInterfaceUuid() {
        return this.interfaceUuid;
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
