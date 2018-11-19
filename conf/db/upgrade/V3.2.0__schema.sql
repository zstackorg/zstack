CREATE TABLE  `zstack`.`PubIpVmNicBandwidthUsageVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `vmNicUuid` varchar(32) NOT NULL,
    `vmInstanceUuid` varchar(32),
    `bandwidthOut` bigint unsigned NOT NULL,
    `bandwidthIn` bigint unsigned NOT NULL,
    `vmNicIp` varchar(128) DEFAULT NULL,
    `vmNicStatus` varchar(64) NOT NULL,
    `l3NetworkUuid` varchar(64) NOT NULL,
    `accountUuid` varchar(32) NOT NULL,
    `dateInLong` bigint unsigned NOT NULL,
    `inventory` text DEFAULT NULL,
    `lastOpDate` timestamp ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp,
    PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `zstack`.`PubIpVipBandwidthUsageVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `vipUuid` varchar(32) NOT NULL,
    `vipName` varchar(255) DEFAULT NULL,
    `vipIp` varchar(128) NOT NULL,
    `bandwidthOut` bigint unsigned NOT NULL,
    `bandwidthIn` bigint unsigned NOT NULL,
    `l3NetworkUuid` varchar(64) NOT NULL,
    `vipStatus` varchar(64) NOT NULL,
    `accountUuid` varchar(32) NOT NULL,
    `dateInLong` bigint unsigned NOT NULL,
    `inventory` text DEFAULT NULL,
    `lastOpDate` timestamp ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp,
    PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;