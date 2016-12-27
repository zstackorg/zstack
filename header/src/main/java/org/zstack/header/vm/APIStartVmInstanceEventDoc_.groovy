package org.zstack.header.vm

import org.zstack.header.errorcode.ErrorCode
import org.zstack.header.vm.VmInstanceInventory

doc {

	title "在这里输入结构的名称"

	ref {
		name "error"
		path "org.zstack.header.vm.APIStartVmInstanceEvent.error"
		desc "错误码，如果不为null，则表示操作失败。结构字段，参考[这里](#org.zstack.header.vm.APIStartVmInstanceEvent.error)获取详细信息"
		type "ErrorCode"
		clz ErrorCode.class
	}
	ref {
		name "inventory"
		path "org.zstack.header.vm.APIStartVmInstanceEvent.inventory"
		desc "结构字段，参考[这里](#org.zstack.header.vm.APIStartVmInstanceEvent.inventory)获取详细信息"
		type "VmInstanceInventory"
		clz VmInstanceInventory.class
	}
}
