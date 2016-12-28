package org.zstack.ldap



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/ladp/servers"

            header (OAuth: 'the-session-uuid')

            clz APIAddLdapServerMsg.class

            desc ""
            
			params {

				column {
					name "name"
					enclosedIn "params"
					desc "资源名称"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "description"
					enclosedIn "params"
					desc "资源的详细描述"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "url"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "base"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "username"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "password"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "encryption"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					values ("None","TLS")
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
            clz APIAddLdapServerEvent.class
        }
    }
}