package org.zstack.network.service.virtualrouter

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/vm-instances/appliances/virtual-routers/{uuid}/actions"

            header (OAuth: 'the-session-uuid')

            clz APIReconnectVirtualRouterMsg.class

            desc ""
            
			params {

				column {
					name "vmInstanceUuid"
					enclosedIn "reconnectVirtualRouter"
					desc "云主机UUID"
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
            clz APIReconnectVirtualRouterEvent.class
        }
    }
}