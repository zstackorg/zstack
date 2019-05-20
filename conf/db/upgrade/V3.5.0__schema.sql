-- ------------------------------
--  for pci device virtualization
-- ------------------------------
ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `name` VARCHAR(255) NOT NULL;
ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `virtStatus` VARCHAR(32) DEFAULT NULL;
ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `parentUuid` VARCHAR(32) DEFAULT NULL;
ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `pciSpecUuid` VARCHAR(32) DEFAULT NULL;
ALTER TABLE `zstack`.`PciDeviceVO` ADD CONSTRAINT `fkPciDeviceVOPciDeviceVO` FOREIGN KEY (`parentUuid`) REFERENCES `PciDeviceVO` (`uuid`) ON DELETE CASCADE;
ALTER TABLE `zstack`.`PciDeviceVO` ADD CONSTRAINT `fkPciDeviceVOPciDeviceSpecVO` FOREIGN KEY (`pciSpecUuid`) REFERENCES `PciDeviceSpecVO` (`uuid`) ON DELETE SET NULL;

ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `type` VARCHAR(32) NOT NULL;
ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `state` VARCHAR(32) NOT NULL;
ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `isVirtual` tinyint(1) NOT NULL DEFAULT 0;
ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `maxPartNum` INT DEFAULT NULL;
ALTER TABLE `zstack`.`PciDeviceSpecVO` ADD COLUMN `ramSize` VARCHAR(32) DEFAULT NULL;

ALTER TABLE `zstack`.`PciDeviceOfferingVO` ADD COLUMN `ramSize` VARCHAR(32) DEFAULT NULL;

CREATE TABLE `zstack`.`MdevDeviceSpecVO` (
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

CREATE TABLE `zstack`.`PciDeviceMdevSpecRefVO` (
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

CREATE TABLE `zstack`.`MdevDeviceVO` (
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
    CONSTRAINT `fkMdevDeviceVOHostEO` FOREIGN KEY (`hostUuid`) REFERENCES `HostEO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkMdevDeviceVOPciDeviceVO` FOREIGN KEY (`parentUuid`) REFERENCES `PciDeviceVO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkMdevDeviceVOVmInstanceEO` FOREIGN KEY (`vmInstanceUuid`) REFERENCES `VmInstanceEO` (`uuid`) ON DELETE SET NULL,
    CONSTRAINT `fkMdevDeviceVOMdevSpecVO` FOREIGN KEY (`mdevSpecUuid`) REFERENCES `MdevDeviceSpecVO` (`uuid`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
