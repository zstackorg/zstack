ALTER TABLE ImageBackupStorageRefVO ADD COLUMN exportMd5Sum VARCHAR(255) DEFAULT NULL;
ALTER TABLE ImageBackupStorageRefVO ADD COLUMN exportUrl VARCHAR(2048) DEFAULT NULL;
UPDATE ImageBackupStorageRefVO ibs, ImageVO i SET ibs.exportMd5Sum = i.exportMd5Sum, ibs.exportUrl = i.exportUrl WHERE ibs.imageUuid = i.uuid;
DROP VIEW IF EXISTS `zstack`.`ImageVO`;
CREATE VIEW `zstack`.`ImageVO` AS SELECT uuid, name, description, status, state, size, actualSize, md5Sum, platform, type, format, url, system, mediaType, createDate, lastOpDate, guestOsType FROM `zstack`.`ImageEO` WHERE deleted IS NULL;
ALTER TABLE ImageEO DROP exportMd5Sum, DROP exportUrl;