CREATE TABLE `VpcFirewallVO` (
  `uuid` varchar(32) NOT NULL,
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `description` varchar(2048) DEFAULT NULL,
  `vpcUuid` varchar(32) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  KEY `fkFirewallVOVirtualRouterVMVO` (`vpcUuid`),
  CONSTRAINT `fkFirewallVOVirtualRouterVMVO` FOREIGN KEY (`vpcUuid`) REFERENCES `VirtualRouterVmVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `VpcFirewallRuleSetVO` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `vpcFirewallUuid` varchar(32) NOT NULL,
  `actionType` varchar(255) NOT NULL,
  `vpcDescription` varchar(255) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `enableDefaultLog` tinyint(1) NOT NULL DEFAULT '0',
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `isDefault` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  KEY `fkVpcFirewallRuleSetVOVpcFirewallVO` (`vpcFirewallUuid`),
  CONSTRAINT `fkVpcFirewallRuleSetVOVpcFirewallVO` FOREIGN KEY (`vpcFirewallUuid`) REFERENCES `VpcFirewallVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `VpcRouterInterfaceVO` (
  `uuid` varchar(32) NOT NULL,
  `vpcFirewallUuid` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `l3Uuid` varchar(32) NOT NULL,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `fkVpcRouterInterfaceVOL3NetworkEO` (`l3Uuid`),
  KEY `fkVpcRouterInterfaceVOVpcFirewallVO` (`vpcFirewallUuid`),
  CONSTRAINT `fkVpcRouterInterfaceVOVpcFirewallVO` FOREIGN KEY (`vpcFirewallUuid`) REFERENCES `VpcFirewallVO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkVpcRouterInterfaceVOL3NetworkEO` FOREIGN KEY (`l3Uuid`) REFERENCES `L3NetworkEO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `VpcFirewallRuleVO` (
  `uuid` varchar(32) NOT NULL,
  `vpcFirewallUuid` varchar(32) NOT NULL,
  `ruleSetUuid` varchar(32) NOT NULL,
  `ruleSetName` varchar(255) NOT NULL,
  `action` varchar(255) NOT NULL,
  `protocol` varchar(255) DEFAULT NULL,
  `sourcePort` varchar(255) DEFAULT NULL,
  `destPort` varchar(255) DEFAULT NULL,
  `sourceIp` varchar(255) DEFAULT NULL,
  `destIp` varchar(255) DEFAULT NULL,
  `ruleNumber` int(10) NOT NULL,
  `icmpTypeName` varchar(255) DEFAULT NULL,
  `allowStates` varchar(255) DEFAULT NULL,
  `tcpFlag` varchar(255) DEFAULT NULL,
  `enableLog` tinyint(1) NOT NULL DEFAULT '0',
  `state` varchar(32) NOT NULL DEFAULT '0',
  `isDefault` tinyint(1) NOT NULL DEFAULT '0',
  `description` varchar(2048) DEFAULT NULL,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  KEY `fkVpcFirewallRuleVOVpcFirewallVO` (`vpcFirewallUuid`),
  KEY `fkVpcFirewallRuleVOVpcFirewallRuleSetVO` (`ruleSetUuid`),
  CONSTRAINT `fkVpcFirewallRuleVOVpcFirewallRuleSetVO` FOREIGN KEY (`ruleSetUuid`) REFERENCES `VpcFirewallRuleSetVO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkVpcFirewallRuleVOVpcFirewallVO` FOREIGN KEY (`vpcFirewallUuid`) REFERENCES `VpcFirewallVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `VpcFirewallRuleSetInterfaceRefVO` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ruleSetUuid` varchar(32) NOT NULL,
  `interfaceUuid` varchar(32) NOT NULL,
  `vpcFirewallUuid` varchar(32) NOT NULL,
  `packetsForwardType` varchar(32) NOT NULL,
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `fkVpcFirewallRuleSetInterfaceRefVOVpcRouterInterfaceVO` (`interfaceUuid`),
  KEY `fkVpcFirewallRuleSetInterfaceRefVOVpcFirewallRuleSetVO` (`ruleSetUuid`),
  KEY `fkVpcFirewallRuleSetInterfaceRefVOVpcFirewallVO` (`vpcFirewallUuid`),
  CONSTRAINT `fkVpcFirewallRuleSetInterfaceRefVOVpcFirewallRuleSetVO` FOREIGN KEY (`ruleSetUuid`) REFERENCES `VpcFirewallRuleSetVO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkVpcFirewallRuleSetInterfaceRefVOVpcFirewallVO` FOREIGN KEY (`vpcFirewallUuid`) REFERENCES `VpcFirewallVO` (`uuid`) ON DELETE CASCADE,
  CONSTRAINT `fkVpcFirewallRuleSetInterfaceRefVOVpcRouterInterfaceVO` FOREIGN KEY (`interfaceUuid`) REFERENCES `VpcRouterInterfaceVO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;