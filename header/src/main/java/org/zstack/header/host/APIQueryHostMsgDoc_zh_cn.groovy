package org.zstack.header.host

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/hosts"

            header (OAuth: 'the-session-uuid')

            clz APIQueryHostMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryHostReply.class
        }
    }
}