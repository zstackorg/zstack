package drs;

import java.io.Serializable;

/**
 * Created by lining on 2019/12/6.
 */
public class VmNode implements Serializable {
    public String uuid;

    private int usedCPUPercent; // 占用物理机CPU百分比
    private int usedMemoryPercent; // 占用物理机内存百分比
    private long usedPhysicalMemoryBit;

    private int cpuNum;
    private long memorySize;

    public VmNode(String uuid, int cpuNum, long memorySize, int usedCPUPercent, long usedPhysicalMemoryBit) {
        this.uuid = uuid;
        this.cpuNum = cpuNum;
        this.memorySize = memorySize;
        this.usedCPUPercent = usedCPUPercent;
        this.usedPhysicalMemoryBit = usedPhysicalMemoryBit;
    }

    public VmNode(String uuid, int cpuNum, long memorySize, int usedCPUPercent, long usedPhysicalMemoryBit, int usedMemoryPercent) {
        this.uuid = uuid;
        this.cpuNum = cpuNum;
        this.memorySize = memorySize;
        this.usedCPUPercent = usedCPUPercent;
        this.usedPhysicalMemoryBit = usedPhysicalMemoryBit;
        this.usedMemoryPercent = usedMemoryPercent;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(int cpuNum) {
        this.cpuNum = cpuNum;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public int getUsedCPUPercent() {
        return usedCPUPercent;
    }

    public void setUsedCPUPercent(int usedCPUPercent) {
        this.usedCPUPercent = usedCPUPercent;
    }

    public long getUsedPhysicalMemoryBit() {
        return usedPhysicalMemoryBit;
    }

    public void setUsedPhysicalMemoryBit(int usedPhysicalMemoryBit) {
        this.usedPhysicalMemoryBit = usedPhysicalMemoryBit;
    }

    public int getUsedMemoryPercent() {
        return usedMemoryPercent;
    }

    private void setUsedMemoryPercent(int usedMemoryPercent) {
        this.usedMemoryPercent = usedMemoryPercent;
    }
}
