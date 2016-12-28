package org.zstack.header.identity

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "DELETE /v1/accounts/users/{userUuid}/policies/{policyUuid}"

            header (OAuth: 'the-session-uuid')

            clz APIDetachPolicyFromUserMsg.class

            desc ""
            
			params {

				column {
					name "policyUuid"
					enclosedIn ""
					desc "权限策略UUID"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "userUuid"
					enclosedIn ""
					desc "用户UUID"
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
            clz APIDetachPolicyFromUserEvent.class
        }
    }
}