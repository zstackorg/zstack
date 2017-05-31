package org.zstack.test.integration.storage.primary.local.datavolume

import org.apache.commons.lang.LocaleUtils
import org.zstack.core.CoreGlobalProperty
import org.zstack.sdk.LocalStorageMigrateVolumeAction
import org.zstack.sdk.VolumeInventory
import org.zstack.test.integration.storage.Env
import org.zstack.test.integration.storage.StorageTest
import org.zstack.testlib.*

import static org.zstack.core.Platform.toI18nString

/**
 * Created by camile on 2017/5/4.
 */
class MigrateVolumeCase extends SubCase {
    EnvSpec env

    @Override
    void setup() {
        useSpring(StorageTest.springSpec)
    }

    @Override
    void environment() {
        env = Env.localStorageOneVmEnvForPrimaryStorage()
    }

    @Override
    void test() {
        env.create {
            testMigrateVolumeWhenPsIsMaintainFailure()
        }
    }


    void testMigrateVolumeWhenPsIsMaintainFailure() {
        PrimaryStorageSpec primaryStorageSpec = env.specByName("local")
        String psUuid = primaryStorageSpec.inventory.uuid
        KVMHostSpec kvm = env.specByName("kvm")
        KVMHostSpec kvm1 = env.specByName("kvm1")
        DiskOfferingSpec disk = env.specByName("diskOffering")

        VolumeInventory dataVolume = createDataVolume {
            name = "1G"
            diskOfferingUuid = disk.inventory.uuid
            primaryStorageUuid = psUuid
            systemTags = Arrays.asList("localStorage::hostUuid::" + kvm.inventory.uuid)
        }
        changePrimaryStorageState {
            uuid = psUuid
            stateEvent = "maintain"
        }
        LocalStorageMigrateVolumeAction localStorageMigrateVolumeAction = new LocalStorageMigrateVolumeAction()
        localStorageMigrateVolumeAction.volumeUuid = dataVolume.uuid
        localStorageMigrateVolumeAction.destHostUuid = kvm1.inventory.uuid
        localStorageMigrateVolumeAction.sessionId = adminSession()
        LocalStorageMigrateVolumeAction.Result res = localStorageMigrateVolumeAction.call()
        assert res.error != null
        assert res.error.code.toString() == "SYS.1007"
        assert res.error.description.toString() == "One or more API argument is invalid"
        assert res.error.details.toString().contains(
                toI18nString("the primary storage[uuid:%s] is disabled or maintenance cold migrate is not allowed",
                        LocaleUtils.toLocale(CoreGlobalProperty.LOCALE),
                        psUuid))
    }

    @Override
    void clean() {
        env.delete()
    }
}
