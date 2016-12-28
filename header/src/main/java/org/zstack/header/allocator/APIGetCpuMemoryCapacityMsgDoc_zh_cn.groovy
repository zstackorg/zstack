package org.zstack.header.allocator



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/hosts/capacities/cpu-memory"

            header (OAuth: 'the-session-uuid')

            clz APIGetCpuMemoryCapacityMsg.class

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
					name "clusterUuids"
					enclosedIn "params"
					desc ""
					inUrl false
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "hostUuids"
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
            clz APIGetCpuMemoryCapacityReply.class
        }
    }
}