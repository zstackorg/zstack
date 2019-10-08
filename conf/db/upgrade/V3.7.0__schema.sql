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

CREATE TABLE IF NOT EXISTS `zstack`.`PortMirrorVO` (
  `uuid` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) DEFAULT "",
  `state` VARCHAR(128) DEFAULT "Enable",
  `mirrorNetworkUuid` VARCHAR(32) NOT NULL,
  `description` VARCHAR(1024) DEFAULT NULL,
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  CONSTRAINT `fkPortMirrorVOL3NetworkVO` FOREIGN KEY (`mirrorNetworkUuid`) REFERENCES `L3NetworkEO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`PortMirrorSessionVO` (
  `uuid` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `srcEndPoint` VARCHAR(32) NOT NULL,
  `dstEndPoint` VARCHAR(32) NOT NULL,
  `type` VARCHAR(32) NOT NULL,
  `status` VARCHAR(128) DEFAULT 'Created',
  `description` VARCHAR(1024) DEFAULT NULL,
  `portMirrorUuid` VARCHAR(32) NOT NULL,
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  CONSTRAINT `fkPortMirrorSessionVOPortMirrorVO` FOREIGN KEY (`portMirrorUuid`) REFERENCES `PortMirrorVO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkPortMirrorSessionVOSrcNIcVmNicVO` FOREIGN KEY (`srcEndPoint`) REFERENCES `VmNicVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`MirrorNetworkUsedIpVO` (
  `uuid` VARCHAR(32) NOT NULL,
  `hostUuid` VARCHAR(32) NOT NULL,
  `clusterUuid` VARCHAR(32) NOT NULL,
  `l3NetworkUuid` VARCHAR(32) NOT NULL,
  `description` VARCHAR(1024) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  CONSTRAINT `fkMirrorNetworkUsedIpVOL3NetworkEO` FOREIGN KEY (`l3NetworkUuid`) REFERENCES `L3NetworkEO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkMirrorNetworkUsedIpVOHostEO` FOREIGN KEY (`hostUuid`) REFERENCES `HostEO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkMirrorNetworkUsedIpVOClusterEO` FOREIGN KEY (`clusterUuid`) REFERENCES `ClusterEO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zstack`.`PortMirrorSessionMirrorNetworkRefVO` (
  `uuid` VARCHAR(32) NOT NULL,
  `sessionUuid` VARCHAR(32) NOT NULL,
  `srcTunnelUuid` VARCHAR(32) NOT NULL,
  `dstTunnelUuid` VARCHAR(32),
  `internalId` int unsigned NOT NULL,
  `internalSeq` int unsigned DEFAULT 0,
  `type` VARCHAR(32) DEFAULT 'GRE',
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  CONSTRAINT `fkMirrorRefVOPortMirrorSessionVO` FOREIGN KEY (`sessionUuid`) REFERENCES `PortMirrorSessionVO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkMirrorRefVOMirrorNetworkUsedIpVOSrc` FOREIGN KEY (`srcTunnelUuid`) REFERENCES `MirrorNetworkUsedIpVO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkMirrorRefVOMirrorNetworkUsedIpVODst` FOREIGN KEY (`dstTunnelUuid`) REFERENCES `MirrorNetworkUsedIpVO` (`uuid`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

