package org.zstack.header.errorcode

import org.zstack.header.errorcode.ErrorCode

doc {

	title "在这里输入结构的名称"

	field {
		name "code"
		desc ""
		type "String"
	}
	field {
		name "description"
		desc "资源的详细描述"
		type "String"
	}
	field {
		name "details"
		desc ""
		type "String"
	}
	field {
		name "elaboration"
		desc ""
		type "String"
	}
	ref {
		name "cause"
		path "org.zstack.header.errorcode.ErrorCode.cause"
		desc "结构字段，参考[这里](#org.zstack.header.errorcode.ErrorCode.cause)获取详细信息"
		type "ErrorCode"
		clz ErrorCode.class
	}
	field {
		name "opaque"
		desc ""
		type "LinkedHashMap"
	}
}
