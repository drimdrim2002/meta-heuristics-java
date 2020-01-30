package domain;

import score.ScoreCalculator;
import score.ScoreLong;

import java.io.Serializable;
import java.util.List;

public class CloudBalance implements Serializable {

    //나중에 멀티쓰레드에서 필요
    private int id ;
    private List<CloudComputer> computerList;
    private List<CloudProcess> processList;
    private ScoreCalculator scoreCalculator;

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
        this.id =id;
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

}
