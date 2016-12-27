package org.zstack.header.volume

import java.lang.Long
import java.lang.Long
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
		name "name"
		desc "资源名称"
		type "String"
	}
	field {
		name "description"
		desc "资源的详细描述"
		type "String"
	}
	field {
		name "primaryStorageUuid"
		desc "主存储UUID"
		type "String"
	}
	field {
		name "vmInstanceUuid"
		desc "云主机UUID"
		type "String"
	}
	field {
		name "diskOfferingUuid"
		desc "云盘规格UUID"
		type "String"
	}
	field {
		name "rootImageUuid"
		desc ""
		type "String"
	}
	field {
		name "installPath"
		desc ""
		type "String"
	}
	field {
		name "type"
		desc ""
		type "String"
	}
	field {
		name "format"
		desc ""
		type "String"
	}
	field {
		name "size"
		desc ""
		type "Long"
	}
	field {
		name "actualSize"
		desc ""
		type "Long"
	}
	field {
		name "deviceId"
		desc ""
		type "Integer"
	}
	field {
		name "state"
		desc ""
		type "String"
	}
	field {
		name "status"
		desc ""
		type "String"
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
