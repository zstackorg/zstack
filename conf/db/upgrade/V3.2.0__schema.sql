CREATE TABLE IF NOT EXISTS `TagPatternVO` (
    `uuid` VARCHAR(32) NOT NULL,
    `name` VARCHAR(128) NOT NULL,
    `value` VARCHAR(128) NOT NULL,
    `description` VARCHAR(2048) DEFAULT NULL,
    `color` VARCHAR(32) DEFAULT NULL,
    `type` VARCHAR(32) NOT NULL,
    `lastOpDate` timestamp ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp,
    PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `zstack`.`UserTagVO` ADD COLUMN `tagPatternUuid` varchar(32) DEFAULT NULL;

ALTER TABLE `zstack`.`UserTagVO` ADD CONSTRAINT fkUserTagVOTagPatternVO FOREIGN KEY (tagPatternUuid) REFERENCES TagPatternVO (uuid) ON DELETE CASCADE;

DELIMITER $$
CREATE PROCEDURE migrateUserTagVO()
    BEGIN
        DECLARE done INT DEFAULT FALSE;
        DECLARE patternUuid VARCHAR(32);
        DECLARE accountUuid VARCHAR(32);
        DECLARE patternTag VARCHAR(128);
        DECLARE color VARCHAR(32);
        DECLARE colorId SMALLINT UNSIGNED;
        DECLARE cur CURSOR FOR SELECT DISTINCT utag.tag, ref.accountUuid FROM zstack.UserTagVO utag, AccountResourceRefVO ref WHERE utag.resourceUuid = ref.resourceUuid;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
        OPEN cur;
        read_loop: LOOP
            FETCH cur INTO patternTag, accountUuid;
            IF done THEN
                LEAVE read_loop;
            END IF;

            SET patternUuid = (REPLACE(UUID(), '-', ''));
            SET colorId = FLOOR(RAND() * 8);
            SET color = (SELECT CASE colorId
                WHEN 0 THEN '#318857'
                WHEN 1 THEN '#918A12'
                WHEN 2 THEN '#DF9900'
                WHEN 3 THEN '#D14B52'
                WHEN 4 THEN '#8A65D4'
                WHEN 5 THEN '#7385A8'
                WHEN 6 THEN '#186EAE'
                WHEN 7 THEN '#2CA6E6'
            ELSE '#2CA6E6' END);

            INSERT zstack.ResourceVO(uuid, resourceName, resourceType, concreteResourceType)
            VALUES (patternUuid, patternTag, 'TagPatternVO', 'org.zstack.header.tag.TagPatternVO');

            INSERT TagPatternVO (uuid, name, value, color, type, createDate, lastOpDate)
            VALUES(patternUuid, patternTag, patternTag, color, 'simple', NOW(), NOW());

            INSERT zstack.AccountResourceRefVO(accountUuid, ownerAccountUuid, resourceUuid, resourceType, concreteResourceType, permission, isShared, createDate, lastOpDate)
            VALUES(accountUuid, accountUuid, patternUuid,  'TagPatternVO', 'org.zstack.header.tag.TagPatternVO', 2, 0, NOW(), NOW());

            UPDATE zstack.UserTagVO utag, AccountResourceRefVO ref SET utag.tagPatternUuid = patternUuid
            WHERE utag.tag = patternTag
            AND utag.resourceUuid = ref.resourceUuid
            AND ref.accountUuid = accountUuid;

        END LOOP;
        CLOSE cur;
        SELECT CURTIME();
    END $$
DELIMITER ;

call migrateUserTagVO();
DROP PROCEDURE IF EXISTS migrateUserTagVO;