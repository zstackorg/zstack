package org.zstack.network.securitygroup



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/security-groups/{securityGroupUuid}/vm-instances/candidate-nics"

            header (OAuth: 'the-session-uuid')

            clz APIGetCandidateVmNicForSecurityGroupMsg.class

            desc ""
            
			params {

				column {
					name "securityGroupUuid"
					enclosedIn ""
					desc "安全组UUID"
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
            clz APIGetCandidateVmNicForSecurityGroupReply.class
        }
    }
}