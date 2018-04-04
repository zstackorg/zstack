CREATE TABLE `zstack`.`SharedBlockGroupVO` (
    `uuid` varchar(32) NOT NULL UNIQUE,
    `sharedBlockGroupType` varchar(128) NOT NULL,
    PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `zstack`.`SharedBlockVO` (
    `uuid` varchar(32) NOT NULL UNIQUE,
    `type` varchar(128) NOT NULL,
    `diskUuid` varchar(64) NOT NULL,
    `name` varchar(255) NOT NULL,
    `description` varchar(2048) DEFAULT NULL,
    `state` varchar(64) NOT NULL,
    `status` varchar(64) NOT NULL,
    `lastOpDate` timestamp ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp,
    PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `zstack`.`SharedBlockGroupPrimaryStorageHostRefVO` (
    `primaryStorageUuid` varchar(32) NOT NULL,
    `hostId` varchar(32) NOT NULL UNIQUE,
    CONSTRAINT `fkSharedBlockGroupPrimaryStorageHostRefVOPrimaryStorageEO` FOREIGN KEY (`primaryStorageUuid`) REFERENCES `zstack`.`PrimaryStorageEO` (`uuid`) ON DELETE CASCADE,
    PRIMARY KEY (`primaryStorageUuid`, `hostId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
