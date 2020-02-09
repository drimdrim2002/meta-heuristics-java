package move;

import common.RandomList;
import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import score.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoveGenerator {


    public static List<AbstractMove> getNextMove(ScoreCalculator scoreCalculator, Random random) {

        List<AbstractMove> moveList = new ArrayList<AbstractMove>();

        CloudBalance cloudBalance = scoreCalculator.getCloudBalance();
        List<CloudProcess> cloudProcessList = cloudBalance.getProcessList();
        List<CloudComputer> cloudComputerList = cloudBalance.getComputerList();
        //TODO : MOVE 선택하기
        int randomIndex = new Random().nextInt(2);

        // change move
        if (randomIndex == 0) {

            RandomList<CloudComputer> computerRandomList = new RandomList<CloudComputer>(cloudComputerList);

            List<CloudProcess> toRemoveProcessList = new ArrayList<>();
            do {
                CloudComputer toRemoveComputer = computerRandomList.randomPick(random);
                for (CloudProcess process : cloudProcessList) {
                    if (process.getComputer().equals(toRemoveComputer)) {
                        toRemoveProcessList.add(process);
                    }
                }
                computerRandomList.remove(toRemoveComputer);
                if (toRemoveProcessList.size() >= 1) {
                    break;
                }

            } while (!computerRandomList.isEmpty());

            RandomList <CloudProcess> randomProcessList = new RandomList<>(toRemoveProcessList);

            do {
                CloudProcess toMoveprocess = randomProcessList.randomPick(random);
                CloudComputer toMoveComputer = computerRandomList.randomPick(random);

                CloudComputerChangeMove changeMove = new CloudComputerChangeMove(toMoveprocess, toMoveprocess.getComputer(), toMoveComputer);
                moveList.add(changeMove);

                randomProcessList.remove(toMoveprocess);
            } while (!randomProcessList.isEmpty());


        } else {


            RandomList<CloudProcess> randomProcessList = new RandomList<>(cloudProcessList);

            do {
                CloudProcess leftProcess = randomProcessList.randomPick(random);
                randomProcessList.remove(leftProcess);

                CloudProcess rightProcess = randomProcessList.randomPick(random);
                randomProcessList.remove(rightProcess);

                CloudProcessSwapMove swapMove = new CloudProcessSwapMove(leftProcess, rightProcess);
                moveList.add(swapMove);
            } while (randomProcessList.size() >= 2);
        }

        return moveList;
    }

}
