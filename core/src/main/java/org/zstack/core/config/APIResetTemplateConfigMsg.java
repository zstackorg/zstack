package org.zstack.core.config;

import org.springframework.http.HttpMethod;
import org.zstack.header.message.APIEvent;
import org.zstack.header.message.APIMessage;
import org.zstack.header.rest.RestRequest;

@RestRequest(
        path = "/template-configuration/{templateUuid}/actions",
        method = HttpMethod.PUT,
        isAction = true,
        responseClass = APIResetGlobalConfigEvent.class
)
public class APIResetTemplateConfigMsg {
}
