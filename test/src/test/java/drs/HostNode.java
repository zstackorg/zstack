package drs;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lining on 2019/12/6.
 */
public class HostNode implements Serializable {
    private String uuid;
    private List<VmNode> vmList;

    private int usedCPUPercent; // cpu使用率, [0, 100]
    private int freeCPUPercent; // cpu空闲率, [0, 100]
    private Integer maxSize2MoveCPUPercent; // 需要迁移或者可以迁入的空间，[0, 100]

    private int usedMemoryPercent; // 内存使用率, [0, 100]
    private int freeMemoryPercent; // 内存空闲率, [0, 100]
    private long freePhysicalMemoryBit;
    private long totalMemoryBit;
    private Long maxSize2MoveMemoryBit;

    private long hostCapacityAvailableCpuNum;
    private long hostCapacityAvailableMemoryBit;

    public HostNode(String uuid, int usedCPUPercent, int freeCPUPercent, long freePhysicalMemoryBit, long hostCapacityAvailableCpuNum, long hostCapacityAvailableMemoryBit, long totalMemoryBit, List<VmNode> vmList) {
        this.uuid = uuid;
        this.usedCPUPercent = usedCPUPercent;
        this.freeCPUPercent = freeCPUPercent;
        this.freePhysicalMemoryBit = freePhysicalMemoryBit;
        this.hostCapacityAvailableCpuNum = hostCapacityAvailableCpuNum;
        this.hostCapacityAvailableMemoryBit = hostCapacityAvailableMemoryBit;
        this.totalMemoryBit = totalMemoryBit;
        this.vmList = vmList;
        this.freeMemoryPercent = (int) ((1f * freePhysicalMemoryBit / totalMemoryBit) * 100);
        this.usedMemoryPercent = 100 - freeMemoryPercent;
    }

    private void updateMemoryPercent() {
        this.freeMemoryPercent = (int) ((1f * freePhysicalMemoryBit / totalMemoryBit) * 100);
        this.usedMemoryPercent = 100 - freeMemoryPercent;
    }

    public int getUsedCPUPercent() {
        return usedCPUPercent;
    }

    public void setUsedCPUPercent(int usedCPUPercent) {
        this.usedCPUPercent = usedCPUPercent;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getFreeCPUPercent() {
        return freeCPUPercent;
    }

    public void setFreeCPUPercent(int freeCPUPercent) {
        this.freeCPUPercent = freeCPUPercent;
    }

    public Integer getMaxSize2MoveCPUPercent() {
        return maxSize2MoveCPUPercent;
    }

    public void setMaxSize2MoveCPUPercent(Integer maxSize2MoveCPUPercent) {
        this.maxSize2MoveCPUPercent = maxSize2MoveCPUPercent;
    }

    public long getHostCapacityAvailableCpuNum() {
        return hostCapacityAvailableCpuNum;
    }

    public void setHostCapacityAvailableCpuNum(long hostCapacityAvailableCpuNum) {
        this.hostCapacityAvailableCpuNum = hostCapacityAvailableCpuNum;
    }

    public long getHostCapacityAvailableMemoryBit() {
        return hostCapacityAvailableMemoryBit;
    }

    public void setHostCapacityAvailableMemoryBit(long hostCapacityAvailableMemoryBit) {
        this.hostCapacityAvailableMemoryBit = hostCapacityAvailableMemoryBit;
    }

    public long getFreePhysicalMemoryBit() {
        return freePhysicalMemoryBit;
    }

    public void setFreePhysicalMemoryBit(long freePhysicalMemoryBit) {
        this.freePhysicalMemoryBit = freePhysicalMemoryBit;
        updateMemoryPercent();
    }

    public long getTotalMemoryBit() {
        return totalMemoryBit;
    }

    private void setTotalMemoryBit(long totalMemoryBit) {
        this.totalMemoryBit = totalMemoryBit;
    }

    public Long getMaxSize2MoveMemoryBit() {
        return maxSize2MoveMemoryBit;
    }

    public void setMaxSize2MoveMemoryBit(Long maxSize2MoveMemoryBit) {
        this.maxSize2MoveMemoryBit = maxSize2MoveMemoryBit;
    }

    public List<VmNode> getVmList() {
        return vmList;
    }

    public void setVmList(List<VmNode> vmList) {
        this.vmList = vmList;
    }

    public int getUsedMemoryPercent() {
        return usedMemoryPercent;
    }

    private void setUsedMemoryPercent(int usedMemoryPercent) {
        this.usedMemoryPercent = usedMemoryPercent;
    }

    public int getFreeMemoryPercent() {
        return freeMemoryPercent;
    }

    public void setFreeMemoryPercent(int freeMemoryPercent) {
        this.freeMemoryPercent = freeMemoryPercent;
    }
}
