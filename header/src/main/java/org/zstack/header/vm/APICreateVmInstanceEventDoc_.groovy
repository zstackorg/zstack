package org.zstack.header.vm

import org.zstack.header.volume.VolumeInventory
import org.zstack.header.vm.VmInstanceInventory

doc {
    field {
        name "inventory"
        desc ""
        type ""
    }

    ref {
        path "inventory.vmNics"
        desc ""
        clz VmNicInventory.class
    }
}
