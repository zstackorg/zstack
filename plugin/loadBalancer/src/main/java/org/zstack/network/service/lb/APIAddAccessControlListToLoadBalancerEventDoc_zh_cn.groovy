package org.zstack.network.service.lb

import org.zstack.header.errorcode.ErrorCode
import org.zstack.network.service.lb.LoadBalancerListenerInventory

doc {

	title "添加监听器的访问控制策略"

	field {
		name "success"
		desc ""
		type "boolean"
		since "0.6"
	}
	ref {
		name "error"
		path "org.zstack.network.service.lb.APIAddAccessControlListToLoadBalancerEvent.error"
		desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null",false
		type "ErrorCode"
		since "3.9"
		clz ErrorCode.class
	}
	ref {
		name "inventory"
		path "org.zstack.network.service.lb.APIAddAccessControlListToLoadBalancerEvent.inventory"
		desc "null"
		type "LoadBalancerListenerInventory"
		since "3.9"
		clz LoadBalancerListenerInventory.class
	}
}
