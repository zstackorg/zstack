package org.zstack.network.service.virtualrouter

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/instance-offerings/virtual-routers"

            header (OAuth: 'the-session-uuid')

            clz APIQueryVirtualRouterOfferingMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryVirtualRouterOfferingReply.class
        }
    }
}