CREATE TABLE IF NOT EXISTS `InstallPathRecycleVO` (
    `trashId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `resourceUuid` varchar(32) NOT NULL,
    `resourceType` varchar(32) NOT NULL,
    `storageUuid` varchar(32) NOT NULL,
    `storageType` varchar(32) NOT NULL,
    `installPath` varchar(1024) NOT NULL,
    `hypervisorType` varchar(32) DEFAULT NULL,
    `trashType` varchar(32) NOT NULL,
    `isFolder` boolean NOT NULL DEFAULT FALSE,
    `size` bigint unsigned NOT NULL,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`trashId`),
    UNIQUE KEY `trashId` (`trashId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`PriceTableVO` (
    `uuid` varchar(32) NOT NULL UNIQUE,
    `name` varchar(255) NOT NULL,
    `description` VARCHAR(256) DEFAULT NULL,
    `lastOpDate` timestamp ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp,
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO ResourceVO (`uuid`, `resourceName`, `resourceType`) VALUES ("12a087c058cc45d5bf80a605f17c0083", "global_default", 'PriceTableVO');
INSERT INTO PriceTableVO (`uuid`, `name`, `lastOpDate`, `createDate`) VALUES ("12a087c058cc45d5bf80a605f17c0083", "global_default", CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

ALTER TABLE PriceVO ADD COLUMN tableUuid VARCHAR(32) DEFAULT NULL;
ALTER TABLE PriceVO ADD CONSTRAINT fkPriceVOPriceTableVO FOREIGN KEY (tableUuid) REFERENCES PriceTableVO (uuid) ON DELETE CASCADE;
UPDATE PriceVO set tableUuid = "12a087c058cc45d5bf80a605f17c0083";
ALTER TABLE PriceVO modify column tableUuid VARCHAR(32) NOT NULL;

CREATE TABLE IF NOT EXISTS `zstack`.`AccountPriceTableRefVO` (
`tableUuid` varchar(32) NOT NULL,
`accountUuid` varchar(32) NOT NULL UNIQUE,
`lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
`createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
PRIMARY KEY (`tableUuid`,`accountUuid`),
CONSTRAINT `fkAccountPriceTableRefVOPriceTableVO` FOREIGN KEY (`tableUuid`) REFERENCES `PriceTableVO` (`uuid`) ON DELETE CASCADE,
CONSTRAINT `fkAccountPriceTableRefVOAccountVO` FOREIGN KEY (`accountUuid`) REFERENCES `AccountVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;