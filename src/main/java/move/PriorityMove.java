package move;

import score.ScoreLong;

import java.util.List;

public class PriorityMove implements Comparable<PriorityMove> {
    private ScoreLong scoreLong;
    private List<AbstractMove> moves;

    public PriorityMove(ScoreLong scoreLong, List<AbstractMove> moves) {
        super();
        this.scoreLong = scoreLong;
        this.moves = moves;
    }

    public List<AbstractMove> getMoves() {
        return moves;
    }

    @Override
    public int compareTo(PriorityMove priorityMove) {
        return this.scoreLong.compareTo(priorityMove.scoreLong) * -1 ;
    }
}
