package move;

import common.RandomList;
import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;
import score.ScoreLong;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoveGenerator {



    private final static Logger logger = LoggerFactory.getLogger(MoveGenerator.class);


    public static List<AbstractMove> getNextMove(ScoreCalculator scoreCalculator, Random random) {
        List<AbstractMove> moveList = new ArrayList<AbstractMove>();

        CloudBalance cloudBalance = scoreCalculator.getCloudBalance();
        List<CloudProcess> cloudProcessList = cloudBalance.getProcessList();
        List<CloudComputer> cloudComputerList = cloudBalance.getComputerList();
        //TODO : MOVE 선택하기
        int moveType = new Random().nextInt(3);
//        int moveType = 0;


        // change move
        if (moveType == 0) {

            // random하게 computer select
            RandomList<CloudComputer> computerRandomList = new RandomList<CloudComputer>(cloudComputerList);
            CloudComputer beforeComputer = null;
            do {
                beforeComputer = computerRandomList.randomPick(random);
                if (!beforeComputer.getAvailProcessList().isEmpty()) {
                    break;
                }
            } while(true);



            List<CloudProcess> toRemoveProcessList = new ArrayList<>();

            do {
                CloudProcess process = beforeComputer.getAvailProcessList().randomPick(random);
                toRemoveProcessList.add(process);
                beforeComputer.getAvailProcessList().remove(process);

            } while (!beforeComputer.getAvailProcessList().isEmpty());

            for (CloudProcess backupProcess : toRemoveProcessList) {
                beforeComputer.getAvailProcessList().add(backupProcess);
            }

            if(toRemoveProcessList.isEmpty()) {
                throw new UnsupportedOperationException();
            }


            RandomList<CloudProcess> randomProcessList = new RandomList<>(toRemoveProcessList);

            int moveSize = new Random().nextInt(randomProcessList.size() );
            moveSize += 1;
//            int moveSize =1;
            if (moveSize == 0) {
                throw new UnsupportedOperationException();
            }

            do {

                CloudComputer afterComputer = computerRandomList.randomPick(random);
                if (beforeComputer.equals(afterComputer)) {
                    continue;
                }

                CloudProcess randomProcess = randomProcessList.randomPick(random);
//                if (!toMoveprocess.getComputer().equals(toRemoveComputer)) {
//                    throw new UnsupportedOperationException();
//                }

                if (randomProcess == null) {
                    throw new UnsupportedOperationException();
                }

                if (afterComputer == null) {
                    throw new UnsupportedOperationException();
                }

                CloudComputerChangeMove changeMove = new CloudComputerChangeMove(randomProcess,
                        randomProcess.getComputer(), afterComputer);
                moveList.add(changeMove);

                randomProcessList.remove(randomProcess);
                moveSize--;
//                break;
            } while (moveSize > 0);


        } else if (moveType == 1) {


            RandomList<CloudProcess> randomProcessList = new RandomList<>(cloudProcessList);
//            int moveSize = new Random().nextInt(randomProcessList.size()) +1;
            int moveSize = 2;
            do {
                CloudProcess leftProcess = randomProcessList.randomPick(random);
                randomProcessList.remove(leftProcess);
                CloudProcess rightProcess = randomProcessList.randomPick(random);
                randomProcessList.remove(rightProcess);

                if (leftProcess.equals(rightProcess)) {
                    randomProcessList.add(leftProcess);
                    randomProcessList.add(rightProcess);
                    continue;
                }


                if (leftProcess.getComputer() != null && rightProcess.getComputer() != null) {
                    if (leftProcess.getComputer().equals(rightProcess.getComputer())) {
                        randomProcessList.add(leftProcess);
                        randomProcessList.add(rightProcess);
                        continue;
                    }
                }

                moveSize -= 2;


                CloudProcessSwapMove swapMove = new CloudProcessSwapMove(leftProcess, rightProcess);
                moveList.add(swapMove);
//                break;

            } while (moveSize >= 2);
        } else {

            RandomList<CloudProcess> candidate = new RandomList<>(cloudProcessList);

            CloudProcess startProcess = candidate.randomPick(random);
            moveList = recursiveSearch(startProcess, moveList, candidate, scoreCalculator);


        }

        return moveList;
    }


    private static List<AbstractMove> recursiveSearch(CloudProcess cloudProcess, List<AbstractMove> moveList,
                                                      RandomList<CloudProcess> candidate,
                                                      ScoreCalculator scoreCalculator) {

//        logger.info("Random pick process => " + cloudProcess);
        //1. remove process from candidate
        candidate.remove(cloudProcess);

        //2. define before and after computer
        CloudComputer beforeComputer = cloudProcess.getComputer();
        CloudComputer afterComputer = getNextComputer(beforeComputer, scoreCalculator, new Random());

//        logger.info("before computer ==> " + beforeComputer.toString());
//        logger.info("after computer ==> " + afterComputer.toString());

        ScoreLong beforeScore = scoreCalculator.calculateScore();
        CloudComputerChangeMove changeMove = new CloudComputerChangeMove(cloudProcess, beforeComputer, afterComputer);
        changeMove.doMove(scoreCalculator);

        ScoreLong afterSCore = scoreCalculator.calculateScore();
        changeMove.undoMove(scoreCalculator);


        moveList.add(changeMove);

        // 만일 더 안좋아졌으면...
        if (beforeScore.compareTo(afterSCore) > 0) {
            CloudProcess newProces = afterComputer.getAvailProcessList().randomPick(new Random());
            if (newProces != null) {
                recursiveSearch(newProces, moveList, candidate, scoreCalculator);
            }
        }
        return moveList;
    }


    private static CloudComputer getNextComputer(CloudComputer befereComputer, ScoreCalculator scoreCalculator, Random random) {

        CloudComputer afterComputer = null;
        RandomList<CloudComputer> computers = new RandomList<CloudComputer>(scoreCalculator.getCloudBalance().getComputerList());

        do  {
            afterComputer = computers.randomPick(random);
            computers.remove(afterComputer);
            if (!befereComputer.equals(afterComputer)){
                break;
            }
        }while (computers.size() > 0);


        return afterComputer;
    }


}
