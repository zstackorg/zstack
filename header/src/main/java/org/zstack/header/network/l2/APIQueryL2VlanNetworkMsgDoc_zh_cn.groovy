package org.zstack.header.network.l2

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/l2-vlan-networks"

            header (OAuth: 'the-session-uuid')

            clz APIQueryL2VlanNetworkMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryL2VlanNetworkReply.class
        }
    }
}