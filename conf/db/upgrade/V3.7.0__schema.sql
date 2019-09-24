CREATE TABLE IF NOT EXISTS `zstack`.`SNSSmsEndpointVO`
(
    `uuid` varchar(32) NOT NULL UNIQUE,
    PRIMARY KEY (`uuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`SNSSmsReceiverVO`
(
    `uuid`         varchar(32) NOT NULL UNIQUE,
    `phoneNumber`  varchar(24) NOT NULL,
    `endpointUuid` varchar(32) NOT NULL,
    `type`         varchar(24) NOT NULL,
    PRIMARY KEY (`uuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`AliyunSmsSNSTextTemplateVO`
(
    `uuid`              varchar(32) NOT NULL UNIQUE,
    `sign`              varchar(24) NOT NULL,
    `alarmTemplateCode` varchar(24) NOT NULL,
    `eventTemplateCode` varchar(24) NOT NULL,
    `eventTemplate`     text,
    PRIMARY KEY (`uuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# Foreign keys for table SNSSmsEndpointVO

ALTER TABLE SNSSmsEndpointVO
    ADD CONSTRAINT fkSNSSmsEndpointVOSNSApplicationEndpointVO FOREIGN KEY (uuid) REFERENCES SNSApplicationEndpointVO (uuid) ON UPDATE RESTRICT ON DELETE CASCADE;

# Foreign keys for table SNSSmsReceiverVO

ALTER TABLE SNSSmsReceiverVO
    ADD CONSTRAINT fkSNSSmsReceiverVOSNSSmsEndpointVO FOREIGN KEY (endpointUuid) REFERENCES SNSSmsEndpointVO (uuid);

# Foreign keys for table AliyunSmsSNSTextTemplateVO

ALTER TABLE AliyunSmsSNSTextTemplateVO
    ADD CONSTRAINT fkAliyunSmsSNSTextTemplateVOSNSTextTemplateVO FOREIGN KEY (uuid) REFERENCES SNSTextTemplateVO (uuid) ON UPDATE RESTRICT ON DELETE CASCADE;
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