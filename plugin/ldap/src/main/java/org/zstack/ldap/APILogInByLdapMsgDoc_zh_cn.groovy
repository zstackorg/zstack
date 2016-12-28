package org.zstack.ldap

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/ldap/uids/{uid}/actions"

            

            clz APILogInByLdapMsg.class

            desc ""
            
			params {

				column {
					name "uid"
					enclosedIn "logInByLdap"
					desc ""
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "password"
					enclosedIn "logInByLdap"
					desc ""
					inUrl false
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
            clz APILogInByLdapReply.class
        }
    }
}