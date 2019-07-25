package org.zstack.header.storage.primary;

public interface PrimaryStorageLicenseInfoFactory {
    PrimaryStorageLicenseInfo getPrimaryStorageLicenseInfo(String primaryStorageUuid);

    PrimaryStorageVendor getPrimaryStorageVendor();
}
