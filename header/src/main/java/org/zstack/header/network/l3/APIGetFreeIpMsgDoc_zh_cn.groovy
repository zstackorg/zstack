package org.zstack.header.network.l3

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1null"

            header (OAuth: 'the-session-uuid')

            clz APIGetFreeIpMsg.class

            desc ""
            
			params {

				column {
					name "l3NetworkUuid"
					enclosedIn "params"
					desc "三层网络UUID"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "ipRangeUuid"
					enclosedIn "params"
					desc "IP段UUID"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "start"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "limit"
					enclosedIn "params"
					desc ""
					inUrl false
					type "int"
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
            clz APIGetFreeIpReply.class
        }
    }
}