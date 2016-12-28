package org.zstack.header.image

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/images"

            header (OAuth: 'the-session-uuid')

            clz APIQueryImageMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryImageReply.class
        }
    }
}