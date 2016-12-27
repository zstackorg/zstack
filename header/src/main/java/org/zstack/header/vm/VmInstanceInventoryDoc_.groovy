package org.zstack.header.vm

import java.lang.Long
import java.lang.Integer
import java.lang.Long
import java.sql.Timestamp
import java.sql.Timestamp
import org.zstack.header.vm.VmNicInventory
import org.zstack.header.volume.VolumeInventory

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
		name "zoneUuid"
		desc "区域UUID"
		type "String"
	}
	field {
		name "clusterUuid"
		desc "集群UUID"
		type "String"
	}
	field {
		name "imageUuid"
		desc "镜像UUID"
		type "String"
	}
	field {
		name "hostUuid"
		desc "物理机UUID"
		type "String"
	}
	field {
		name "lastHostUuid"
		desc ""
		type "String"
	}
	field {
		name "instanceOfferingUuid"
		desc "计算规格UUID"
		type "String"
	}
	field {
		name "rootVolumeUuid"
		desc ""
		type "String"
	}
	field {
		name "platform"
		desc ""
		type "String"
	}
	field {
		name "defaultL3NetworkUuid"
		desc ""
		type "String"
	}
	field {
		name "type"
		desc ""
		type "String"
	}
	field {
		name "hypervisorType"
		desc ""
		type "String"
	}
	field {
		name "memorySize"
		desc ""
		type "Long"
	}
	field {
		name "cpuNum"
		desc ""
		type "Integer"
	}
	field {
		name "cpuSpeed"
		desc ""
		type "Long"
	}
	field {
		name "allocatorStrategy"
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
	field {
		name "state"
		desc ""
		type "String"
	}
	ref {
		name "vmNics"
		path "org.zstack.header.vm.VmInstanceInventory.vmNics"
		desc "结构字段，参考[这里](#org.zstack.header.vm.VmInstanceInventory.vmNics)获取详细信息"
		type "List"
		clz VmNicInventory.class
	}
	ref {
		name "allVolumes"
		path "org.zstack.header.vm.VmInstanceInventory.allVolumes"
		desc "结构字段，参考[这里](#org.zstack.header.vm.VmInstanceInventory.allVolumes)获取详细信息"
		type "List"
		clz VolumeInventory.class
	}
}
