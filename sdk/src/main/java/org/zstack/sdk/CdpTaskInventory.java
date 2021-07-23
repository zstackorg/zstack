package org.zstack.sdk;

import org.zstack.sdk.CdpTaskStatus;

public class CdpTaskInventory  {

    public java.lang.String vmInstanceUuid;
    public void setVmInstanceUuid(java.lang.String vmInstanceUuid) {
        this.vmInstanceUuid = vmInstanceUuid;
    }
    public java.lang.String getVmInstanceUuid() {
        return this.vmInstanceUuid;
    }

    public java.lang.String backupStorageUuid;
    public void setBackupStorageUuid(java.lang.String backupStorageUuid) {
        this.backupStorageUuid = backupStorageUuid;
    }
    public java.lang.String getBackupStorageUuid() {
        return this.backupStorageUuid;
    }

    public CdpTaskStatus status;
    public void setStatus(CdpTaskStatus status) {
        this.status = status;
    }
    public CdpTaskStatus getStatus() {
        return this.status;
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
