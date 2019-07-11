CREATE TABLE  `zstack`.`GlobalConfigTemplateVO` (
    `uuid` varchar(32) NOT NULL UNIQUE,
    `name` varchar(255) NOT NULL,
    `type` varchar(32) NOT NULL,
    `description` varchar(1024) DEFAULT NULL,
    PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `zstack`.`TemplateConfigVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `category` varchar(64) NOT NULL,
    `templateUuid` varchar(32) NOT NULL,
    `defaultValue` text DEFAULT NULL,
    `value` text DEFAULT NULL,
    PRIMARY KEY  (`id`),
    CONSTRAINT `GlobalConfigTemplateVOTemplateConfigVO` FOREIGN KEY (`templateUuid`) REFERENCES `GlobalConfigTemplateVO` (`uuid`) ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;