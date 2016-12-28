package org.zstack.storage.fusionstor.backup



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/backup-storage/fusionstor/mons/{monUuid}/actions"

            header (OAuth: 'the-session-uuid')

            clz APIUpdateFusionstorBackupStorageMonMsg.class

            desc ""
            
			params {

				column {
					name "monUuid"
					enclosedIn "updateFusionstorBackupStorageMon"
					desc ""
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "hostname"
					enclosedIn "updateFusionstorBackupStorageMon"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sshUsername"
					enclosedIn "updateFusionstorBackupStorageMon"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sshPassword"
					enclosedIn "updateFusionstorBackupStorageMon"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sshPort"
					enclosedIn "updateFusionstorBackupStorageMon"
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "monPort"
					enclosedIn "updateFusionstorBackupStorageMon"
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "systemTags"
					enclosedIn ""
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "userTags"
					enclosedIn ""
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
			}
        }

        response {
            clz APIUpdateMonToFusionstorBackupStorageEvent.class
        }
    }
}