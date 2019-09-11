package org.zstack.network.service.flat;

import org.zstack.core.Platform;
import org.zstack.header.apimediator.ApiMessageInterceptionException;
import org.zstack.header.apimediator.ApiMessageInterceptor;
import org.zstack.header.message.APIMessage;

import static org.zstack.network.service.flat.IpStatisticConstants.*;

/**
 * Created by Qi Le on 2019/9/9
 */
public class FlatApiInterceptor implements ApiMessageInterceptor {
    @Override
    public APIMessage intercept(APIMessage msg) throws ApiMessageInterceptionException {
        if (msg instanceof APIGetL3NetworkIpStatisticMsg) {
            validate((APIGetL3NetworkIpStatisticMsg) msg);
        }

        return msg;
    }

    private void validate(APIGetL3NetworkIpStatisticMsg msg) {
        if (!(msg.getLimit() == 20 || msg.getLimit() == 10 || msg.getLimit() == 50 || msg.getLimit() == 100)) {
            throw new ApiMessageInterceptionException(Platform.argerr("Page size should in [10, 20, 50, 100]. Got %d.", msg.getLimit()));
        }
    }
}
