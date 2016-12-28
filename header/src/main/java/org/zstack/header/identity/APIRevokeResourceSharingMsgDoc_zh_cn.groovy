package org.zstack.header.identity



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/accounts/resources/actions"

            header (OAuth: 'the-session-uuid')

            clz APIRevokeResourceSharingMsg.class

            desc ""
            
			params {

				column {
					name "resourceUuids"
					enclosedIn "revokeResourceSharing"
					desc ""
					inUrl false
					type "List"
					optional false
					since "0.6"
					
				}
				column {
					name "toPublic"
					enclosedIn "revokeResourceSharing"
					desc ""
					inUrl false
					type "boolean"
					optional true
					since "0.6"
					
				}
				column {
					name "accountUuids"
					enclosedIn "revokeResourceSharing"
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "all"
					enclosedIn "revokeResourceSharing"
					desc ""
					inUrl false
					type "boolean"
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
            clz APIRevokeResourceSharingEvent.class
        }
    }
}