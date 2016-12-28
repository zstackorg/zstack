package org.zstack.header.console

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/consoles/agents"

            header (OAuth: 'the-session-uuid')

            clz APIQueryConsoleProxyAgentMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryConsoleProxyAgentReply.class
        }
    }
}