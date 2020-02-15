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
        int randomIndex = new Random().nextInt(1);

//        if (randomIndex == 0 ){
//            System.out.println("Change");
//        } else {
//            System.out.println("swap");
//        }

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

            int moveSize = new Random().nextInt(randomProcessList.size());
            CloudComputer toMoveComputer = computerRandomList.randomPick(random);
            do {
                CloudProcess toMoveprocess = randomProcessList.randomPick(random);


                CloudComputerChangeMove changeMove = new CloudComputerChangeMove(toMoveprocess, toMoveprocess.getComputer(), toMoveComputer);
                moveList.add(changeMove);

                randomProcessList.remove(toMoveprocess);
                moveSize--;
//                break;
            } while ( moveSize >=0);


        } else {


            RandomList<CloudProcess> randomProcessList = new RandomList<>(cloudProcessList);
//            int moveSize = new Random().nextInt(randomProcessList.size());
            int moveSize = 30;
            do {
                CloudProcess leftProcess = randomProcessList.randomPick(random);
                randomProcessList.remove(leftProcess);
                CloudProcess rightProcess = randomProcessList.randomPick(random);
                randomProcessList.remove(rightProcess);

                if (leftProcess.equals(rightProcess)){
                    randomProcessList.add(leftProcess);
                    randomProcessList.add(rightProcess);
                    continue;
                }


                if (leftProcess.getComputer() != null && rightProcess.getComputer() != null){
                    if (leftProcess.getComputer().equals(rightProcess.getComputer())) {
                        randomProcessList.add(leftProcess);
                        randomProcessList.add(rightProcess);
                        continue;
                    }
                }

                moveSize -=2;


                CloudProcessSwapMove swapMove = new CloudProcessSwapMove(leftProcess, rightProcess);
                moveList.add(swapMove);
//                break;

            } while (moveSize >= 2);
        }

        return moveList;
    }

}
