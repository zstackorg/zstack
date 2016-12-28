package org.zstack.header.volume



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/volumes/{volumeUuid}/candidate-vm-instances"

            header (OAuth: 'the-session-uuid')

            clz APIGetDataVolumeAttachableVmMsg.class

            desc ""
            
			params {

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
            clz APIGetDataVolumeAttachableVmReply.class
        }
    }
}