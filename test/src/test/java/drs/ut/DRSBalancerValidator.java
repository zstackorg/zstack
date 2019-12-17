package drs.ut;

import drs.HostNode;
import drs.MigrateTask;
import drs.VmNode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lining on 2019/12/6.
 */
public class DRSBalancerValidator {
    private List<HostNode> hostNodes;
    private List<MigrateTask> tasks;

    private Integer cpuThreshold;
    private Integer memThreshold;

    private int averageCpuLoadPercent;
    private int averageMemoryLoadPercent;

    public DRSBalancerValidator(List<HostNode> hostNodes, List<MigrateTask> tasks, Integer cpuThreshold, Integer memThreshold) {
        assert hostNodes != null;
        assert tasks != null;

        this.hostNodes = hostNodes;
        this.tasks = tasks;
        this.cpuThreshold = cpuThreshold;
        this.memThreshold = memThreshold;

        this.averageCpuLoadPercent = hostNodes.stream().mapToInt(HostNode::getUsedCPUPercent).sum() / hostNodes.size();
        this.averageMemoryLoadPercent = (int) ((1f * hostNodes.stream().mapToLong(HostNode::getFreePhysicalMemoryBit).sum()
                / hostNodes.stream().mapToLong(HostNode::getTotalMemoryBit).sum())
                * 100);
    }

    public void validate() {
        checkMigrateTask();
        checkSourceHost();
        checkTargetHost();
    }

    //迁移vm, 应该从高负载物理机迁移到低负载物理机
    private void checkMigrateTask() {
        for (MigrateTask task : tasks) {
            HostNode sourceHost = hostNodes.stream().filter(h ->  h.getUuid().equals(task.getSourceHostUuid())).findFirst().get();
            HostNode targetHost = hostNodes.stream().filter(h ->  h.getUuid().equals(task.getTargetHostUuid())).findFirst().get();

            if (this.cpuThreshold != null) {
                assert sourceHost.getUsedCPUPercent() > this.cpuThreshold;
                assert targetHost.getUsedCPUPercent() < this.cpuThreshold;
            }

            if (this.memThreshold != null) {
                assert sourceHost.getUsedMemoryPercent() > this.memThreshold;
                assert targetHost.getUsedMemoryPercent() < this.memThreshold;
            }
        }
    }

    //高负载物理机，迁移后负载不应该低于了平均
    private void checkSourceHost() {
        for (HostNode hostNode : hostNodes) {
            List<String> vmUuids = tasks.stream()
                    .filter(t -> t.sourceHostUuid.equals(hostNode.getUuid()))
                    .map(MigrateTask::getVmUuid).collect(Collectors.toList());
            if (vmUuids.isEmpty()) {
                continue;
            }

            if (this.cpuThreshold != null) {
                int migrate = getAllVmNodes().stream().filter(vm -> vmUuids.contains(vm.uuid)).mapToInt(VmNode::getUsedCPUPercent).sum();
                assert hostNode.getUsedCPUPercent() - averageCpuLoadPercent >= migrate;
            }

            if (this.memThreshold != null) {
                int migrate = getAllVmNodes().stream().filter(vm -> vmUuids.contains(vm.uuid)).mapToInt(VmNode::getUsedMemoryPercent).sum();
                assert hostNode.getUsedMemoryPercent() - averageMemoryLoadPercent >= migrate;
            }
        }
    }

    //低负载物理机，迁移后负载不应该高于阈值
    private void checkTargetHost() {
        Integer averageCpuLoadPercent = hostNodes.stream().mapToInt(HostNode::getUsedCPUPercent).sum() / hostNodes.size();

        for (HostNode hostNode : hostNodes) {
            List<String> vmUuids = tasks.stream()
                    .filter(t -> t.targetHostUuid.equals(hostNode.getUuid()))
                    .map(MigrateTask::getVmUuid).collect(Collectors.toList());
            if (vmUuids.isEmpty()) {
                continue;
            }

            if (this.cpuThreshold != null) {
                int migrate = getAllVmNodes().stream().filter(vm -> vmUuids.contains(vm.uuid)).mapToInt(VmNode::getUsedCPUPercent).sum();
                assert hostNode.getUsedCPUPercent() + migrate <= averageCpuLoadPercent;
            }

            if (this.memThreshold != null) {
                int migrate = getAllVmNodes().stream().filter(vm -> vmUuids.contains(vm.uuid)).mapToInt(VmNode::getUsedMemoryPercent).sum();
                assert hostNode.getUsedMemoryPercent() + migrate <= averageMemoryLoadPercent;
            }
        }
    }

    private List<VmNode> getAllVmNodes() {
        List<VmNode> allVmNodes = new ArrayList<>();
        hostNodes.forEach(h -> {
            allVmNodes.addAll(h.getVmList());
        });
        return allVmNodes;
    }
}
