package org.zstack.sdk;

import org.zstack.sdk.VpcFirewallRuleSetInterfaceRefInventory;

public class AttachRuleSetResult {
    public VpcFirewallRuleSetInterfaceRefInventory inventory;
    public void setInventory(VpcFirewallRuleSetInterfaceRefInventory inventory) {
        this.inventory = inventory;
    }
    public VpcFirewallRuleSetInterfaceRefInventory getInventory() {
        return this.inventory;
    }

}
