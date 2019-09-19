--  FOR GUEST TOOLS
CREATE TABLE IF NOT EXISTS `zstack`.`GuestToolsVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `name` varchar(255) DEFAULT "",
    `description` varchar(2048) DEFAULT NULL,
    `managementNodeUuid` VARCHAR(32) NOT NULL,
    `architecture` VARCHAR(32) NOT NULL,
    `hypervisorType` VARCHAR(32) NOT NULL,
    `version` VARCHAR(32) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;