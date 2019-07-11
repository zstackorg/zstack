package org.zstack.core.config;

import org.springframework.http.HttpMethod;
import org.zstack.header.identity.Action;
import org.zstack.header.message.APIParam;
import org.zstack.header.query.APIQueryMessage;
import org.zstack.header.query.AutoQuery;
import org.zstack.header.rest.RestRequest;

import java.util.List;

import static java.util.Arrays.asList;

@AutoQuery(replyClass = APIQueryTemplateConfigReply.class, inventoryClass = TemplateConfigInventory.class)
@Action(category = "configuration", names = {"read"})
@RestRequest(
        path = "/template-configurations/{templateUuid}/actions",
        method = HttpMethod.GET,
        responseClass = APIQueryGlobalConfigReply.class
)
public class APIQueryTemplateConfigMsg extends APIQueryMessage {
    public static List<String> __example__() {
        return asList();
    }
}
