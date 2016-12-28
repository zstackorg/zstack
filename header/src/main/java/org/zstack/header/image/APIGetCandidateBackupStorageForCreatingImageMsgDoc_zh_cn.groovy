package org.zstack.header.image

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1null"

            header (OAuth: 'the-session-uuid')

            clz APIGetCandidateBackupStorageForCreatingImageMsg.class

            desc ""
            
			params {

				column {
					name "volumeUuid"
					enclosedIn ""
					desc "云盘UUID"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "volumeSnapshotUuid"
					enclosedIn ""
					desc "云盘快照UUID"
					inUrl false
					type "String"
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
            clz APIGetCandidateBackupStorageForCreatingImageReply.class
        }
    }
}