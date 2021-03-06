package org.zstack.network.service.lb

import org.zstack.header.errorcode.ErrorCode
import org.zstack.network.service.lb.LoadBalancerServerGroupInventory

doc {

	title "在这里输入结构的名称"

	field {
		name "success"
		desc ""
		type "boolean"
		since "0.6"
	}
	ref {
		name "error"
		path "org.zstack.network.service.lb.APIUpdateLoadBalancerServerGroupEvent.error"
		desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null",false
		type "ErrorCode"
		since "0.6"
		clz ErrorCode.class
	}
	ref {
		name "inventory"
		path "org.zstack.network.service.lb.APIUpdateLoadBalancerServerGroupEvent.inventory"
		desc "null"
		type "LoadBalancerServerGroupInventory"
		since "0.6"
		clz LoadBalancerServerGroupInventory.class
	}
}
