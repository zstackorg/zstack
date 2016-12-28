package org.zstack.network.service.virtualrouter

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/instance-offerings/virtual-routers"

            header (OAuth: 'the-session-uuid')

            clz APICreateVirtualRouterOfferingMsg.class

            desc ""
            
			params {

				column {
					name "zoneUuid"
					enclosedIn "params"
					desc "区域UUID"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "managementNetworkUuid"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "imageUuid"
					enclosedIn "params"
					desc "镜像UUID"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "publicNetworkUuid"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
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
					name "cpuNum"
					enclosedIn "params"
					desc ""
					inUrl false
					type "int"
					optional false
					since "0.6"
					
				}
				column {
					name "cpuSpeed"
					enclosedIn "params"
					desc ""
					inUrl false
					type "int"
					optional false
					since "0.6"
					
				}
				column {
					name "memorySize"
					enclosedIn "params"
					desc ""
					inUrl false
					type "long"
					optional false
					since "0.6"
					
				}
				column {
					name "allocatorStrategy"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sortKey"
					enclosedIn "params"
					desc ""
					inUrl false
					type "int"
					optional true
					since "0.6"
					
				}
				column {
					name "type"
					enclosedIn "params"
					desc ""
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
            clz APICreateInstanceOfferingEvent.class
        }
    }
}