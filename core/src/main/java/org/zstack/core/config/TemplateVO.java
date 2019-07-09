package org.zstack.core.config;

import javax.persistence.*;

@Entity
@Table
public class TemplateVO {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private String uuid;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private String description;

    @Column
    private String defaultValue;

    @Column
    private String value;

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

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getDescription() {return description; }

    public void setDescription(String description) { this.description = description; }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TemplateVO toTemplate() {
        TemplateVO t = new TemplateVO();
        t.setUuid(this.getUuid());
        t.setName(this.getName());
        t.setType(this.getType());
        t.setValue(this.getValue());
        t.setDescription(this.getDescription());
        return t;
    }

}
