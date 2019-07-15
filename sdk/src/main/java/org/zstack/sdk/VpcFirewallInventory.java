package org.zstack.sdk;



public class VpcFirewallInventory  {

    public java.lang.String uuid;
    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }
    public java.lang.String getUuid() {
        return this.uuid;
    }

    public java.lang.String vpcUuid;
    public void setVpcUuid(java.lang.String vpcUuid) {
        this.vpcUuid = vpcUuid;
    }
    public java.lang.String getVpcUuid() {
        return this.vpcUuid;
    }

    public java.util.List interfaces;
    public void setInterfaces(java.util.List interfaces) {
        this.interfaces = interfaces;
    }
    public java.util.List getInterfaces() {
        return this.interfaces;
    }

    public java.util.List ruleSets;
    public void setRuleSets(java.util.List ruleSets) {
        this.ruleSets = ruleSets;
    }
    public java.util.List getRuleSets() {
        return this.ruleSets;
    }

    public java.util.List refs;
    public void setRefs(java.util.List refs) {
        this.refs = refs;
    }
    public java.util.List getRefs() {
        return this.refs;
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

    public java.lang.String description;
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    public java.lang.String getDescription() {
        return this.description;
    }

}
