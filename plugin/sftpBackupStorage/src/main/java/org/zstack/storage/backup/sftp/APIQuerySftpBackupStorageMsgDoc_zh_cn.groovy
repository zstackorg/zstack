package org.zstack.storage.backup.sftp

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/backup-storage/sftp"

            header (OAuth: 'the-session-uuid')

            clz APIQuerySftpBackupStorageMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQuerySftpBackupStorageReply.class
        }
    }
}