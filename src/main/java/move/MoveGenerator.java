package move;

import domain.CloudBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MoveGenerator {


    private final static Logger logger = LoggerFactory.getLogger(MoveGenerator.class);


    public static AbstractMove getNextMove( CloudBalance cloudBalance, HashSet<Integer> tried, ScoreCalculator scoreCalculator) {


        Random random =cloudBalance.randomSeed;
        List<AbstractMove> changeMoveList = cloudBalance.getChangeMoveList();
        List<AbstractMove> swapMoveList = cloudBalance.getSwapMoveList();

        //1. move type은 change, swap 2개
        int moveType = cloudBalance.randomSeed.nextInt(2);

        int randomIndex;
        AbstractMove move = null;
        // change move
        if (moveType == 0) {

            do {
                randomIndex =  getRandomIndex(random, changeMoveList, tried);
                if(!tried.contains(randomIndex)) {
                    tried.add(randomIndex);
                    move = changeMoveList.get(randomIndex);
                    if(move.isMoveDoable(scoreCalculator)){
                        break;
                    }
                }
            } while (true);
        } else {
            // swap move
            do {
                randomIndex = getRandomIndex(random, swapMoveList, tried);
                if(!tried.contains(changeMoveList.size() + randomIndex +1)) {
                    tried.add(changeMoveList.size() + randomIndex +1);
                    move = swapMoveList.get(randomIndex);
                    if(move.isMoveDoable(scoreCalculator)){
                        break;
                    }
                }
            } while (true);
        }
        return  move;
    }

    private static int getRandomIndex(Random random, List<AbstractMove> changeMoveList, HashSet<Integer> tried) {
        int randomIndex;
        do {
            randomIndex = random.nextInt(changeMoveList.size());
            if (!tried.contains(randomIndex)) {
                break;
            }
        } while (true);
        return randomIndex;
    }


}
