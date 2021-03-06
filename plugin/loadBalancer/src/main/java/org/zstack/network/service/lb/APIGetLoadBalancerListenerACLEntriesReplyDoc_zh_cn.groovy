package org.zstack.network.service.lb

import org.zstack.header.errorcode.ErrorCode

doc {

	title "在这里输入结构的名称"

	ref {
		name "error"
		path "org.zstack.network.service.lb.APIGetLoadBalancerListenerACLEntriesReply.error"
		desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null",false
		type "ErrorCode"
		since "0.6"
		clz ErrorCode.class
	}
	field {
		name "success"
		desc ""
		type "boolean"
		since "0.6"
	}
	field {
		name "inventories"
		desc ""
		type "Map"
		since "0.6"
	}
}
