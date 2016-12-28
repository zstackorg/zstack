package org.zstack.storage.primary.local



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/primary-storage/local-storage/capacities"

            header (OAuth: 'the-session-uuid')

            clz APIGetLocalStorageHostDiskCapacityMsg.class

            desc ""
            
			params {

				column {
					name "hostUuid"
					enclosedIn "params"
					desc "物理机UUID"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "primaryStorageUuid"
					enclosedIn "params"
					desc "主存储UUID"
					inUrl false
					type "String"
					optional false
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
            clz APIGetLocalStorageHostDiskCapacityReply.class
        }
    }
}