ALTER TABLE `UsbDeviceVO` ADD COLUMN `attachType` varchar(32);

DELIMITER $$
CREATE PROCEDURE setDefaultUsbAttachType()
    BEGIN
        DECLARE done INT DEFAULT FALSE;
        DECLARE usbUuid VARCHAR(32);
        DEClARE cur CURSOR FOR SELECT uuid from UsbDeviceVO where vmInstanceUuid IS NOT NULL;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
        OPEN cur;
        read_loop: LOOP
            FETCH cur INTO usbUuid;
            IF done THEN
                LEAVE read_loop;
            END IF;

            UPDATE UsbDeviceVO set attachType = "PassThrough" WHERE uuid = usbUuid;
        END LOOP;
        CLOSE cur;
        SELECT CURTIME();
    END $$
DELIMITER ;

call setDefaultUsbAttachType();
DROP PROCEDURE IF EXISTS setDefaultUsbAttachType;