package org.zstack.network.service.eip



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/eips/{uuid}/actions"

            header (OAuth: 'the-session-uuid')

            clz APIChangeEipStateMsg.class

            desc ""
            
			params {

				column {
					name "uuid"
					enclosedIn "changeEipState"
					desc "资源的UUID，唯一标示该资源"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "stateEvent"
					enclosedIn "changeEipState"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					values ("enable","disable")
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
            clz APIChangeEipStateEvent.class
        }
    }
}