package solver;

import common.EngineConfig;
import domain.CloudComputer;
import domain.CloudProcess;
import move.AbstractMove;
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
    private int scoreArraySize;
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
        for (int i = 0; i <= scoreArraySize; i++) {
            scoreArray[i] = new ScoreLong(currScore);
        }


        int nstep = 1;
        boolean accept;

        long calstartTime = System.currentTimeMillis();
        int elapsedTime = 0;
        int displayedTime = 0;
        long bestScoreTime = calstartTime;


        Map<CloudProcess, CloudComputer> backupBestScoreAnswer = new HashMap<CloudProcess, CloudComputer>();
        Random random =  new Random(1);



        do {

            // next Move의 score를 가져오기
            List<AbstractMove> moveList = MoveGenerator.getNextMove(scoreCalculator, random);


            for (AbstractMove move : moveList) {
                move.doMove(scoreCalculator);
            }


            ScoreLong nextScore = scoreCalculator.calculateScore();

            for (AbstractMove move : moveList) {
                move.undoMove(scoreCalculator);
            }


            ScoreLong prevSetpScore = scoreArray[nstep % scoreArraySize] ;


            // 다음 move가 현재보다 좋거나, 이전 setp보다 좋으면 accept
            accept = (nextScore.compareTo(currScore) >= 0 || nextScore.compareTo(prevSetpScore) >= 0);

            if(accept) {
                currScore.assign(nextScore);
                //accept하여 move를 받아들임
                for (AbstractMove move : moveList) {
                    move.doMove(scoreCalculator);
                }

            }

            if (accept && currScore.compareTo(prevSetpScore) >= 0) {
                prevSetpScore.assign(currScore);


            }


            long calEndTime = System.currentTimeMillis();
            int currentTime = (int) (calEndTime - calstartTime) / 1000;



            if (currScore.compareTo(bestScore) > 0) {
                bestScore.assign(currScore);

                //domove는 이미 되어있다고 본다.
                for (CloudProcess cloudProcess : scoreCalculator.getCloudBalance().getProcessList()) {
                    backupBestScoreAnswer.put(cloudProcess, cloudProcess.getComputer());
                }
                bestScoreTime = calEndTime;
            }

            //1초에 한번씩 display
            if(currentTime != displayedTime && currentTime % 1 == 0){
                displayedTime = currentTime;
                logger.info(String.format("%11d", nstep) + " best : " + bestScore.toString() + " time " + currentTime );
            }

            // 10초마다 한 번씩 검사
            if (currentTime != elapsedTime && currentTime % 10 == 0) {
                elapsedTime = currentTime;

                //30초 이후로 10초마다 score array size를 줄인다.
                if (elapsedTime >= 30) {
                    scoreArraySize = scoreArraySize/2;
                }

                // 최대 수행 시간 초과시 종료
                if (elapsedTime >= maxRunningTime) {
                    break;
                }
            }

            // 해 개선이 이루어지지 않으면 종료
            if ((calEndTime - bestScoreTime) / 1000 >= maxPermitIdleTime) {
                break;
            }
            nstep++;
        } while (true);


        //final score 처리

        logger.info("final score ==" + scoreCalculator.calculateScore());

        for (CloudProcess cloudProcess : scoreCalculator.getCloudBalance().getProcessList()) {
            CloudComputer cloudComputer = backupBestScoreAnswer.get(cloudProcess);
            cloudProcess.setComputer(cloudComputer);
        }


    }
}
