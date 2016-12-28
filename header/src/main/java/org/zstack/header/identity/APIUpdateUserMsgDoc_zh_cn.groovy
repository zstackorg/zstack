package org.zstack.header.identity



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/accounts/users/actions"

            header (OAuth: 'the-session-uuid')

            clz APIUpdateUserMsg.class

            desc ""
            
			params {

				column {
					name "uuid"
					enclosedIn "updateUser"
					desc "资源的UUID，唯一标示该资源"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "password"
					enclosedIn "updateUser"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "name"
					enclosedIn "updateUser"
					desc "资源名称"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "description"
					enclosedIn "updateUser"
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
            clz APIUpdateUserEvent.class
        }
    }
}