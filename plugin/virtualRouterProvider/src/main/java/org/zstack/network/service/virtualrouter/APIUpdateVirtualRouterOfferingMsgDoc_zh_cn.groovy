package org.zstack.network.service.virtualrouter

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/instance-offerings/virtual-routers/{uuid}/actions"

            header (OAuth: 'the-session-uuid')

            clz APIUpdateVirtualRouterOfferingMsg.class

            desc ""
            
			params {

				column {
					name "isDefault"
					enclosedIn "params"
					desc ""
					inUrl false
					type "Boolean"
					optional true
					since "0.6"
					
				}
				column {
					name "imageUuid"
					enclosedIn "params"
					desc "镜像UUID"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "uuid"
					enclosedIn "params"
					desc "资源的UUID，唯一标示该资源"
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
					optional true
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
            clz APIUpdateInstanceOfferingEvent.class
        }
    }
}