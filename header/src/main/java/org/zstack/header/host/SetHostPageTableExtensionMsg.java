package org.zstack.header.host;

import org.zstack.header.message.NeedReplyMessage;

/**
 * @author: kefeng.wang
 * @date: 2018-11-20
 **/
public class SetHostPageTableExtensionMsg extends NeedReplyMessage implements HostMessage {
    private String uuid;
    private Boolean disabled;

    public SetHostPageTableExtensionMsg() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String getHostUuid() {
        return getUuid();
    }
}
