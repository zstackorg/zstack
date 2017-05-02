package org.zstack.sdk;

public class PrepareBaremetalPxeServerResult {
    public BaremetalPxeServerInventory inventory;
    public void setInventory(BaremetalPxeServerInventory inventory) {
        this.inventory = inventory;
    }
    public BaremetalPxeServerInventory getInventory() {
        return this.inventory;
    }

}
