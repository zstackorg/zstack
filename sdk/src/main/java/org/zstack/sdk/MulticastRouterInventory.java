package org.zstack.sdk;



public class MulticastRouterInventory  {

    public java.lang.String uuid;
    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }
    public java.lang.String getUuid() {
        return this.uuid;
    }

    public java.lang.String vpcRouterUuid;
    public void setVpcRouterUuid(java.lang.String vpcRouterUuid) {
        this.vpcRouterUuid = vpcRouterUuid;
    }
    public java.lang.String getVpcRouterUuid() {
        return this.vpcRouterUuid;
    }

    public java.lang.String description;
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    public java.lang.String getDescription() {
        return this.description;
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

    public java.util.List rpGroups;
    public void setRpGroups(java.util.List rpGroups) {
        this.rpGroups = rpGroups;
    }
    public java.util.List getRpGroups() {
        return this.rpGroups;
    }

}
