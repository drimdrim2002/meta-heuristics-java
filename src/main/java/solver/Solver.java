package solver;

import domain.CloudBalance;
import score.ScoreCalculator;

public abstract class Solver {

    

    public abstract void optimalPlanning(ScoreCalculator scoreCalculator);
}
