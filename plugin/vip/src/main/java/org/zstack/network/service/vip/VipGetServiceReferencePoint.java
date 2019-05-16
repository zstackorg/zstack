package org.zstack.network.service.vip;

import java.util.List;

public interface VipGetServiceReferencePoint {
    public final class ServiceReference{
        String useFor;
        long    count;
        List<String> uuids;

        public ServiceReference(String useFor, long count, List<String> uuids) {
            this.useFor = useFor;
            this.count = count;
            this.uuids = uuids;
        }

        public List<String> getUuids() {
            return uuids;
        }

        public void setUuids(List<String> uuids) {
            this.uuids = uuids;
        }

        public String getUseFor() {
            return useFor;
        }

        public void setUseFor(String useFor) {
            this.useFor = useFor;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    /*how many networks attached*/
    /* this api will return the active peerL3 count bound to this vip */
    ServiceReference getServiceReference(String vipUuid);

    /*this api will return the nic count with peer L3 bound to this vip*/
    ServiceReference getServicePeerL3Reference(String vipUuid, String peerL3Uuid);
}
