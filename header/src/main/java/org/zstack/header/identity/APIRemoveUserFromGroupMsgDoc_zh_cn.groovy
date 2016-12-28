package org.zstack.header.identity

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "DELETE /v1/accounts/groups/{groupUuid}/users/{userUuid}"

            header (OAuth: 'the-session-uuid')

            clz APIRemoveUserFromGroupMsg.class

            desc ""
            
			params {

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
					name "groupUuid"
					enclosedIn ""
					desc ""
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
            clz APIRemoveUserFromGroupEvent.class
        }
    }
}