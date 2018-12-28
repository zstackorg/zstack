-- ----------------------------
--  For unattended baremetal provisioning
-- ----------------------------
CREATE TABLE `PreconfigurationTemplateVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(2048) DEFAULT NULL,
    `distribution` VARCHAR(64) NOT NULL,
    `type` VARCHAR(32) NOT NULL,
    `content` MEDIUMTEXT NOT NULL,
    `md5sum` VARCHAR(255) NOT NULL,
    `isPredefined` TINYINT(1) UNSIGNED DEFAULT 0,
    `state` varchar(32) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TemplateCustomParamVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `templateUuid` VARCHAR(32) NOT NULL,
    `param` VARCHAR(255) NOT NULL,
    CONSTRAINT fkTemplateCustomParamVOPreconfigurationTemplateVO FOREIGN KEY (templateUuid) REFERENCES PreconfigurationTemplateVO (uuid) ON DELETE CASCADE,
    PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `CustomPreconfigurationVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `baremetalInstanceUuid` VARCHAR(32) NOT NULL,
    `param` VARCHAR(255) NOT NULL,
    `value` TEXT NOT NULL,
    CONSTRAINT fkCustomPreconfigurationVOBaremetalInstanceVO FOREIGN KEY (baremetalInstanceUuid) REFERENCES BaremetalInstanceVO (uuid) ON DELETE CASCADE,
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `BaremetalInstanceVO` ADD COLUMN `templateUuid` varchar(32) DEFAULT NULL;
ALTER TABLE `BaremetalInstanceVO` ADD CONSTRAINT `fkBaremetalInstanceVOPreconfigurationTemplateVO` FOREIGN KEY (`templateUuid`) REFERENCES `PreconfigurationTemplateVO` (`uuid`) ON DELETE SET NULL;
