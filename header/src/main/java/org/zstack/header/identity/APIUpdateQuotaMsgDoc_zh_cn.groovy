package org.zstack.header.identity

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/accounts/quotas/actions"

            header (OAuth: 'the-session-uuid')

            clz APIUpdateQuotaMsg.class

            desc ""
            
			params {

				column {
					name "identityUuid"
					enclosedIn "updateQuota"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "name"
					enclosedIn "updateQuota"
					desc "资源名称"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "value"
					enclosedIn "updateQuota"
					desc ""
					inUrl false
					type "long"
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
            clz APIUpdateQuotaEvent.class
        }
    }
}