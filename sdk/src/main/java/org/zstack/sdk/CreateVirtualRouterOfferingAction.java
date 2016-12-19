package org.zstack.sdk;

import java.util.HashMap;
import java.util.Map;

public class CreateVirtualRouterOfferingAction extends AbstractAction {

    private static final HashMap<String, Parameter> parameterMap = new HashMap<>();

    public static class Result {
        public ErrorCode error;
        public CreateInstanceOfferingResult value;
    }

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String zoneUuid;

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String managementNetworkUuid;

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String imageUuid;

    @Param(required = false, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String publicNetworkUuid;

    @Param(required = false)
    public java.lang.Boolean isDefault;

    @Param(required = true, maxLength = 255, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String name;

    @Param(required = false, maxLength = 2048, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String description;

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, numberRange = {1,1024}, noTrim = false)
    public int cpuNum;

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, numberRange = {1,2147483647}, noTrim = false)
    public int cpuSpeed;

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, numberRange = {1,-1}, noTrim = false)
    public long memorySize;

    @Param(required = false)
    public java.lang.String allocatorStrategy;

    @Param(required = false)
    public int sortKey;

    @Param(required = false)
    public java.lang.String type;

    @Param(required = false)
    public java.lang.String resourceUuid;

    @Param(required = false)
    public java.util.List systemTags;

    @Param(required = false)
    public java.util.List userTags;

    @Param(required = true)
    public String sessionId;

    public long timeout;
    
    public long pollingInterval;


    public Result call() {
        ApiResult res = ZSClient.call(this);
        Result ret = new Result();
        if (res.error != null) {
            ret.error = res.error;
            return ret;
        }
        
        CreateInstanceOfferingResult value = res.getResult(CreateInstanceOfferingResult.class);
        ret.value = value == null ? new CreateInstanceOfferingResult() : value;
        return ret;
    }

    public void call(final Completion<Result> completion) {
        ZSClient.call(this, new InternalCompletion() {
            @Override
            public void complete(ApiResult res) {
                Result ret = new Result();
                if (res.error != null) {
                    ret.error = res.error;
                    completion.complete(ret);
                    return;
                }
                
                CreateInstanceOfferingResult value = res.getResult(CreateInstanceOfferingResult.class);
                ret.value = value == null ? new CreateInstanceOfferingResult() : value;
                completion.complete(ret);
            }
        });
    }

    Map<String, Parameter> getParameterMap() {
        return parameterMap;
    }

    RestInfo getRestInfo() {
        RestInfo info = new RestInfo();
        info.httpMethod = "POST";
        info.path = "/instance-offerings/virtual-routers";
        info.needSession = true;
        info.needPoll = true;
        info.parameterName = "params";
        return info;
    }

}
