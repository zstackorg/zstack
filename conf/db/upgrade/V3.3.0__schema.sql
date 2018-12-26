CREATE TABLE IF NOT EXISTS `ElaborationVO` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `errorInfo` text NOT NULL,
  `md5sum` varchar(32) NOT NULL,
  `distance` double NOT NULL,
  `matched` boolean NOT NULL DEFAULT FALSE,
  `repeats` bigint(20) unsigned NOT NULL,
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX idxElaborationVOmd5sum ON ElaborationVO (md5sum);

CREATE TABLE `VmCdRomVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `name` VARCHAR(256) NOT NULL,
    `vmInstanceUuid` VARCHAR(32) NOT NULL,
    `deviceId` int(10) unsigned NOT NULL COMMENT 'device id',
    `isoUuid` VARCHAR(32) DEFAULT NULL,
    `isoInstallPath` VARCHAR(1024) DEFAULT NULL,
    `description` VARCHAR(2048) DEFAULT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`),
    UNIQUE KEY `vmInstanceCdRomDeviceId` (`vmInstanceUuid`,`deviceId`),
    KEY `fkVmCdRomVOVmInstanceEO` (`vmInstanceUuid`),
    CONSTRAINT `fkVmCdRomVOVmInstanceEO` FOREIGN KEY (`vmInstanceUuid`) REFERENCES `VmInstanceEO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkVmCdRomVOImageEO` FOREIGN KEY (`isoUuid`) REFERENCES `ImageEO` (`uuid`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
