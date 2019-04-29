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
