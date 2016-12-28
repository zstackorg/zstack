package org.zstack.network.securitygroup

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/security-groups/vm-instances/nics"

            header (OAuth: 'the-session-uuid')

            clz APIQueryVmNicInSecurityGroupMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryVmNicInSecurityGroupReply.class
        }
    }
}