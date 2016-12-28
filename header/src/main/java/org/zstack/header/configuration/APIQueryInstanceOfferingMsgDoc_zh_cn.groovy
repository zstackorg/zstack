package org.zstack.header.configuration

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/instance-offerings"

            header (OAuth: 'the-session-uuid')

            clz APIQueryInstanceOfferingMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryInstanceOfferingReply.class
        }
    }
}