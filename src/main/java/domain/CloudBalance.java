package domain;

import move.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;
import score.ScoreLong;
import solver.LahcSolver;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CloudBalance implements Serializable {

    private List<CloudComputer> computerList;
    private List<CloudProcess> processList;


    private List<AbstractMove> changeMoveList;
    public List<AbstractMove> getChangeMoveList() {
        return changeMoveList;
    }
    public void setChangeMoveList(List<AbstractMove> changeMoveList) {
        this.changeMoveList = changeMoveList;
    }

    private List<AbstractMove> swapMoveList;
    public List<AbstractMove> getSwapMoveList() {
        return swapMoveList;
    }
    public void setSwapMoveList(List<AbstractMove> swapMoveList) {
        this.swapMoveList = swapMoveList;
    }

    public void setScoreCalculator(ScoreCalculator scoreCalculator) {
        this.scoreCalculator = scoreCalculator;
    }

    public ScoreCalculator getScoreCalculator() {
        return scoreCalculator;
    }

    public final static Random randomSeed = new Random(0);

    private ScoreCalculator scoreCalculator;
    private final static Logger logger = LoggerFactory.getLogger(CloudBalance.class);



    //멀티쓰레드 적용을 위해 설정
    private int id;

    public void setScore(ScoreLong score) {
        this.score = score;
    }

    private ScoreLong score;

    public ScoreLong getScore() {
        return score;
    }

    public CloudBalance() {
    }

    @Override
    public String toString() {
        return "CloudBalance{" +
                "id=" + id +
                '}';
    }

    public CloudBalance(int id, List<CloudComputer> computerList, List<CloudProcess> processList) {
        this.id = id;
        this.computerList = computerList;
        this.processList = processList;
    }

    // problem fact
    public List<CloudComputer> getComputerList() {
        return computerList;
    }

    public void setComputerList(List<CloudComputer> computerList) {
        this.computerList = computerList;
    }

    // planning entity
    public List<CloudProcess> getProcessList() {
        return processList;
    }

    public void setProcessList(List<CloudProcess> processList) {
        this.processList = processList;
    }


    public void showPlans() {
        logger.info("show Plans");
        for (CloudProcess process : processList) {
            if (process.getComputer() != null) {
                logger.info("   Process " + process.getId()  + "==> " + process.getComputer().getId());
            }

        }
    }

}
