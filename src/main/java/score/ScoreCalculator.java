package score;

import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ScoreCalculator {
    private Map<CloudComputer, Integer> cpuPowerUsageMap;
    private Map<CloudComputer, Integer> memoryUsageMap;
    private Map<CloudComputer, Integer> networkBandwidthUsageMap;
    private Map<CloudComputer, Integer> processCountMap;


    public CloudBalance getCloudBalance() {
        return cloudBalance;
    }

    private CloudBalance cloudBalance;

    public ScoreCalculator(CloudBalance cloudBalance) {
        this.cloudBalance = cloudBalance;
    }

    private int hardScore;
    private int softScore;

    public void resetWorkingSolution(CloudBalance cloudBalance) {
        int computerListSize = cloudBalance.getComputerList().size();
        cpuPowerUsageMap = new HashMap<>(computerListSize);
        memoryUsageMap = new HashMap<>(computerListSize);
        networkBandwidthUsageMap = new HashMap<>(computerListSize);
        processCountMap = new HashMap<>(computerListSize);

        for (CloudComputer computer : cloudBalance.getComputerList()) {
            cpuPowerUsageMap.put(computer, 0);
            memoryUsageMap.put(computer, 0);
            networkBandwidthUsageMap.put(computer, 0);
            processCountMap.put(computer, 0);
        }
        hardScore = 0;
        softScore = 0;
        for (CloudProcess process : cloudBalance.getProcessList()) {
            insert(process);
        }
    }


    public void initialPlan() {

        // 초기해를 구한다.
        for (CloudProcess cloudProcess : cloudBalance.getProcessList()) {

            TreeMap<ScoreLong, CloudComputer> scoreRank = new TreeMap<ScoreLong, CloudComputer>();
            for (CloudComputer cloudComputer : cloudBalance.getComputerList()) {
                cloudProcess.setComputer(cloudComputer);
                afterVariableChanged(cloudProcess);
                ScoreLong afterScore = calculateScore();
                beforeVariableChanged(cloudProcess);

                if (afterScore.getHardScore() >= 0) {
                    scoreRank.put(afterScore, cloudComputer);
                }
                cloudProcess.setComputer(null);
            }

            if (!scoreRank.isEmpty()) {
                CloudComputer cloudComputer = scoreRank.lastEntry().getValue();
                cloudProcess.setComputer(cloudComputer);
                afterVariableChanged(cloudProcess);
            }
        }

    }


    private void insert(CloudProcess process) {

        CloudComputer computer = process.getComputer();
        if (computer != null) {
            int cpuPower = computer.getCpuPower();
            int oldCpuPowerUsage = cpuPowerUsageMap.get(computer);
            int oldCpuPowerAvailable = cpuPower - oldCpuPowerUsage;
            int newCpuPowerUsage = oldCpuPowerUsage + process.getRequiredCpuPower();
            int newCpuPowerAvailable = cpuPower - newCpuPowerUsage;
            hardScore += Math.min(newCpuPowerAvailable, 0) - Math.min(oldCpuPowerAvailable, 0);
            cpuPowerUsageMap.put(computer, newCpuPowerUsage);

            int memory = computer.getMemory();
            int oldMemoryUsage = memoryUsageMap.get(computer);
            int oldMemoryAvailable = memory - oldMemoryUsage;
            int newMemoryUsage = oldMemoryUsage + process.getRequiredMemory();
            int newMemoryAvailable = memory - newMemoryUsage;
            hardScore += Math.min(newMemoryAvailable, 0) - Math.min(oldMemoryAvailable, 0);
            memoryUsageMap.put(computer, newMemoryUsage);

            int networkBandwidth = computer.getNetworkBandwidth();
            int oldNetworkBandwidthUsage = networkBandwidthUsageMap.get(computer);
            int oldNetworkBandwidthAvailable = networkBandwidth - oldNetworkBandwidthUsage;
            int newNetworkBandwidthUsage = oldNetworkBandwidthUsage + process.getRequiredNetworkBandwidth();
            int newNetworkBandwidthAvailable = networkBandwidth - newNetworkBandwidthUsage;
            hardScore += Math.min(newNetworkBandwidthAvailable, 0) - Math.min(oldNetworkBandwidthAvailable, 0);
            networkBandwidthUsageMap.put(computer, newNetworkBandwidthUsage);

            int oldProcessCount = processCountMap.get(computer);
            if (oldProcessCount == 0) {
                softScore -= computer.getCost();
            }
            int newProcessCount = oldProcessCount + 1;
            processCountMap.put(computer, newProcessCount);

            computer.getAvailProcessList().add(process);
        }

    }

    private void retract(CloudProcess process) {
        CloudComputer computer = process.getComputer();
        if (computer != null) {
            int cpuPower = computer.getCpuPower();
            int oldCpuPowerUsage = cpuPowerUsageMap.get(computer);
            int oldCpuPowerAvailable = cpuPower - oldCpuPowerUsage;
            int newCpuPowerUsage = oldCpuPowerUsage - process.getRequiredCpuPower();
            int newCpuPowerAvailable = cpuPower - newCpuPowerUsage;
            hardScore += Math.min(newCpuPowerAvailable, 0) - Math.min(oldCpuPowerAvailable, 0);
            cpuPowerUsageMap.put(computer, newCpuPowerUsage);

            int memory = computer.getMemory();
            int oldMemoryUsage = memoryUsageMap.get(computer);
            int oldMemoryAvailable = memory - oldMemoryUsage;
            int newMemoryUsage = oldMemoryUsage - process.getRequiredMemory();
            int newMemoryAvailable = memory - newMemoryUsage;
            hardScore += Math.min(newMemoryAvailable, 0) - Math.min(oldMemoryAvailable, 0);
            memoryUsageMap.put(computer, newMemoryUsage);

            int networkBandwidth = computer.getNetworkBandwidth();
            int oldNetworkBandwidthUsage = networkBandwidthUsageMap.get(computer);
            int oldNetworkBandwidthAvailable = networkBandwidth - oldNetworkBandwidthUsage;
            int newNetworkBandwidthUsage = oldNetworkBandwidthUsage - process.getRequiredNetworkBandwidth();
            int newNetworkBandwidthAvailable = networkBandwidth - newNetworkBandwidthUsage;
            hardScore += Math.min(newNetworkBandwidthAvailable, 0) - Math.min(oldNetworkBandwidthAvailable, 0);
            networkBandwidthUsageMap.put(computer, newNetworkBandwidthUsage);

            int oldProcessCount = processCountMap.get(computer);
            int newProcessCount = oldProcessCount - 1;
            if (newProcessCount == 0) {
                softScore += computer.getCost();
            }
            processCountMap.put(computer, newProcessCount);

            computer.getAvailProcessList().remove(process);
        }
    }

    public ScoreLong calculateScore() {
        return new ScoreLong(hardScore, softScore);
    }

    public void beforeVariableChanged(CloudProcess cloudProcess) {
        retract(cloudProcess);
    }

    public void afterVariableChanged(CloudProcess cloudProcess) {
        insert(cloudProcess);
    }


}
