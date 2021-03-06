package org.zstack.header.cluster

import org.zstack.header.errorcode.ErrorCode

doc {

	title "集群清单"

	field {
		name "success"
		desc ""
		type "boolean"
		since "0.6"
	}
	ref {
		name "error"
		path "org.zstack.header.cluster.APIUpdateClusterEvent.error"
		desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null",false
		type "ErrorCode"
		since "0.6"
		clz ErrorCode.class
	}
	ref {
		name "inventory"
		path "org.zstack.header.cluster.APIUpdateClusterEvent.inventory"
		desc "null"
		type "ClusterInventory"
		since "0.6"
		clz ClusterInventory.class
	}
}
