package solver;

import domain.CloudBalance;
import score.ScoreCalculator;

public abstract class Solver {

    public abstract void initialPlanning(ScoreCalculator scoreCalculator);

    public abstract void optimalPlanning(ScoreCalculator scoreCalculator);
}
