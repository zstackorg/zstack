package org.zstack.header.vm

import org.zstack.header.errorcode.ErrorCode

doc {

    title "删除云主机指定IP结果"

	field {
		name "success"
		desc ""
		type "boolean"
		since "0.6"
	}
    ref {
        name "error"
        path "org.zstack.header.vm.APIDeleteVmStaticIpEvent.error"
        desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null", false
        type "ErrorCode"
        since "0.6"
        clz ErrorCode.class
    }
}
