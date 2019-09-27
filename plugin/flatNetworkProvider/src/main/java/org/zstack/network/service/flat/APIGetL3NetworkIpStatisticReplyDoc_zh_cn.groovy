package org.zstack.network.service.flat

import org.zstack.header.errorcode.ErrorCode
import org.zstack.network.service.flat.IpStatisticData
import java.lang.Long
import org.zstack.header.errorcode.ErrorCode

doc {

	title "在这里输入结构的名称"

	ref {
		name "error"
		path "org.zstack.network.service.flat.APIGetL3NetworkIpStatisticReply.error"
		desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null",false
		type "ErrorCode"
		since "0.6"
		clz ErrorCode.class
	}
	ref {
		name "ipStatistics"
		path "org.zstack.network.service.flat.APIGetL3NetworkIpStatisticReply.ipStatistics"
		desc "null"
		type "List"
		since "0.6"
		clz IpStatisticData.class
	}
	field {
		name "total"
		desc ""
		type "Long"
		since "0.6"
	}
	field {
		name "success"
		desc ""
		type "boolean"
		since "0.6"
	}
	ref {
		name "error"
		path "org.zstack.network.service.flat.APIGetL3NetworkIpStatisticReply.error"
		desc "null"
		type "ErrorCode"
		since "0.6"
		clz ErrorCode.class
	}
}
