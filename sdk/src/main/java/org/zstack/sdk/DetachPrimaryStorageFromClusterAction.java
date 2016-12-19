package org.zstack.sdk;

import java.util.HashMap;
import java.util.Map;

public class DetachPrimaryStorageFromClusterAction extends AbstractAction {

    private static final HashMap<String, Parameter> parameterMap = new HashMap<>();

    public static class Result {
        public ErrorCode error;
        public DetachPrimaryStorageFromClusterResult value;
    }

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String primaryStorageUuid;

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String clusterUuid;

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
        
        DetachPrimaryStorageFromClusterResult value = res.getResult(DetachPrimaryStorageFromClusterResult.class);
        ret.value = value == null ? new DetachPrimaryStorageFromClusterResult() : value;
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
                
                DetachPrimaryStorageFromClusterResult value = res.getResult(DetachPrimaryStorageFromClusterResult.class);
                ret.value = value == null ? new DetachPrimaryStorageFromClusterResult() : value;
                completion.complete(ret);
            }
        });
    }

    Map<String, Parameter> getParameterMap() {
        return parameterMap;
    }

    RestInfo getRestInfo() {
        RestInfo info = new RestInfo();
        info.httpMethod = "DELETE";
        info.path = "/clusters/{clusterUuid}/primary-storage/{primaryStorageUuid}";
        info.needSession = true;
        info.needPoll = true;
        info.parameterName = "null";
        return info;
    }

}
