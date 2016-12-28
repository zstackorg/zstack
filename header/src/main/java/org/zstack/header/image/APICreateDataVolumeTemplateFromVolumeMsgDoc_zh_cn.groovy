package org.zstack.header.image



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/images/data-volume-templates/from/volumes/{volumeUuid}"

            header (OAuth: 'the-session-uuid')

            clz APICreateDataVolumeTemplateFromVolumeMsg.class

            desc ""
            
			params {

				column {
					name "name"
					enclosedIn "params"
					desc "资源名称"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "description"
					enclosedIn "params"
					desc "资源的详细描述"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "volumeUuid"
					enclosedIn "params"
					desc "云盘UUID"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "backupStorageUuids"
					enclosedIn "params"
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "resourceUuid"
					enclosedIn "params"
					desc ""
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
            clz APICreateDataVolumeTemplateFromVolumeEvent.class
        }
    }
}