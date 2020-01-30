package app;

import common.XmlReader;
import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;
import score.ScoreLong;
import solver.LahcSolver;

import java.util.Map;
import java.util.TreeMap;

public class PlannerMain {

    private final static Logger logger = LoggerFactory.getLogger(PlannerMain.class);
    public static void main(String[] args) {

        logger.info("Plan Start");


        CloudBalance cloudBalance = XmlReader.createSolution();


        ScoreCalculator scoreCalculator = new ScoreCalculator(cloudBalance);
        scoreCalculator.resetWorkingSolution(cloudBalance);

        logger.info("initial score ==" + scoreCalculator.calculateScore());


        CloudBalance cloudBalance = scoreCalculator.getCloudBalance();

        // 초기해를 구한다.
        for (CloudProcess cloudProcess : cloudBalance.getProcessList()) {

            TreeMap<ScoreLong, CloudComputer> scoreRank = new TreeMap<ScoreLong, CloudComputer>();
            for (CloudComputer cloudComputer : cloudBalance.getComputerList()) {
                cloudProcess.setComputer(cloudComputer);
                scoreCalculator.afterVariableChanged(cloudProcess);
                ScoreLong afterScore = scoreCalculator.calculateScore();
                scoreCalculator.beforeVariableChanged(cloudProcess);

                if (afterScore.getHardScore() >= 0) {
                    scoreRank.put(afterScore, cloudComputer);
                }

                cloudProcess.setComputer(null);
            }

            if (!scoreRank.isEmpty()) {
                CloudComputer cloudComputer = scoreRank.lastEntry().getValue();
                cloudProcess.setComputer(cloudComputer);
                scoreCalculator.afterVariableChanged(cloudProcess);
                logger.info("cloud process = " + cloudProcess.toString());
                logger.info("   cloud computer = " + cloudComputer.toString());
                logger.info("       Commit score = " + scoreCalculator.calculateScore());
            }


        }

        ScoreLong score = scoreCalculator.calculateScore();
        logger.info("score ==" + score.toString());


    }
}
