package org.zstack.header.configuration

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/disk-offerings"

            header (OAuth: 'the-session-uuid')

            clz APIQueryDiskOfferingMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryDiskOfferingReply.class
        }
    }
}