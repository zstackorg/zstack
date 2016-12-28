package org.zstack.header.simulator

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/hosts/simulators"

            header (OAuth: 'the-session-uuid')

            clz APIAddSimulatorHostMsg.class

            desc ""
            
			params {

				column {
					name "memoryCapacity"
					enclosedIn "params"
					desc ""
					inUrl false
					type "long"
					optional false
					since "0.6"
					
				}
				column {
					name "cpuCapacity"
					enclosedIn "params"
					desc ""
					inUrl false
					type "long"
					optional false
					since "0.6"
					
				}
				column {
					name "name"
					enclosedIn "params"
					desc "资源名称"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "description"
					enclosedIn "params"
					desc "资源的详细描述"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "managementIp"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "clusterUuid"
					enclosedIn "params"
					desc "集群UUID"
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "resourceUuid"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
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
            clz APIAddHostEvent.class
        }
    }
}