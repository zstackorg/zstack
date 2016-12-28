package org.zstack.ldap

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/ladp/account-refs"

            header (OAuth: 'the-session-uuid')

            clz APIBindLdapAccountMsg.class

            desc ""
            
			params {

				column {
					name "ldapUid"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "accountUuid"
					enclosedIn "params"
					desc "账户UUID"
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
            clz APIBindLdapAccountEvent.class
        }
    }
}