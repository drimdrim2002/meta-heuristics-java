package score;

import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import move.AbstractMove;

import java.util.*;

public class ScoreCalculator {

    //
    private Map<CloudComputer, Integer> cpuPowerUsageMap;
    private Map<CloudComputer, Integer> memoryUsageMap;
    private Map<CloudComputer, Integer> networkBandwidthUsageMap;
    private Map<CloudComputer, Integer> processCountMap;


    public ScoreCalculator() {
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


    public void initialPlan(CloudBalance cloudBalance) {

        // 초기해를 구한다.

        Collections.sort(cloudBalance.getProcessList(), (left, right) -> {
            int leftCputPower = left.getRequiredCpuPower();
            int leftNetworkBandWidth = left.getRequiredNetworkBandwidth();
            int leftMemory = left.getRequiredMemory();

            int leftTotal = leftCputPower + leftNetworkBandWidth + leftMemory;

            int rightCputPower = right.getRequiredCpuPower();
            int rightNetworkdBandwidth = right.getRequiredNetworkBandwidth();
            int rightMemory = right.getRequiredMemory();

            int rightTotal = rightCputPower + rightNetworkdBandwidth + rightMemory;

            if (leftTotal == rightTotal) {
                return left.getId() > right.getId() ? 1 : -1;
            }

            return leftTotal > rightTotal ? -1 : 1;


        });
        for (CloudProcess cloudProcess : cloudBalance.getProcessList()) {

            TreeMap<ScoreLong, List<CloudComputer>> scoreRank = new TreeMap<ScoreLong, List<CloudComputer>>();
            for (CloudComputer cloudComputer : cloudBalance.getComputerList()) {
                cloudProcess.setComputer(cloudComputer);
                afterVariableChanged(cloudProcess);
                ScoreLong afterScore = calculateScore();
                beforeVariableChanged(cloudProcess);

                if (afterScore.getHardScore() >= 0) {

                    if (!scoreRank.containsKey(afterScore)) {
                        scoreRank.put(afterScore, new ArrayList<CloudComputer>());
                    }
                    scoreRank.get(afterScore).add(cloudComputer);
                }
                cloudProcess.setComputer(null);
            }

            if (!scoreRank.isEmpty()) {
                List<CloudComputer> cloudComputerList = scoreRank.lastEntry().getValue();
                int randomIndex = cloudBalance.randomSeed.nextInt(cloudComputerList.size());
                cloudProcess.setComputer(cloudComputerList.get(randomIndex));
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


    public void doAndProcessMove(AbstractMove move, TreeMap<ScoreLong, AbstractMove> moveScope) {

        ScoreLong beforeScore = calculateScore();
        AbstractMove undoMove = move.doMove(this);
        ScoreLong afterScore = calculateScore();

        moveScope.put(afterScore, move);
        undoMove.doMove(this);

        ScoreLong rollbackScore = calculateScore();

        //TODO : 안전성 입증되면 삭제 필요
        if (!beforeScore.equals(rollbackScore)) {
            throw new UnsupportedOperationException();
        }
    }


}
