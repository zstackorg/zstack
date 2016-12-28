package org.zstack.header.zone

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/zones"

            header (OAuth: 'the-session-uuid')

            clz APIQueryZoneMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryZoneReply.class
        }
    }
}