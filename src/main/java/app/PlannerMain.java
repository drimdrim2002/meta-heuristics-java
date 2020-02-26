package app;

import common.XmlReader;
import domain.CloudBalance;
import domain.DomainAssociator;
import move.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;
import score.ScoreLong;
import solver.LahcSolver;

import java.util.List;

public class PlannerMain {

    private final static Logger logger = LoggerFactory.getLogger(PlannerMain.class);
    public static void main(String[] args) {

        logger.info("Plan Start");


        CloudBalance cloudBalance = XmlReader.createSolution();
        if(cloudBalance == null) {
            return;
        }

        // move를 정의
        List<AbstractMove> changeMoveList = CloudComputerChangeMoveFactory.createMoveList(cloudBalance);
        List<AbstractMove> swapMoveList = CloudProcessSwapFactory.createMoveList(cloudBalance);

        cloudBalance.setChangeMoveList(changeMoveList);
        cloudBalance.setSwapMoveList(swapMoveList);


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
