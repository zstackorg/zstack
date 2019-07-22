INSERT INTO AccountResourceRefVO (`accountUuid`, `ownerAccountUuid`, `resourceUuid`, `resourceType`, `permission`, `isShared`, `lastOpDate`, `createDate`, `concreteResourceType`) SELECT "36c27e8ff05c4780bf6d2fa65700f22e", "36c27e8ff05c4780bf6d2fa65700f22e", t.uuid, "VCenterVO", 2, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), "org.zstack.vmware.VCenterVO" FROM VCenterVO t where t.uuid NOT IN (SELECT resourceUuid FROM AccountResourceRefVO);

CREATE TABLE `zstack`.`SNSSmsEndpointVO`
(
    `uuid` varchar(32) NOT NULL UNIQUE,
    PRIMARY KEY (`uuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `zstack`.`SNSSmsReceiverVO`
(
    `uuid`         varchar(32) NOT NULL UNIQUE,
    `phoneNumber`  varchar(24) NOT NULL,
    `endpointUuid` varchar(32) NOT NULL,
    `type`         varchar(24) NOT NULL,
    PRIMARY KEY (`uuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `zstack`.`AliyunSmsSNSTextTemplateVO`
(
    `uuid`              varchar(32) NOT NULL UNIQUE,
    `sign`              varchar(12) NOT NULL,
    `alarmTemplateCode` varchar(13) NOT NULL,
    `eventTemplateCode` varchar(13) NOT NULL,
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