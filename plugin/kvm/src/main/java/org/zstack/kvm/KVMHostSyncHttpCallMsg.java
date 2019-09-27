package org.zstack.kvm;

import org.zstack.header.log.HasSensitiveInfo;
import org.zstack.header.host.HostMessage;
import org.zstack.header.log.NoLogging;
import org.zstack.header.message.CarrierMessage;
import org.zstack.header.message.NeedReplyMessage;
import org.zstack.utils.gson.JSONObjectUtil;

/**
 */
public class KVMHostSyncHttpCallMsg extends NeedReplyMessage implements HostMessage, CarrierMessage, HasSensitiveInfo {
    private String path;
    @NoLogging(type = NoLogging.Type.Auto)
    private Object command;
    private String hostUuid;
    private boolean noStatusCheck;
    private String commandClassName;

    public boolean isNoStatusCheck() {
        return noStatusCheck;
    }

    public void setNoStatusCheck(boolean noStatusCheck) {
        this.noStatusCheck = noStatusCheck;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getCommand() {
        return command;
    }

    public String getFormatCommand() {
        return JSONObjectUtil.toJsonString(command);
    }

    public void setCommand(Object command) {
        this.command = command;
        commandClassName = command.getClass().getName();
    }

    public String getCommandClassName() {
        return commandClassName;
    }

    @Override
    public String getHostUuid() {
        return hostUuid;
    }

    public void setHostUuid(String hostUuid) {
        this.hostUuid = hostUuid;
    }
}
