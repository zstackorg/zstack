package org.zstack.header.volume



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/volumes/data/from/data-volume-templates/{imageUuid}"

            header (OAuth: 'the-session-uuid')

            clz APICreateDataVolumeFromVolumeTemplateMsg.class

            desc ""
            
			params {

				column {
					name "imageUuid"
					enclosedIn "params"
					desc "镜像UUID"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
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
					name "primaryStorageUuid"
					enclosedIn "params"
					desc "主存储UUID"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
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
            clz APICreateDataVolumeFromVolumeTemplateEvent.class
        }
    }
}