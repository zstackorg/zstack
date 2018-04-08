CREATE TABLE  `zstack`.`L3NetworkHostRouteVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `l3NetworkUuid` varchar(32) NOT NULL COMMENT 'l3 network uuid',
    `prefix` varchar(255) NOT NULL,
    `nexthop` varchar(255) NOT NULL,
    `lastOpDate` timestamp ON UPDATE CURRENT_TIMESTAMP COMMENT 'last operation date',
    `createDate` timestamp,
    PRIMARY KEY (`id`),
    CONSTRAINT `fkL3NetworkHostRouteVOL3NetworkEO` FOREIGN KEY (`l3NetworkUuid`) REFERENCES `zstack`.`L3NetworkEO` (`uuid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
