package org.zstack.network.service.portforwarding

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/port-forwarding"

            header (OAuth: 'the-session-uuid')

            clz APICreatePortForwardingRuleMsg.class

            desc ""
            
			params {

				column {
					name "vipUuid"
					enclosedIn ""
					desc "VIP UUID"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "vipPortStart"
					enclosedIn ""
					desc ""
					inUrl false
					type "Integer"
					optional false
					since "0.6"
					
				}
				column {
					name "vipPortEnd"
					enclosedIn ""
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "privatePortStart"
					enclosedIn ""
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "privatePortEnd"
					enclosedIn ""
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "protocolType"
					enclosedIn ""
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					values ("TCP","UDP")
				}
				column {
					name "vmNicUuid"
					enclosedIn ""
					desc "云主机网卡UUID"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "allowedCidr"
					enclosedIn ""
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "name"
					enclosedIn ""
					desc "资源名称"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "description"
					enclosedIn ""
					desc "资源的详细描述"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "resourceUuid"
					enclosedIn ""
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
            clz APICreatePortForwardingRuleEvent.class
        }
    }
}