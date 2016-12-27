package org.zstack.header.vm

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /vm-instances/{uuid}/actions"

            header (OAuth: 'the-session-uuid')

            clz APIStartVmInstanceMsg.class

            desc ""
            
			params {

				column {
					name "uuid"
					desc ""
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "clusterUuid"
					desc ""
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "hostUuid"
					desc ""
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "systemTags"
					desc ""
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "userTags"
					desc ""
					type "List"
					optional true
					since "0.6"
					
				}
			}
        }

        response {
            clz APIStartVmInstanceEvent.class
        }
    }
}