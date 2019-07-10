package org.zstack.core.config;

import org.zstack.header.query.APIQueryReply;
import org.zstack.header.rest.RestResponse;
import java.util.List;
import static java.util.Arrays.asList;


@RestResponse(allTo = "inventories")
public class APIQueryTemplateReply extends APIQueryReply {
    private List<TemplateInventory> inventories;

    public List<TemplateInventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<TemplateInventory> inventories) {
        this.inventories = inventories;
    }

    public static APIQueryTemplateReply __example__() {
        APIQueryTemplateReply reply = new APIQueryTemplateReply();
        TemplateInventory inv = new TemplateInventory();
        inv.setUuid("reservedCapacity");
        inv.setName("scenes1");
        inv.setType("System");
        inv.setDescription("For scenes1");
        reply.setInventories(asList(inv));
        return reply;
    }
}
