package org.zstack.kvm



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "PUT /v1/hosts/kvm/{uuid}/actions"

            header (OAuth: 'the-session-uuid')

            clz APIUpdateKVMHostMsg.class

            desc ""
            
			params {

				column {
					name "username"
					enclosedIn "updateKVMHost"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "password"
					enclosedIn "updateKVMHost"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sshPort"
					enclosedIn "updateKVMHost"
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "uuid"
					enclosedIn "updateKVMHost"
					desc "资源的UUID，唯一标示该资源"
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "name"
					enclosedIn "updateKVMHost"
					desc "资源名称"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "description"
					enclosedIn "updateKVMHost"
					desc "资源的详细描述"
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "managementIp"
					enclosedIn "updateKVMHost"
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
            clz APIUpdateHostEvent.class
        }
    }
}