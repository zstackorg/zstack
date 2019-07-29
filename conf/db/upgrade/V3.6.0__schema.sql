INSERT INTO AccountResourceRefVO (`accountUuid`, `ownerAccountUuid`, `resourceUuid`, `resourceType`, `permission`, `isShared`, `lastOpDate`, `createDate`, `concreteResourceType`) SELECT "36c27e8ff05c4780bf6d2fa65700f22e", "36c27e8ff05c4780bf6d2fa65700f22e", t.uuid, "VCenterVO", 2, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), "org.zstack.vmware.VCenterVO" FROM VCenterVO t where t.uuid NOT IN (SELECT resourceUuid FROM AccountResourceRefVO);
CREATE TABLE `FlowMeterVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE COMMENT 'flow meter uuid' ,
    `name` VARCHAR(32) DEFAULT "" ,
    `description` VARCHAR(128) DEFAULT "" ,
    `version` VARCHAR(16) DEFAULT 'V5',
    `type` VARCHAR(16) DEFAULT 'NetFlow',
    `sample` int unsigned DEFAULT 1,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `FlowCollectorVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE COMMENT 'flow collector uuid' ,
    `flowMeterUuid` VARCHAR(32) NOT NULL,
    `name` VARCHAR(32) DEFAULT "" ,
    `description` VARCHAR(128) DEFAULT "" ,
    `server` VARCHAR(64) NOT NULL,
    `port` VARCHAR(16) DEFAULT '2055',
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`),
    CONSTRAINT `fkFlowCollectorVOFlowMeterVO` FOREIGN KEY (`flowMeterUuid`) REFERENCES `FlowMeterVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `FlowRouterVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE COMMENT 'logic flow router uuid for vrouterHA' ,
    `systemID` int unsigned DEFAULT 0,
    `type` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT 'router ha type' ,
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `NetworkRouterFlowMeterRefVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `flowMeterUuid` VARCHAR(32) NOT NULL,
    `vFlowRouterUuid` VARCHAR(32) NOT NULL,
    `l3NetworkUuid` VARCHAR(32) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`),
    CONSTRAINT `fkNetworkRouterFlowMeterRefVOFlowMeterVO` FOREIGN KEY (`flowMeterUuid`) REFERENCES `FlowMeterVO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkNetworkRouterFlowMeterRefVOL3NetworkVO` FOREIGN KEY (`l3NetworkUuid`) REFERENCES `L3NetworkEO` (`uuid`) ON DELETE CASCADE,
    CONSTRAINT `fkNetworkRouterFlowMeterRefVOFlowRouterVmVO` FOREIGN KEY (`vFlowRouterUuid`) REFERENCES `FlowRouterVO` (`uuid`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
