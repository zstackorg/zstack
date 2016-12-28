package org.zstack.storage.ceph.primary

doc {
    title "在这里填写API标题"

    desc "在这里填写API描述"

    rest {
        request {
            url "GET /v1/primary-storage/ceph"

            header (OAuth: 'the-session-uuid')

            clz APIQueryCephPrimaryStorageMsg.class

            desc ""
            
		params APIQueryMessage.class
        }

        response {
            clz APIQueryPrimaryStorageReply.class
        }
    }
}