package org.zstack.header.network.l3

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/l3-networks"

            header (OAuth: 'the-session-uuid')

            clz APIQueryL3NetworkMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryL3NetworkReply.class
        }
    }
}