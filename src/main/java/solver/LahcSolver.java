package solver;

import common.EngineConfig;
import common.RandomList;
import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import move.AbstractMove;
import move.CloudComputerChangeMove;
import move.MoveGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;
import score.ScoreLong;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LahcSolver extends Solver {


    private final static Logger logger = LoggerFactory.getLogger(LahcSolver.class);
    private final ScoreCalculator scoreCalculator;
    private final int scoreArraySize;
    private final ScoreLong[] scoreArray;
    private final int foragarSize;
    private final int maxRunningTime;
    private final int maxPermitIdleTime;


    public LahcSolver(ScoreCalculator scoreCalculator) {
        this.scoreCalculator = scoreCalculator;
        this.scoreArraySize = EngineConfig.SCORE_ARRAY_SIZE;
        this.scoreArray = new ScoreLong[scoreArraySize + 1];
        this.foragarSize = EngineConfig.FORAGAR_SIZE;
        this.maxRunningTime = EngineConfig.MAX_RUNNING_TIME;
        this.maxPermitIdleTime = EngineConfig.MAX_PERMIT_IDLE_TIME;
    }


    @Override
    public void optimalPlanning(ScoreCalculator scoreCalculator) {

        ScoreLong currScore = scoreCalculator.calculateScore();
        logger.info("ini score : " + currScore);

        ScoreLong bestScore = new ScoreLong(currScore);

        // 초기 해를 best score 로 저장
        for (int i = 1; i <= scoreArraySize; i++) {
            scoreArray[i] = new ScoreLong(currScore);
        }


        int nstep = 1;
        boolean accept;

        long calstartTime = System.currentTimeMillis();
        int maxTime = 0;
        int displayTerm = 0;
        long bestScoreTime = calstartTime;


        Map<CloudProcess, CloudComputer> backupBestScoreAnswer = new HashMap<CloudProcess, CloudComputer>();
        Random random =  new Random(1);



        do {

            // next Move의 score를 가져오기
            AbstractMove move = MoveGenerator.getNextMove(scoreCalculator, random);

            logger.info(move.toString());

            move.doMove(scoreCalculator);
            scoreCalculator.getCloudBalance().showPlans();


            ScoreLong nextScore = scoreCalculator.calculateScore();
            move.undoMove(scoreCalculator);
            scoreCalculator.getCloudBalance().showPlans();
            ScoreLong prevSetpScore = scoreArray[nstep % scoreArraySize] ;


            // 다음 move가 현재보다 좋거나, 이전 setp보다 좋으면 accept
            accept = (nextScore.compareTo(currScore) >= 0 || nextScore.compareTo(prevSetpScore) >= 0);


            if (accept && currScore.compareTo(prevSetpScore) >= 0) {
                prevSetpScore.assign(currScore);
                move.doMove(scoreCalculator);

                //accept하여 move를 받아들임
            }


            long calEndTime = System.currentTimeMillis();
            int calPeriod = (int) (calEndTime - calstartTime) / 1000;


            if (currScore.compareTo(bestScore) > 0) {
                bestScore.assign(currScore);


                //domove는 이미 되어있다고 본다.
                for (CloudProcess cloudProcess : scoreCalculator.getCloudBalance().getProcessList()) {
                    backupBestScoreAnswer.put(cloudProcess, cloudProcess.getComputer());
                }
                logger.info("score recorded!!" + scoreCalculator.calculateScore());
                bestScoreTime = calEndTime;
            }

            if (calPeriod != maxTime && calPeriod % 10 == 0) {
                maxTime = calPeriod;

                if (maxTime >= maxRunningTime) {
                    break;
                }
            }

//            if ((calEndTime - bestScoreTime) / 1000 >= maxPermitIdleTime) {
//                break;
//            }

        } while (true);


        //final score 처리

        logger.info("final score ==" + scoreCalculator.calculateScore());

        for (CloudProcess cloudProcess : scoreCalculator.getCloudBalance().getProcessList()) {
            CloudComputer cloudComputer = backupBestScoreAnswer.get(cloudProcess);
            cloudProcess.setComputer(cloudComputer);
        }


    }
}
