package org.zstack.test.integration.core.config;

import org.zstack.core.config.TemplateConfig;

public class TemplateConfigForTest {

    public static TemplateConfig RESERVED_MEMORY_CAPACITY = new TemplateConfig("kvm", "reservedMemory", "00001111");
    public static TemplateConfig PING_INTERVAL = new TemplateConfig("host", "ping.interval", "00002222");
    public static TemplateConfig EMULATE_HYPERV = new TemplateConfig("vm", "emulateHyperV", "00003333");

}
