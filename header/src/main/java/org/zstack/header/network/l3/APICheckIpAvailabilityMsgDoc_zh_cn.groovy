package org.zstack.header.network.l3

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/l3-networks/{l3NetworkUuid}/ip/{ip}/availability"

            header (OAuth: 'the-session-uuid')

            clz APICheckIpAvailabilityMsg.class

            desc ""
            
			params {

				column {
					name "l3NetworkUuid"
					enclosedIn ""
					desc "三层网络UUID"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "ip"
					enclosedIn ""
					desc ""
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "systemTags"
					enclosedIn ""
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "userTags"
					enclosedIn ""
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
			}
        }

        response {
            clz APICheckIpAvailabilityReply.class
        }
    }
}