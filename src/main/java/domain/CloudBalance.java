package domain;

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
            logger.info("   Process " + process.getId()  + "==> " + process.getComputer().getId());

        }
    }

}
