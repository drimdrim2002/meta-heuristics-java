package move;

import score.ScoreCalculator;

public abstract class AbstractMove {
    public abstract boolean isMoveDoable(ScoreCalculator scoreCalculator);
    public abstract void doMove(ScoreCalculator scoreCalculator);
    public abstract void undoMove(ScoreCalculator scoreCalculator);
    public abstract String toString();
}
