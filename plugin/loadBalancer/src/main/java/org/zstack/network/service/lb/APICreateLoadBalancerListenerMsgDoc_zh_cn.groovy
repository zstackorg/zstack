package org.zstack.network.service.lb



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/load-balancers/{loadBalancerUuid}/listeners"

            header (OAuth: 'the-session-uuid')

            clz APICreateLoadBalancerListenerMsg.class

            desc ""
            
			params {

				column {
					name "loadBalancerUuid"
					enclosedIn "params"
					desc "负载均衡器UUID"
					inUrl true
					type "String"
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
					name "instancePort"
					enclosedIn "params"
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "loadBalancerPort"
					enclosedIn "params"
					desc ""
					inUrl false
					type "int"
					optional false
					since "0.6"
					
				}
				column {
					name "protocol"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					values ("tcp","http")
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
            clz APICreateLoadBalancerListenerEvent.class
        }
    }
}