package org.zstack.header.volume



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/volumes/{volumeUuid}/vm-instances/{vmInstanceUuid}"

            header (OAuth: 'the-session-uuid')

            clz APIAttachDataVolumeToVmMsg.class

            desc ""
            
			params {

				column {
					name "vmInstanceUuid"
					enclosedIn ""
					desc "云主机UUID"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "volumeUuid"
					enclosedIn ""
					desc "云盘UUID"
					inUrl true
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
            clz APIAttachDataVolumeToVmEvent.class
        }
    }
}