package org.zstack.ldap

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/ldap/servers/actions"

            header (OAuth: 'the-session-uuid')

            clz APITestAddLdapServerConnectionMsg.class

            desc ""
            
			params {

				column {
					name "name"
					enclosedIn "testAddLdapServerConnection"
					desc "资源名称"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "description"
					enclosedIn "testAddLdapServerConnection"
					desc "资源的详细描述"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "url"
					enclosedIn "testAddLdapServerConnection"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "base"
					enclosedIn "testAddLdapServerConnection"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "username"
					enclosedIn "testAddLdapServerConnection"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "password"
					enclosedIn "testAddLdapServerConnection"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "encryption"
					enclosedIn "testAddLdapServerConnection"
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
            clz APITestAddLdapServerConnectionEvent.class
        }
    }
}