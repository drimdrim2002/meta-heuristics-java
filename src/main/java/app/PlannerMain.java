package app;

import common.XmlReader;
import domain.CloudBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;
import score.ScoreLong;
import solver.LahcSolver;

public class PlannerMain {

    private final static Logger logger = LoggerFactory.getLogger(PlannerMain.class);
    public static void main(String[] args) {

        logger.info("Plan Start");


        CloudBalance cloudBalance = XmlReader.createSolution();


        ScoreCalculator scoreCalculator = new ScoreCalculator(cloudBalance);
        scoreCalculator.resetWorkingSolution(cloudBalance);

        // initial planning
        scoreCalculator.initialPlan();
        ScoreLong score = scoreCalculator.calculateScore();
        logger.info("initial score ==" + score.toString());


        // optimal planning
        LahcSolver lahcSolver = new LahcSolver(scoreCalculator);
        lahcSolver.optimalPlanning(scoreCalculator);

    }
}
