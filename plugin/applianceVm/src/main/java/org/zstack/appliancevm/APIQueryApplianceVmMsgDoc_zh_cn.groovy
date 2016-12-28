package org.zstack.appliancevm

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/vm-instances/appliances"

            header (OAuth: 'the-session-uuid')

            clz APIQueryApplianceVmMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryApplianceVmReply.class
        }
    }
}