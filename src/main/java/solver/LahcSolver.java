package solver;

import app.PlannerMain;
import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;
import score.ScoreLong;

import java.util.TreeMap;

public class LahcSolver extends Solver {


    private final static Logger logger = LoggerFactory.getLogger(LahcSolver.class);
//    private final int maxRunningTime;
//    private final int maxPermitIdleTime;
//
//    private final int threadSize;
//    private final int scoreArraySize;
//    private final ScoreDouble[] scoreArray;
//    private final int foragarSize;
    private long nstep;


    @Override
    public void initialPlanning(ScoreCalculator scoreCalculator) {


    }

    @Override
    public void optimalPlanning(ScoreCalculator scoreCalculator) {

        //초기 해 표시
        ScoreLong currScore = scoreCalculator.calculateScore();
        logger.info("ini score : " + currScore);

        // 초기 해를 best score 로 저장
        ScoreLong bestScore = new ScoreLong(currScore);
//        for (int i = 0; i <= scoreArray.length; i++) {
//            scoreArray[i] = new ScoreDouble(currScore);
//        }
    }
}
