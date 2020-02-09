package score;

import java.util.*;

public class ScoreLong implements Comparable<ScoreLong>, Comparator<ScoreLong> {

    protected long[] scoreOrder;

    public ScoreLong(ScoreLong _scoreLong) {
        this.scoreOrder = new long[2];
        scoreOrder[0] = _scoreLong.getHardScore();
        scoreOrder[1] = _scoreLong.getSoftScore();
    }



    public ScoreLong(int hardScore, int softScore) {
        scoreOrder = new long[2];
        scoreOrder[0] = hardScore;
        scoreOrder[1] = softScore;
    }

    public void assign(ScoreLong _scoreLong) {
        this.scoreOrder = new long[2];
        scoreOrder[0] = _scoreLong.getHardScore();
        scoreOrder[1] = _scoreLong.getSoftScore();
    }


    @Override
    public String toString() {
        return "ScoreLong{" +
                "score_order=" + Arrays.toString(scoreOrder) +
                '}';
    }

    @Override
    public int compareTo(ScoreLong _scoreLong) {
     return compareToScoreOrder(this, _scoreLong);
    }

    @Override
    public int compare(ScoreLong _scoreLong1, ScoreLong _scoreLong2) {
      return this.compareToScoreOrder(_scoreLong1, _scoreLong2);
    }

    private int compareToScoreOrder (ScoreLong _scoreLong1, ScoreLong _scoreLong2) {
        for (int i = 0; i <_scoreLong1.scoreOrder.length; i ++) {
            if (_scoreLong1.scoreOrder[i] != _scoreLong2.scoreOrder[i]) {
                return  _scoreLong1.scoreOrder[i] > _scoreLong2.scoreOrder[i] ? 1 : -1;
            }
        }
        return 0;
    }

    public long getHardScore() {
        return scoreOrder[0];
    }

    public long getSoftScore() {return scoreOrder[1];}
}
