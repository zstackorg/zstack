ALTER TABLE VolumeSnapshotTreeEO ADD COLUMN status VARCHAR(32) NOT NULL DEFAULT "Completed";
DROP VIEW IF EXISTS `zstack`.`VolumeSnapshotTreeVO`;
CREATE VIEW `zstack`.`VolumeSnapshotTreeVO` AS SELECT uuid, volumeUuid, current, status, createDate, lastOpDate FROM `zstack`.`VolumeSnapshotTreeEO` WHERE deleted IS NULL;

ALTER TABLE `IAM2OrganizationVO` ADD COLUMN `rootOrganizationUuid` VARCHAR(32) NOT NULL;

DROP PROCEDURE IF EXISTS upgradeChild;
DROP PROCEDURE IF EXISTS upgradeOrganization;

DELIMITER $$
CREATE PROCEDURE upgradeChild(IN root_organization_uuid VARCHAR(32), IN current_organization_uuid VARCHAR(32))
    BEGIN
        DECLARE next_organization_uuid varchar(32);
        DECLARE done INT DEFAULT FALSE;
        DEClARE cur CURSOR FOR SELECT uuid FROM IAM2OrganizationVO WHERE parentUuid = current_organization_uuid;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

        open cur;
        upgrade_child_loop: LOOP
            FETCH cur INTO next_organization_uuid;
            SELECT next_organization_uuid;
            IF done THEN
                LEAVE upgrade_child_loop;
            END IF;

            UPDATE IAM2OrganizationVO SET rootOrganizationUuid = root_organization_uuid WHERE uuid = next_organization_uuid;
            CALL upgradeChild(root_organization_uuid, next_organization_uuid);
        END LOOP;
        close cur;
        SELECT CURTIME();
    END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE upgradeOrganization()
    upgrade_procedure: BEGIN
        DECLARE root_organization_uuid VARCHAR(32);
        DECLARE null_root_organization_uuid_exists INT DEFAULT 0;
        DECLARE done INT DEFAULT FALSE;
        DEClARE cur CURSOR FOR SELECT uuid FROM IAM2OrganizationVO WHERE parentUuid is NULL;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

        SELECT COUNT(uuid) INTO null_root_organization_uuid_exists FROM IAM2OrganizationVO where rootOrganizationUuid is NULL or rootOrganizationUuid = '';

        IF (null_root_organization_uuid_exists = 0) THEN
            LEAVE upgrade_procedure;
        END IF;

        OPEN cur;
        root_organization_loop: LOOP
            FETCH cur INTO root_organization_uuid;
            IF done THEN
                LEAVE root_organization_loop;
            END IF;

            UPDATE IAM2OrganizationVO SET rootOrganizationUuid = root_organization_uuid WHERE (rootOrganizationUuid is NULL or rootOrganizationUuid = '') and uuid = root_organization_uuid;
            CALL upgradeChild(root_organization_uuid, root_organization_uuid);
        END LOOP;
        CLOSE cur;
        SELECT CURTIME();
    END $$
DELIMITER ;

SET max_sp_recursion_depth=512;
call upgradeOrganization();
SET max_sp_recursion_depth=0;
DROP PROCEDURE IF EXISTS upgradeChild;
DROP PROCEDURE IF EXISTS upgradeOrganization;

ALTER TABLE `UsbDeviceVO` ADD COLUMN `attachType` varchar(32);

DELIMITER $$
CREATE PROCEDURE setDefaultUsbAttachType()
    BEGIN
        DECLARE done INT DEFAULT FALSE;
        DECLARE usbUuid VARCHAR(32);
        DEClARE cur CURSOR FOR SELECT uuid from UsbDeviceVO where vmInstanceUuid IS NOT NULL;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
        OPEN cur;
        read_loop: LOOP
            FETCH cur INTO usbUuid;
            IF done THEN
                LEAVE read_loop;
            END IF;

            UPDATE UsbDeviceVO set attachType = "PassThrough" WHERE uuid = usbUuid;
        END LOOP;
        CLOSE cur;
        SELECT CURTIME();
    END $$
DELIMITER ;

call setDefaultUsbAttachType();
DROP PROCEDURE IF EXISTS setDefaultUsbAttachType;

