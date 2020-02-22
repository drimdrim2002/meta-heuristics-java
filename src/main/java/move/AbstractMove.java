package move;

import score.ScoreCalculator;

public abstract class AbstractMove {
    public abstract boolean isMoveDoable(ScoreCalculator scoreCalculator);
    public abstract AbstractMove doMove(ScoreCalculator scoreCalculator);
    protected abstract void doMoveOnGenuineVariables(ScoreCalculator scoreDirector);
    protected abstract AbstractMove createUndoMove(ScoreCalculator scoreCalculator);
    public abstract String toString();
}
