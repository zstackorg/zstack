package org.zstack.core.config;

public interface TemplateConfigUpdateExtensionPoint {
    void updateTemplateConfig(TemplateConfig oldConfig, TemplateConfig newConfig);
}
