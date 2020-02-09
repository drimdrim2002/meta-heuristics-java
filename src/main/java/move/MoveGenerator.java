package move;

import common.RandomList;
import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import score.ScoreCalculator;

import java.util.List;
import java.util.Random;

public class MoveGenerator {


    public static AbstractMove getNextMove(ScoreCalculator scoreCalculator, Random random) {

        CloudBalance cloudBalance = scoreCalculator.getCloudBalance();
        List<CloudProcess> cloudProcessList = cloudBalance.getProcessList();
        List<CloudComputer> cloudComputerList = cloudBalance.getComputerList();
        //TODO : MOVE 선택하기
        int randomIndex = new Random().nextInt(2);

        RandomList<CloudProcess> randomProcessList = new RandomList<CloudProcess>(cloudProcessList);


        // change move
        if (randomIndex == 0) {
            CloudProcess randomProcess = randomProcessList.randomPick(random);

            // from, to computer 선택
            CloudComputer fromCloudComputer = randomProcess.getComputer();
            CloudComputer toCloudComputer = null;




            //random하게 computer 선택
            RandomList<CloudComputer> cloudComputerRandomList = new RandomList<>(cloudComputerList);
            do {

                CloudComputer randomComputer= cloudComputerRandomList.randomPick(random);
                if ( fromCloudComputer!= null && randomComputer.equals(fromCloudComputer)){
                    cloudComputerRandomList.remove(randomComputer);
                } else {
                    toCloudComputer = randomComputer;
//                    randomProcess.setComputer(randomComputer);
                    break;
                }

            }while(!cloudComputerRandomList.isEmpty());

            CloudComputerChangeMove changeMove = new CloudComputerChangeMove(randomProcess, fromCloudComputer, toCloudComputer);
            return changeMove;
            //원복하기

        } else {

            CloudProcess leftProcess = null;
            CloudProcess rightProcess = null;

            do {
                leftProcess = randomProcessList.randomPick(random);
                randomProcessList.remove(leftProcess);

                rightProcess = randomProcessList.randomPick(random);
                randomProcessList.remove(rightProcess);

                break;
            } while(!randomProcessList.isEmpty());

            CloudProcessSwapMove swapMove = new CloudProcessSwapMove(leftProcess, rightProcess);
            return swapMove;

        }
    }

}
