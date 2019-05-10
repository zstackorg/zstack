package org.zstack.network.service.vip;

public interface VipGetServiceReferencePoint {
    public final class ServiceReference{
        String useFor;
        long    count;

        public ServiceReference(String useFor, long count) {
            this.useFor = useFor;
            this.count = count;
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

    /* this api will return the active rules(except serviceUuid) count bound to this vip */
    ServiceReference getServiceReference(String vipUuid);
}
