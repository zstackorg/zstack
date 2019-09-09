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