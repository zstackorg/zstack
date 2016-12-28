package org.zstack.network.service.portforwarding

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/port-forwarding/{ruleUuid}/vm-instances/nics/{vmNicUuid}"

            header (OAuth: 'the-session-uuid')

            clz APIAttachPortForwardingRuleMsg.class

            desc ""
            
			params {

				column {
					name "ruleUuid"
					enclosedIn ""
					desc ""
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "vmNicUuid"
					enclosedIn ""
					desc "云主机网卡UUID"
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
            clz APIAttachPortForwardingRuleEvent.class
        }
    }
}