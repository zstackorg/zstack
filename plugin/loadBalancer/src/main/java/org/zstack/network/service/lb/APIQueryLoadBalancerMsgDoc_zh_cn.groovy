package org.zstack.network.service.lb

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/load-balancers"

            header (OAuth: 'the-session-uuid')

            clz APIQueryLoadBalancerMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryLoadBalancerReply.class
        }
    }
}