package org.zstack.core.config;

public interface TemplateConfigValidatorExtensionPoint {
    void validateTemplateConfig(String templateUuid, String category, String name, String oldValue, String newValue) throws TemplateConfigException;
}
