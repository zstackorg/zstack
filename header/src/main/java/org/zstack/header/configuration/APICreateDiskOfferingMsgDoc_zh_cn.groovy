package org.zstack.header.configuration



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/disk-offerings"

            header (OAuth: 'the-session-uuid')

            clz APICreateDiskOfferingMsg.class

            desc ""
            
			params {

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
					name "diskSize"
					enclosedIn "params"
					desc ""
					inUrl false
					type "long"
					optional false
					since "0.6"
					
				}
				column {
					name "sortKey"
					enclosedIn "params"
					desc ""
					inUrl false
					type "int"
					optional true
					since "0.6"
					
				}
				column {
					name "allocationStrategy"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "type"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional true
					since "0.6"
					values ("zstack")
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
            clz APICreateDiskOfferingEvent.class
        }
    }
}