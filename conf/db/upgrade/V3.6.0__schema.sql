CREATE TABLE  `zstack`.`TemplateVO` (
    `uuid` varchar(32) NOT NULL UNIQUE,
    `name` varchar(255) NOT NULL,
    `type` varchar(32) NOT NULL,
    `description` varchar(1024) DEFAULT NULL,
    `value` text DEFAULT NULL,
    PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `zstack`.`TemplateConfigVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `type` varchar(32) NOT NULL,
    `description` varchar(1024) DEFAULT NULL,
    `value` text DEFAULT NULL,
    PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;