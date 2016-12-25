package org.zstack.header.zone

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /zones/{uuid}/actions"

            header (OAuth: 'the-session-uuid')

            clz APIChangeZoneStateMsg.class

            desc ""
            
			params {
				column {
					name "uuid"
					desc ""
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "stateEvent"
					desc ""
					type "String"
					optional true
					since "0.6"
					values ("enable","disable")
				}
				column {
					name "systemTags"
					desc ""
					type "List"
					optional false
					since "0.6"
					
				}
				column {
					name "userTags"
					desc ""
					type "List"
					optional false
					since "0.6"
					
				}
			}
        }

        response {
            clz APIChangeZoneStateEvent.class
        }
    }
}