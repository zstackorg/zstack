package org.zstack.header.vm

import java.lang.Integer
import java.sql.Timestamp
import java.sql.Timestamp

doc {

	title "在这里输入结构的名称"

	field {
		name "uuid"
		desc "资源的UUID，唯一标示该资源"
		type "String"
	}
	field {
		name "vmInstanceUuid"
		desc "云主机UUID"
		type "String"
	}
	field {
		name "l3NetworkUuid"
		desc "三层网络UUID"
		type "String"
	}
	field {
		name "ip"
		desc ""
		type "String"
	}
	field {
		name "mac"
		desc ""
		type "String"
	}
	field {
		name "netmask"
		desc ""
		type "String"
	}
	field {
		name "gateway"
		desc ""
		type "String"
	}
	field {
		name "metaData"
		desc ""
		type "String"
	}
	field {
		name "deviceId"
		desc ""
		type "Integer"
	}
	field {
		name "createDate"
		desc "创建时间"
		type "Timestamp"
	}
	field {
		name "lastOpDate"
		desc "最后一次修改时间"
		type "Timestamp"
	}
}
