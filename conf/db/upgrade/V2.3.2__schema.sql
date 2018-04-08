UPDATE `SchedulerJobVO` SET `jobData` = CONCAT('{"uuid":"', uuid, '",'
                    ,'"targetResourceUuid":"', targetResourceUuid, '",'
                    ,'"name":"', name,'",'
                    ,'"createDate":"', Json_getKeyValue(jobData, 'createDate'), '",'
                    ,'"accountUuid":"', Json_getKeyValue(jobData, 'accountUuid'), '"}');

