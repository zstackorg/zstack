package org.zstack.header.tag

import org.zstack.header.errorcode.ErrorCode
import org.zstack.header.tag.SystemTagInventory

doc {

	title "标签清单"

	field {
		name "success"
		desc ""
		type "boolean"
		since "0.6"
	}
	ref {
		name "error"
		path "org.zstack.header.tag.APIQuerySystemTagReply.error"
		desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null",false
		type "ErrorCode"
		since "0.6"
		clz ErrorCode.class
	}
	ref {
		name "inventories"
		path "org.zstack.header.tag.APIQuerySystemTagReply.inventories"
		desc "null"
		type "List"
		since "0.6"
		clz SystemTagInventory.class
	}
}
