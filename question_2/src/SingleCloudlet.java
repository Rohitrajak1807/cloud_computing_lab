import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.*;
import org.cloudbus.cloudsim.provisioners.*;

public class SingleCloudlet {
    public static void main(String[] args) {
        List<Cloudlet> cloudlets;
        List<Vm> vms;
        Log.printLine("Starting");

        try {
            int users = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;
            CloudSim.init(users, calendar, traceFlag);
            Datacenter datacenter0 = createDataCenter("datacenter0");

            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();
            vms = new ArrayList<>();
            int vmId = 0;
            int mips = 1000;
            long size = 10000;
            int ram = 512;
            long bandwidth = 1000;
            int peCount = 1;
            String vmm = "Xen";

            Vm vm = new Vm(vmId, brokerId, mips, peCount, ram, bandwidth, size, vmm, new CloudletSchedulerTimeShared());

            vms.add(vm);
            broker.submitVmList(vms);

            cloudlets = new ArrayList<>();

            int id = 0;
            long length = 400000;
            long fileSize = 300;
            long outputSize = 300;

            UtilizationModel utilizationModel = new UtilizationModelFull();

            Cloudlet cloudlet = new Cloudlet(
                    id,
                    length,
                    peCount,
                    fileSize,
                    outputSize,
                    utilizationModel,
                    utilizationModel,
                    utilizationModel
            );

            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(vmId);

            cloudlets.add(cloudlet);

            broker.submitCloudletList(cloudlets);

            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            List<Cloudlet> cloudletList = broker.getCloudletReceivedList();
            printCloudletList(cloudletList);
            Log.printLine("Ended");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + "Time" + indent
                + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId()
                        + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent
                        + dft.format(cloudlet.getActualCPUTime()) + indent
                        + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }
    }

    private static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return broker;
    }

    private static Datacenter createDataCenter(String name) {
        List<Host> hosts = new ArrayList<>();
        List<Pe> pes = new ArrayList<>();
        int mips = 1000;
        PeProvisionerSimple provisioner = new PeProvisionerSimple(mips);
        Pe pe = new Pe(0, provisioner);
        pes.add(pe);

        int hostId = 0;
        int ram = 1024;
        long storage = 1000000;
        int bandwidth = 10000;

        hosts.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bandwidth),
                        storage,
                        pes,
                        new VmSchedulerTimeShared(pes)
                )
        );

        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";

        double timeZone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;

        LinkedList<Storage> storages = new LinkedList<Storage>();

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hosts, timeZone, cost, costPerMem, costPerStorage, costPerBw
        );
        Datacenter dataCenter = null;

        try {
            dataCenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hosts), storages, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCenter;
    }

}
