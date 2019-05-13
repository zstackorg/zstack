CREATE TABLE `SchedulerJobHistoryVO` (
    `id`                    BIGINT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,
    `triggerUuid`           VARCHAR(32),
    `schedulerJobUuid`      VARCHAR(32) NOT NULL,
    `schedulerJobGroupUuid` VARCHAR(32),
    `targetResourceUuid`    VARCHAR(32) NOT NULL,
    `startTime`             TIMESTAMP NOT NULL,
    `executeTime`           BIGINT,
    `requestDump`           TEXT,
    `resultDump`            TEXT,
    `success`               BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idxSchedulerJobHistoryVOTriggerUuid (triggerUuid),
    INDEX idxSchedulerJobHistoryVOSchedulerJobUuid (schedulerJobUuid),
    INDEX idxSchedulerJobHistoryVOSchedulerJobGroupUuid (schedulerJobGroupUuid),
    INDEX idxSchedulerJobHistoryVOTargetResourceUuid (targetResourceUuid),
    PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DELIMITER $$
CREATE PROCEDURE copyDataNetworkTag()
    BEGIN
        DECLARE done INT DEFAULT FALSE;
        DECLARE bsUuid VARCHAR(32);
        DECLARE cidrTag VARCHAR(64);
        DECLARE cidr VARCHAR(32);
        DEClARE cur CURSOR FOR SELECT resourceUuid, tag from SystemTagVO WHERE tag LIKE 'backupStorage::data::network::cidr::%';
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
        OPEN cur;
        read_loop: LOOP
            FETCH cur INTO bsUuid, cidrTag;
            IF done THEN
                LEAVE read_loop;
            END IF;

            SET cidr = substring(cidrTag, LENGTH('backupStorage::data::network::cidr::') + 1);

            INSERT zstack.SystemTagVO(uuid, resourceUuid,resourceType, tag, type, inherent, createDate, lastOpDate)
            VALUES (REPLACE(UUID(), '-', ''), bsUuid, 'ImageStoreBackupStorageVO', CONCAT('backup::network::cidr::', cidr), 'System', FALSE, NOW(), NOW());

        END LOOP;
        CLOSE cur;
        SELECT CURTIME();
    END $$
DELIMITER ;

call copyDataNetworkTag();
DROP PROCEDURE IF EXISTS copyDataNetworkTag;
