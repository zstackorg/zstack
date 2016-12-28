package org.zstack.storage.fusionstor.primary



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/primary-storage/fusionstor/mons/{monUuid}/actions"

            header (OAuth: 'the-session-uuid')

            clz APIUpdateFusionstorPrimaryStorageMonMsg.class

            desc ""
            
			params {

				column {
					name "monUuid"
					enclosedIn "updateFusionstorPrimaryStorageMon"
					desc ""
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "hostname"
					enclosedIn "updateFusionstorPrimaryStorageMon"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sshUsername"
					enclosedIn "updateFusionstorPrimaryStorageMon"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sshPassword"
					enclosedIn "updateFusionstorPrimaryStorageMon"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sshPort"
					enclosedIn "updateFusionstorPrimaryStorageMon"
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "monPort"
					enclosedIn "updateFusionstorPrimaryStorageMon"
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
            clz APIUpdateMonToFusionstorPrimaryStorageEvent.class
        }
    }
}