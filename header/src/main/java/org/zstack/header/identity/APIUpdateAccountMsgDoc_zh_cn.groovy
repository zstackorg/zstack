package org.zstack.header.identity



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/accounts/{uuid}"

            header (OAuth: 'the-session-uuid')

            clz APIUpdateAccountMsg.class

            desc ""
            
			params {

				column {
					name "uuid"
					enclosedIn "updateAccount"
					desc "资源的UUID，唯一标示该资源"
					inUrl true
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "password"
					enclosedIn "updateAccount"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "name"
					enclosedIn "updateAccount"
					desc "资源名称"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "description"
					enclosedIn "updateAccount"
					desc "资源的详细描述"
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
            clz APIUpdateAccountEvent.class
        }
    }
}