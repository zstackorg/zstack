package org.zstack.ldap

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/ldap/accounts/refs"

            header (OAuth: 'the-session-uuid')

            clz APIQueryLdapAccountMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryLdapAccountReply.class
        }
    }
}