package org.zstack.core.config;

import org.zstack.header.configuration.PythonClassInventory;
import org.zstack.header.search.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@PythonClassInventory
@Inventory(mappingVOClass = TemplateVO.class)
public class TemplateInventory {
    private String uuid;
    private String name;
    private String type;
    private String description;

    public static TemplateInventory valueOf(TemplateVO vo) {
        TemplateInventory inv = new TemplateInventory();
        inv.setUuid(vo.getUuid());
        inv.setName(vo.getName());
        inv.setType(vo.getType());
        inv.setDescription(vo.getDescription());
        return inv;
    }

    public static List<TemplateInventory> valueOf(Collection<TemplateVO> vos) {
        List<TemplateInventory> invs = new ArrayList<TemplateInventory>();
        for (TemplateVO vo : vos) {
            invs.add(valueOf(vo));
        }
        return invs;
    }

    public static TemplateInventory valueOf(Template t) {
        TemplateInventory inv = new TemplateInventory();
        inv.setUuid(t.getUuid());
        inv.setName(t.getName());
        inv.setType(t.getType());
        inv.setDescription(t.getDescription());
        return inv;
    }
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
