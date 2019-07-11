package org.zstack.core.config;

import java.util.Map;

public interface TemplateConfigFacade {
    String updateTemplateConfig(String category, String name, String templateUuid, String value);

    Map<String, TemplateConfig> getAllConfig(String templateUuid);

    <T> T getConfigValue(String category, String name, String templateUuid, Class<T> clz);

    TemplateConfig createTemplateConfig(TemplateConfigVO vo);
}
