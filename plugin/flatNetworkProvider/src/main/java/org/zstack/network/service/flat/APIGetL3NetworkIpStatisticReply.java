package org.zstack.network.service.flat;

import org.zstack.header.message.APIReply;
import org.zstack.header.rest.RestResponse;

import java.util.Collections;
import java.util.List;

/**
 * Created by Qi Le on 2019/9/9
 */
@RestResponse(fieldsTo = {"all"})
public class APIGetL3NetworkIpStatisticReply extends APIReply {
    private List<IpStatisticData> ipStatistics;

    private Integer count;

    public static APIGetL3NetworkIpStatisticReply __example__() {
        APIGetL3NetworkIpStatisticReply reply = new APIGetL3NetworkIpStatisticReply();
        IpStatisticData data = new IpStatisticData();
        data.setIp("192.168.0.1");
        data.setResourceTypes(Collections.singletonList(IpStatisticConstants.ResourceType.OTHER));
        reply.setIpStatistics(Collections.singletonList(data));
        reply.setCount(1);
        return reply;
    }

    public List<IpStatisticData> getIpStatistics() {
        return ipStatistics;
    }

    public void setIpStatistics(List<IpStatisticData> ipStatistics) {
        this.ipStatistics = ipStatistics;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
