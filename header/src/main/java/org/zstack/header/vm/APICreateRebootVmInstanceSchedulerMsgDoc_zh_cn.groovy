package org.zstack.header.vm



doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "POST /v1/vm-instances/{vmUuid}/schedulers/rebooting"

            header (OAuth: 'the-session-uuid')

            clz APICreateRebootVmInstanceSchedulerMsg.class

            desc ""
            
			params {

				column {
					name "vmUuid"
					enclosedIn "params"
					desc ""
					inUrl true
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "schedulerName"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "schedulerDescription"
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
					optional false
					since "0.6"
					values ("simple","cron")
				}
				column {
					name "interval"
					enclosedIn "params"
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "repeatCount"
					enclosedIn "params"
					desc ""
					inUrl false
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "startTime"
					enclosedIn "params"
					desc ""
					inUrl false
					type "Long"
					optional true
					since "0.6"
					
				}
				column {
					name "cron"
					enclosedIn "params"
					desc ""
					inUrl false
					type "String"
					optional true
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
            clz APICreateRebootVmInstanceSchedulerEvent.class
        }
    }
}