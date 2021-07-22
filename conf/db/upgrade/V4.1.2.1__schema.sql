CREATE TABLE IF NOT EXISTS `zstack`.`CdpBackupStorageVO` (
    `uuid` varchar(32) NOT NULL UNIQUE,
    `hostname` varchar(255) NOT NULL UNIQUE,
    `username` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `sshPort` int unsigned NOT NULL,
    PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`CdpPolicyEO` (
    `uuid` varchar(32) NOT NULL UNIQUE,
    `name` varchar(255) DEFAULT NULL,
    `description` varchar(2048) DEFAULT NULL,
    `retentionTimePerDay` int unsigned NOT NULL,
    `incrementalPointPerMinute` int unsigned NOT NULL,
    `recoveryPointPerSecond` int unsigned NOT NULL,
    `state` varchar(32) NOT NULL,
    `deleted` varchar(255) DEFAULT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`CdpPolicyRefVO` (
    `vmInstanceUuid` varchar(32) NOT NULL UNIQUE,
    `policyUuid` varchar(32) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`vmInstanceUuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP VIEW IF EXISTS `zstack`.`CdpPolicyVO`;
CREATE VIEW `zstack`.`CdpPolicyVO` AS SELECT uuid, name, description, retentionTimePerDay, incrementalPointPerMinute, recoveryPointPerSecond, state, lastOpDate, createDate FROM `zstack`.`CdpPolicyEO` WHERE deleted IS NULL;

CREATE TABLE IF NOT EXISTS `zstack`.`CdpTaskVO` (
    `vmInstanceUuid` varchar(32) NOT NULL UNIQUE,
    `backupStorageUuid` varchar(32) NOT NULL,
    `status` varchar(32) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`vmInstanceUuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`CdpTaskVolumeRefVO` (
    `volumeUuid` varchar(32) NOT NULL UNIQUE,
    `lastVolumePath` varchar(1024) DEFAULT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY  (`volumeUuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
