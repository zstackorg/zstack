CREATE TABLE `FlowMeterVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE COMMENT 'flow meter uuid' ,
    `name` VARCHAR(32) DEFAULT NULL ,
    `description` VARCHAR(128) DEFAULT NULL ,
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
    `name` VARCHAR(32) DEFAULT NULL ,
    `description` VARCHAR(128) DEFAULT NULL ,
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
    CONSTRAINT `fkNetworkRouterFlowMeterRefVOFlowRouterVmVO` FOREIGN KEY (`vFlowRouterUuid`) REFERENCES `FlowRouterVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;