package app;

import common.XmlReader;
import domain.CloudBalance;
import domain.DomainAssociator;
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
        if(cloudBalance == null) {
            return;
        }

        // score calculator는 cloudbalance에 종속
        ScoreCalculator scoreCalculator = new ScoreCalculator();
        scoreCalculator.resetWorkingSolution(cloudBalance);

        // Domain associate
        DomainAssociator.associate(cloudBalance);

        // initial planning
        scoreCalculator.initialPlan(cloudBalance);
        ScoreLong score = scoreCalculator.calculateScore();
        logger.info("initial score ==" + score.toString());


        cloudBalance.showPlans();

        // optimal planning
        LahcSolver lahcSolver = new LahcSolver(cloudBalance, scoreCalculator);
        lahcSolver.optimalPlanning();

    }
}
