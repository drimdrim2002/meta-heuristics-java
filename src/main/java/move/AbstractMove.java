package move;

import score.ScoreCalculator;

import java.io.Serializable;

public abstract class AbstractMove implements Serializable {
    public abstract boolean isMoveDoable(ScoreCalculator scoreCalculator);
    public abstract AbstractMove doMove(ScoreCalculator scoreCalculator);
    protected abstract void doMoveOnGenuineVariables(ScoreCalculator scoreDirector);
    protected abstract AbstractMove createUndoMove(ScoreCalculator scoreCalculator);
    public abstract String toString();
}
