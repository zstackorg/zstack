package org.zstack.core.config;

public interface GlobalConfigTemplateValidatorExtensionPoint {
    void validateTemplate(String type, String name) throws GlobalConfigTemplateException;
}

