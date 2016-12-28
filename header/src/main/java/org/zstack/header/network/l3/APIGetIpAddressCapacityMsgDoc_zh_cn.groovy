package org.zstack.header.network.l3



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/ip-capacity"

            header (OAuth: 'the-session-uuid')

            clz APIGetIpAddressCapacityMsg.class

            desc ""
            
			params {

				column {
					name "zoneUuids"
					enclosedIn "params"
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "l3NetworkUuids"
					enclosedIn "params"
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "ipRangeUuids"
					enclosedIn "params"
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "all"
					enclosedIn "params"
					desc ""
					inUrl false
					type "boolean"
					optional true
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
            clz APIGetIpAddressCapacityReply.class
        }
    }
}