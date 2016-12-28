package org.zstack.network.service.eip



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/eips/{eipUuid}/vm-instances/nics/{vmNicUuid"

            header (OAuth: 'the-session-uuid')

            clz APIAttachEipMsg.class

            desc ""
            
			params {

				column {
					name "eipUuid"
					enclosedIn ""
					desc "弹性IP UUID"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "vmNicUuid"
					enclosedIn ""
					desc "云主机网卡UUID"
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
            clz APIAttachEipEvent.class
        }
    }
}