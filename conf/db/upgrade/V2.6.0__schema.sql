SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `TwoFactorAuthenticationSecretVO` (
    `uuid` VARCHAR(32) NOT NULL,
    `secret` VARCHAR(2048) NOT NULL,
    `resourceUuid` VARCHAR(32) NOT NULL,
    `resourceType` VARCHAR(256) NOT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;