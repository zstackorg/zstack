package org.zstack.network.service.virtualrouter

import org.zstack.header.errorcode.ErrorCode
import org.zstack.network.service.virtualrouter.VirtualRouterVmInventory

doc {

	title "更新虚拟路由器"

	field {
		name "success"
		desc ""
		type "boolean"
		since "0.6"
	}
	ref {
		name "error"
		path "org.zstack.network.service.virtualrouter.APIUpdateVirtualRouterEvent.error"
		desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null",false
		type "ErrorCode"
		since "0.6"
		clz ErrorCode.class
	}
	ref {
		name "inventory"
		path "org.zstack.network.service.virtualrouter.APIUpdateVirtualRouterEvent.inventory"
		desc "null"
		type "VirtualRouterVmInventory"
		since "0.6"
		clz VirtualRouterVmInventory.class
	}
}
