package org.zstack.core.config;

public interface TemplateConfigBeforeUpdateExtensionPoint {
    void beforeUpdateExtensionPoint(TemplateConfig oldConfig, String newValue);
}
