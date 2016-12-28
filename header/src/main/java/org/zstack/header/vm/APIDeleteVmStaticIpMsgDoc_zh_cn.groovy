package org.zstack.header.vm



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "DELETE /v1/vm-instances/{vmInstanceUuid}/static-ips"

            header (OAuth: 'the-session-uuid')

            clz APIDeleteVmStaticIpMsg.class

            desc ""
            
			params {

				column {
					name "vmInstanceUuid"
					enclosedIn "params"
					desc "云主机UUID"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "l3NetworkUuid"
					enclosedIn "params"
					desc "三层网络UUID"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "deleteMode"
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
            clz APIDeleteVmStaticIpEvent.class
        }
    }
}