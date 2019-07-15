INSERT INTO AccountResourceRefVO (`accountUuid`, `ownerAccountUuid`, `resourceUuid`, `resourceType`, `permission`, `isShared`, `lastOpDate`, `createDate`, `concreteResourceType`) SELECT "36c27e8ff05c4780bf6d2fa65700f22e", "36c27e8ff05c4780bf6d2fa65700f22e", t.uuid, "VCenterVO", 2, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), "org.zstack.vmware.VCenterVO" FROM VCenterVO t where t.uuid NOT IN (SELECT resourceUuid FROM AccountResourceRefVO);

CREATE TABLE `zstack`.`RaidControllerVO` (
    `uuid` varchar(32) not null unique,
    `name` varchar(255) default null,
    `sasAddress` varchar(255) default null,
    `hostUuid` varchar(32) default null,
    `description` varchar(255) default null,
    `productName` varchar(255) default null,
    `lastOpDate` timestamp not null default '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp not null default '0000-00-00 00:00:00',
    CONSTRAINT fkRaidControllerVOHostVO FOREIGN KEY (hostUuid) REFERENCES HostEO (uuid) ON DELETE CASCADE,
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `zstack`.`RaidPhysicalDriveVO` (
    `uuid` varchar(32) not null unique,
    `raidControllerUuid` varchar(32) not null,
    `raidLevel` varchar(32) default null,
    `name` varchar(255) default null,
    `description` varchar(255) default null,
    `deviceModel` varchar(255) default null,
    `enclosureDeviceID` smallint not null,
    `slotNumber` smallint not null,
    `deviceId` smallint default null,
    `diskGroup` smallint default null,
    `wwn` varchar(255) default null,
    `serialNumber` varchar(255) default null,
    `size` bigint(20) not null,
    `driveState` varchar(255) default null,
    `locateStatus` varchar(32) default null,
    `driveType` varchar(255) default null,
    `mediaType` varchar(255) default null,
    `rotationRate` smallint default null,
    `lastOpDate` timestamp not null default '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp not null default '0000-00-00 00:00:00',
    CONSTRAINT fkRaidPhysicalDriveVORaidControllerVO FOREIGN KEY (raidControllerUuid) REFERENCES RaidControllerVO (uuid) ON DELETE CASCADE,
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
