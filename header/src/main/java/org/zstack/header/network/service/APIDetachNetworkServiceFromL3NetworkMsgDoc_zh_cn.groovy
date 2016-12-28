package org.zstack.header.network.service

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "DELETE /v1/l3-networks/{l3NetworkUuid}/network-services"

            header (OAuth: 'the-session-uuid')

            clz APIDetachNetworkServiceFromL3NetworkMsg.class

            desc ""
            
			params {

				column {
					name "l3NetworkUuid"
					enclosedIn "params"
					desc "三层网络UUID"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "networkServices"
					enclosedIn "params"
					desc ""
					inUrl false
					type "Map"
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
            clz APIDetachNetworkServiceFromL3NetworkEvent.class
        }
    }
}