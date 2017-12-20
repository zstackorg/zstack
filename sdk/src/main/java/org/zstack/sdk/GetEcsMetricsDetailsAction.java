package org.zstack.sdk;

import java.util.HashMap;
import java.util.Map;

public class GetEcsMetricsDetailsAction extends AbstractAction {

    private static final HashMap<String, Parameter> parameterMap = new HashMap<>();

    private static final HashMap<String, Parameter> nonAPIParameterMap = new HashMap<>();

    public static class Result {
        public ErrorCode error;
        public GetEcsMetricsDetailsResult value;

        public Result throwExceptionIfError() {
            if (error != null) {
                throw new ApiException(
                    String.format("error[code: %s, description: %s, details: %s]", error.code, error.description, error.details)
                );
            }
            
            return this;
        }
    }

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String ecsUuid;

    @Param(required = true, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String metrics;

    @Param(required = false, nonempty = false, nullElements = false, emptyString = true, numberRange = {1L,43200L}, noTrim = false)
    public java.lang.Integer startTime;

    @Param(required = false, validValues = {"days","hours","minutes"}, nonempty = false, nullElements = false, emptyString = true, noTrim = false)
    public java.lang.String startTimeUnit;

    @Param(required = false)
    public java.util.List systemTags;

    @Param(required = false)
    public java.util.List userTags;

    @Param(required = true)
    public String sessionId;


    private Result makeResult(ApiResult res) {
        Result ret = new Result();
        if (res.error != null) {
            ret.error = res.error;
            return ret;
        }
        
        GetEcsMetricsDetailsResult value = res.getResult(GetEcsMetricsDetailsResult.class);
        ret.value = value == null ? new GetEcsMetricsDetailsResult() : value; 

        return ret;
    }

    public Result call() {
        ApiResult res = ZSClient.call(this);
        return makeResult(res);
    }

    public void call(final Completion<Result> completion) {
        ZSClient.call(this, new InternalCompletion() {
            @Override
            public void complete(ApiResult res) {
                completion.complete(makeResult(res));
            }
        });
    }

    Map<String, Parameter> getParameterMap() {
        return parameterMap;
    }

    Map<String, Parameter> getNonAPIParameterMap() {
        return nonAPIParameterMap;
    }

    RestInfo getRestInfo() {
        RestInfo info = new RestInfo();
        info.httpMethod = "GET";
        info.path = "/hybrid/aliyun/ecs/metrics_details";
        info.needSession = true;
        info.needPoll = false;
        info.parameterName = "";
        return info;
    }

}