CREATE TABLE `SchedulerJobHistoryVO` (
    `id`                    BIGINT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,
    `triggerUuid`           VARCHAR(32),
    `schedulerJobUuid`      VARCHAR(32) NOT NULL,
    `schedulerJobGroupUuid` VARCHAR(32),
    `targetResourceUuid`    VARCHAR(32) NOT NULL,
    `startTime`             TIMESTAMP NOT NULL,
    `executeTime`           BIGINT,
    `requestDump`           TEXT,
    `resultDump`            TEXT,
    `success`               BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idxSchedulerJobHistoryVOTriggerUuid (triggerUuid),
    INDEX idxSchedulerJobHistoryVOSchedulerJobUuid (schedulerJobUuid),
    INDEX idxSchedulerJobHistoryVOSchedulerJobGroupUuid (schedulerJobGroupUuid),
    INDEX idxSchedulerJobHistoryVOTargetResourceUuid (targetResourceUuid),
    PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DELIMITER $$
CREATE PROCEDURE copyDataNetworkTag()
    BEGIN
        DECLARE done INT DEFAULT FALSE;
        DECLARE bsUuid VARCHAR(32);
        DECLARE cidrTag VARCHAR(64);
        DECLARE cidr VARCHAR(32);
        DEClARE cur CURSOR FOR SELECT resourceUuid, tag from SystemTagVO WHERE tag LIKE 'backupStorage::data::network::cidr::%';
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
        OPEN cur;
        read_loop: LOOP
            FETCH cur INTO bsUuid, cidrTag;
            IF done THEN
                LEAVE read_loop;
            END IF;

            SET cidr = substring(cidrTag, LENGTH('backupStorage::data::network::cidr::') + 1);

            INSERT zstack.SystemTagVO(uuid, resourceUuid,resourceType, tag, type, inherent, createDate, lastOpDate)
            VALUES (REPLACE(UUID(), '-', ''), bsUuid, 'ImageStoreBackupStorageVO', CONCAT('backup::network::cidr::', cidr), 'System', FALSE, NOW(), NOW());

        END LOOP;
        CLOSE cur;
        SELECT CURTIME();
    END $$
DELIMITER ;

call copyDataNetworkTag();
DROP PROCEDURE IF EXISTS copyDataNetworkTag;
CREATE TABLE `zstack`.`VipNetworkServicesRefVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `serviceType` VARCHAR(32) NOT NULL,
    `vipUuid` VARCHAR(32) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    CONSTRAINT fkVipNetworkServicesRefVOVipVO FOREIGN KEY (vipUuid) REFERENCES VipVO (uuid) ON DELETE CASCADE,
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO VipNetworkServicesRefVO (`uuid`, `serviceType`, `vipUuid`, `lastOpDate`, `createDate`) SELECT s.uuid, "LoadBalancer", s.vipUuid, s.createDate, s.createDate FROM LoadBalancerVO s;
INSERT INTO VipNetworkServicesRefVO (`uuid`, `serviceType`, `vipUuid`, `lastOpDate`, `createDate`) SELECT s.uuid, "PortForwarding", s.vipUuid, s.createDate, s.createDate  FROM PortForwardingRuleVO s;
INSERT INTO VipNetworkServicesRefVO (`uuid`, `serviceType`, `vipUuid`, `lastOpDate`, `createDate`) SELECT s.uuid, "IPsec", s.vipUuid, s.createDate, s.createDate  FROM IPsecConnectionVO s;
INSERT INTO VipNetworkServicesRefVO (`uuid`, `serviceType`, `vipUuid`, `lastOpDate`, `createDate`) SELECT s.uuid, "Eip", s.vipUuid, s.createDate, s.createDate  FROM EipVO s;
INSERT INTO VipNetworkServicesRefVO (`uuid`, `serviceType`, `vipUuid`, `lastOpDate`, `createDate`) SELECT s.uuid, "SNAT", s.uuid, current_timestamp(), current_timestamp()  FROM VirtualRouterVipVO s;

-- ------------------------------
--  for pci device virtualization
-- ------------------------------
ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `name` VARCHAR(255) NOT NULL;
ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `virtStatus` VARCHAR(32) DEFAULT NULL;
ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `parentUuid` VARCHAR(32) DEFAULT NULL;
ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `pciSpecUuid` VARCHAR(32) DEFAULT NULL;
ALTER TABLE `zstack`.`PciDeviceVO` ADD CONSTRAINT `fkPciDeviceVOPciDeviceVO` FOREIGN KEY (`parentUuid`) REFERENCES `PciDeviceVO` (`uuid`) ON DELETE CASCADE;
ALTER TABLE `zstack`.`PciDeviceVO` ADD CONSTRAINT `fkPciDeviceVOPciDeviceSpecVO` FOREIGN KEY (`pciSpecUuid`) REFERENCES `PciDeviceSpecVO` (`uuid`) ON DELETE SET NULL;
CREATE INDEX `idxPciDeviceVOtype` ON PciDeviceVO (`type`);
CREATE INDEX `idxPciDeviceVOhostUuid` ON PciDeviceVO (`hostUuid`);
CREATE INDEX `idxPciDeviceVOparentUuid` ON PciDeviceVO (`parentUuid`);
CREATE INDEX `idxPciDeviceVOpciSpecUuid` ON PciDeviceVO (`pciSpecUuid`);

ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `type` VARCHAR(32) NOT NULL;
ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `state` VARCHAR(32) NOT NULL;
ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `isVirtual` tinyint(1) NOT NULL DEFAULT 0;
ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `maxPartNum` INT DEFAULT NULL;
ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `ramSize` VARCHAR(32) DEFAULT NULL;

ALTER TABLE `zstack`.`PciDeviceOfferingVO` ADD COLUMN `ramSize` VARCHAR(32) DEFAULT NULL;

CREATE TABLE IF NOT EXISTS `zstack`.`MdevDeviceSpecVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `name` VARCHAR(32) NOT NULL,
    `description` VARCHAR(2048) DEFAULT NULL,
    `specification` TEXT DEFAULT NULL,
    `type` VARCHAR(32) NOT NULL,
    `state` VARCHAR(32) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`PciDeviceMdevSpecRefVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `pciDeviceUuid` VARCHAR(32) NOT NULL,
    `mdevSpecUuid` VARCHAR(32) NOT NULL,
    `effective` tinyint(1) unsigned DEFAULT 0,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`id`),
    CONSTRAINT `fkSpecRefPciDeviceUuid` FOREIGN KEY (`pciDeviceUuid`) REFERENCES `PciDeviceVO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkSpecRefMdevSpecUuid` FOREIGN KEY (`mdevSpecUuid`) REFERENCES `MdevDeviceSpecVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`MdevDeviceVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `name` VARCHAR(32) NOT NULL,
    `description` VARCHAR(2048) DEFAULT NULL,
    `hostUuid` VARCHAR(32) NOT NULL,
    `parentUuid` VARCHAR(32) NOT NULL,
    `vmInstanceUuid` VARCHAR(32) DEFAULT NULL,
    `mdevSpecUuid` VARCHAR(32) DEFAULT NULL,
    `type` VARCHAR(32) NOT NULL,
    `state` VARCHAR(32) NOT NULL,
    `status` VARCHAR(32) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`),
    INDEX `idxMdevDeviceVOtype` (`type`),
    INDEX `idxMdevDeviceVOhostUuid` (`hostUuid`),
    INDEX `idxMdevDeviceVOparentUuid` (`parentUuid`),
    INDEX `idxMdevDeviceVOmdevSpecUuid` (`mdevSpecUuid`),
    CONSTRAINT `fkMdevDeviceVOHostEO` FOREIGN KEY (`hostUuid`) REFERENCES `HostEO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkMdevDeviceVOPciDeviceVO` FOREIGN KEY (`parentUuid`) REFERENCES `PciDeviceVO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkMdevDeviceVOVmInstanceEO` FOREIGN KEY (`vmInstanceUuid`) REFERENCES `VmInstanceEO` (`uuid`) ON DELETE SET NULL,
    CONSTRAINT `fkMdevDeviceVOMdevSpecVO` FOREIGN KEY (`mdevSpecUuid`) REFERENCES `MdevDeviceSpecVO` (`uuid`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`VmInstancePciDeviceSpecRefVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `vmInstanceUuid` VARCHAR(32) NOT NULL,
    `pciSpecUuid` VARCHAR(32) NOT NULL,
    `pciDeviceNumber` int unsigned DEFAULT 1,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`id`),
    CONSTRAINT `fkVmPciSpecRefVmInstanceUuid` FOREIGN KEY (`vmInstanceUuid`) REFERENCES `VmInstanceEO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkVmPciSpecRefPciSpecUuid` FOREIGN KEY (`pciSpecUuid`) REFERENCES `PciDeviceSpecVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`VmInstancePciSpecDeviceRefVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `vmInstanceUuid` VARCHAR(32) NOT NULL,
    `pciSpecUuid` VARCHAR(32) NOT NULL,
    `pciDeviceUuid` VARCHAR(32) DEFAULT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`id`),
    CONSTRAINT `fkVmPciDeviceRefVmInstanceUuid` FOREIGN KEY (`vmInstanceUuid`) REFERENCES `VmInstanceEO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkVmPciDeviceRefPciSpecUuid` FOREIGN KEY (`pciSpecUuid`) REFERENCES `PciDeviceSpecVO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkVmPciDeviceRefPciDeviceUuid` FOREIGN KEY (`pciDeviceUuid`) REFERENCES `PciDeviceVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`VmInstanceMdevDeviceSpecRefVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `vmInstanceUuid` VARCHAR(32) NOT NULL,
    `mdevSpecUuid` VARCHAR(32) NOT NULL,
    `mdevDeviceNumber` int unsigned DEFAULT 1,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`id`),
    CONSTRAINT `fkVmMdevSpecRefVmInstanceUuid` FOREIGN KEY (`vmInstanceUuid`) REFERENCES `VmInstanceEO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkVmMdevSpecRefMdevSpecUuid` FOREIGN KEY (`mdevSpecUuid`) REFERENCES `MdevDeviceSpecVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`VmInstanceMdevSpecDeviceRefVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `vmInstanceUuid` VARCHAR(32) NOT NULL,
    `mdevSpecUuid` VARCHAR(32) NOT NULL,
    `mdevDeviceUuid` VARCHAR(32) DEFAULT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`id`),
    CONSTRAINT `fkVmMdevDeviceRefVmInstanceUuid` FOREIGN KEY (`vmInstanceUuid`) REFERENCES `VmInstanceEO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkVmMdevDeviceRefMdevSpecUuid` FOREIGN KEY (`mdevSpecUuid`) REFERENCES `MdevDeviceSpecVO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkVmMdevDeviceRefMdevDeviceUuid` FOREIGN KEY (`mdevDeviceUuid`) REFERENCES `MdevDeviceVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
