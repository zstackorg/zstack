package drs.ut;

import drs.Balancer;
import drs.HostNode;
import drs.MigrateTask;
import drs.VmNode;
import org.junit.Test;

import java.util.*;

/**
 * Created by lining on 2019/11/28.
 */
public class TestDRSBalancer {
    @Test
    public void testDRSBalancerCPUThreshold() {
        List<HostNode> hostNodes = new ArrayList<>();
        HostNode hostNode = new HostNode(
                "host-1",
                80,
                20,
                1024 * 1024 * 1024,
                14,
                1024 * 1024 * 1024 * 10L,
                1024 * 1024 * 1024 * 20L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-1",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                50,
                                1024 * 1024 * 1024),
                        new VmNode(
                                "vm-2",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                20,
                                1024 * 1024 * 1024),
                        new VmNode(
                                "vm-3",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                10,
                                1024 * 1024 * 1024)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-2",
                30,
                70,
                1024 * 1024 * 1024,
                24,
                1024 * 1024 * 1024 * 10L,
                1024 * 1024 * 1024 * 20L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-4",
                                10,
                                1024 * 1024 * 1024 * 2L,
                                50,
                                1024 * 1024 * 1024),
                        new VmNode(
                                "vm-5",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                20,
                                1024 * 1024 * 1024),
                        new VmNode(
                                "vm-6",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                10,
                                1024 * 1024 * 1024)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-3",
                40,
                60,
                1024 * 1024 * 1024,
                24,
                1024 * 1024 * 1024 * 10L,
                1024 * 1024 * 1024 * 20L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-7",
                                10,
                                1024 * 1024 * 1024 * 2L,
                                10,
                                1024 * 1024 * 1024),
                        new VmNode(
                                "vm-8",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                20,
                                1024 * 1024 * 1024),
                        new VmNode(
                                "vm-9",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                5,
                                1024 * 1024 * 1024),
                        new VmNode(
                                "vm-10",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                5,
                                1024 * 1024 * 1024)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-4",
                10,
                90,
                1024 * 1024 * 1024,
                300,
                1024 * 1024 * 1024 * 20L,
                1024 * 1024 * 1024 * 20L,
                new LinkedList<>()
        );
        hostNodes.add(hostNode);

        List<MigrateTask> tasks = new Balancer().makeTasks(hostNodes,
                50, null);
        assert tasks != null;
        assert !tasks.isEmpty();

        new DRSBalancerValidator(hostNodes, tasks, 50, null).validate();
    }

    @Test
    public void testDRSBalancerMemoryThreshold() {
        List<HostNode> hostNodes = new ArrayList<>();
        HostNode hostNode = new HostNode(
                "host-1",
                80,
                20,
                3 * 1024 * 1024 * 1024L,
                14,
                10 * 1024 * 1024 * 1024L,
                20 * 1024 * 1024 * 1024L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-1",
                                5,
                                3 * 1024 * 1024 * 1024L,
                                50,
                                3L * 1024 * 1024 * 1024L,
                                15),
                        new VmNode(
                                "vm-2",
                                5,
                                5 * 1024 * 1024 * 1024L,
                                20,
                                5L * 1024 * 1024 * 1024,
                                20),
                        new VmNode(
                                "vm-3",
                                5,
                                9 * 1024 * 1024 * 1024L,
                                10,
                                9 * 1024 * 1024 * 1024L,
                                45)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-2",
                30,
                70,
                5 * 1024 * 1024 * 1024L,
                24,
                1024 * 1024 * 1024 * 10L,
                20 * 1024 * 1024 * 1024L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-4",
                                10,
                                1024 * 1024 * 1024 * 2L,
                                50,
                                1024 * 1024 * 1024,
                                5),
                        new VmNode(
                                "vm-5",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                20,
                                1024 * 1024 * 1024,
                                5),
                        new VmNode(
                                "vm-6",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                10,
                                13 * 1024 * 1024 * 1024L,
                                65)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-3",
                40,
                60,
                12 * 1024 * 1024 * 1024L,
                24,
                1024 * 1024 * 1024 * 10L,
                20 * 1024 * 1024 * 1024L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-7",
                                10,
                                1024 * 1024 * 1024 * 2L,
                                10,
                                3 * 1024 * 1024 * 1024L,
                                15),
                        new VmNode(
                                "vm-8",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                20,
                                2 * 1024 * 1024 * 1024L,
                                10),
                        new VmNode(
                                "vm-9",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                5,
                                1024 * 1024 * 1024,
                                5),
                        new VmNode(
                                "vm-10",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                5,
                                1024 * 1024 * 1024,
                                5)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-4",
                10,
                90,
                20 * 1024 * 1024 * 1024L,
                300,
                20 * 1024 * 1024 * 1024L,
                20 * 1024 * 1024 * 1024L,
                new LinkedList<>()
        );
        hostNodes.add(hostNode);

        List<MigrateTask> tasks = new Balancer().makeTasks(hostNodes,
                null, 50);
        assert tasks != null;
        assert !tasks.isEmpty();

        new DRSBalancerValidator(hostNodes, tasks, null, 50).validate();
    }

    @Test
    public void testDRSBalancerMemoryAndCPUThreshold() {
        List<HostNode> hostNodes = new ArrayList<>();
        HostNode hostNode = new HostNode(
                "host-1",
                80,
                20,
                3 * 1024 * 1024 * 1024L,
                14,
                10 * 1024 * 1024 * 1024L,
                20 * 1024 * 1024 * 1024L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-1",
                                5,
                                3 * 1024 * 1024 * 1024L,
                                50,
                                3L * 1024 * 1024 * 1024L,
                                15),
                        new VmNode(
                                "vm-2",
                                5,
                                5 * 1024 * 1024 * 1024L,
                                20,
                                5L * 1024 * 1024 * 1024,
                                20),
                        new VmNode(
                                "vm-3",
                                5,
                                9 * 1024 * 1024 * 1024L,
                                10,
                                9 * 1024 * 1024 * 1024L,
                                45)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-2",
                30,
                70,
                5 * 1024 * 1024 * 1024L,
                24,
                1024 * 1024 * 1024 * 10L,
                20 * 1024 * 1024 * 1024L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-4",
                                10,
                                1024 * 1024 * 1024 * 2L,
                                50,
                                1024 * 1024 * 1024,
                                5),
                        new VmNode(
                                "vm-5",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                20,
                                1024 * 1024 * 1024,
                                5),
                        new VmNode(
                                "vm-6",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                10,
                                13 * 1024 * 1024 * 1024L,
                                65)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-3",
                40,
                60,
                12 * 1024 * 1024 * 1024L,
                24,
                1024 * 1024 * 1024 * 10L,
                20 * 1024 * 1024 * 1024L,
                new LinkedList<>(Arrays.asList(
                        new VmNode(
                                "vm-7",
                                10,
                                1024 * 1024 * 1024 * 2L,
                                10,
                                3 * 1024 * 1024 * 1024L,
                                15),
                        new VmNode(
                                "vm-8",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                20,
                                2 * 1024 * 1024 * 1024L,
                                10),
                        new VmNode(
                                "vm-9",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                5,
                                1024 * 1024 * 1024,
                                5),
                        new VmNode(
                                "vm-10",
                                5,
                                1024 * 1024 * 1024 * 2L,
                                5,
                                1024 * 1024 * 1024,
                                5)
                ))
        );
        hostNodes.add(hostNode);

        hostNode = new HostNode(
                "host-4",
                10,
                90,
                20 * 1024 * 1024 * 1024L,
                300,
                20 * 1024 * 1024 * 1024L,
                20 * 1024 * 1024 * 1024L,
                new LinkedList<>()
        );
        hostNodes.add(hostNode);

        List<MigrateTask> tasks = new Balancer().makeTasks(hostNodes,
                50, 50);
        assert tasks != null;
        assert !tasks.isEmpty();

        new DRSBalancerValidator(hostNodes, tasks, 50, 50).validate();
    }

    public long toByte(int GB) {
        return GB * 1024 * 1024 * 1024;
    }
}

