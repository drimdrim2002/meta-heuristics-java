package solver;

import common.EngineConfig;
import common.RandomList;
import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import move.AbstractMove;
import move.CloudComputerChangeMoveFactory;
import move.CloudProcessSwapFactory;
import move.MoveGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import score.ScoreCalculator;
import score.ScoreLong;

import java.util.*;

public class LahcSolver extends Solver {


    private final static Logger logger = LoggerFactory.getLogger(LahcSolver.class);
    private final CloudBalance cloudBalance;
    private final ScoreCalculator scoreCalculator;
    private int scoreArraySize;
    private final ScoreLong[] scoreArray;
    private final int foragerSize;
    private final int maxRunningTime;
    private final int maxPermitIdleTime;


    public LahcSolver(CloudBalance cloudBalance, ScoreCalculator scoreCalculator) {
        this.cloudBalance = cloudBalance;
        this.scoreCalculator = scoreCalculator;
        this.scoreArraySize = EngineConfig.SCORE_ARRAY_SIZE;
        this.scoreArray = new ScoreLong[scoreArraySize + 1];
        this.maxRunningTime = EngineConfig.MAX_RUNNING_TIME;
        this.maxPermitIdleTime = EngineConfig.MAX_PERMIT_IDLE_TIME;
        this.foragerSize = EngineConfig.FORAGAR_SIZE;
    }


    private AbstractMove getNextMove(RandomList<AbstractMove> randomMoveList,
                                                           ScoreLong currScore, ScoreLong prevSetpScore) {
        //매 턴마다 foraerMap 생성
        TreeMap<ScoreLong, List<AbstractMove>> foragerMap = new TreeMap<ScoreLong, List<AbstractMove>>();
        int acceptCount = 0;
        boolean accept;

        do {
            AbstractMove randomPick = randomMoveList.randomPick(cloudBalance.randomSeed);
            if (randomPick == null) {
                break;
            }

            randomMoveList.remove(randomPick);

            if (randomPick.isMoveDoable(scoreCalculator)) {
                AbstractMove undoMove = randomPick.doMove(scoreCalculator); // random pick 된 move에 대해 수행
                ScoreLong nextScore = scoreCalculator.calculateScore(); // 점수를 계산하고
                undoMove.doMove(scoreCalculator); // 다시 원

                // 다음 move가 현재보다 좋거나, 이전 setp보다 좋으면 accept
                accept = (nextScore.compareTo(currScore) >= 0 || nextScore.compareTo(prevSetpScore) >= 0);
                if (accept) {
                    acceptCount++;
                    if (!foragerMap.containsKey(nextScore)) {
                        foragerMap.put(nextScore, new ArrayList<AbstractMove>());
                    }
                    foragerMap.get(nextScore).add(randomPick);
                }

                if (acceptCount == foragerSize) {
                    break;
                }
            }
            // 시간 종료 조건 추가
        } while (true);

        if (foragerMap.isEmpty()) {
            return  null;
        }

        int highScoreSize = foragerMap.lastEntry().getValue().size();
        int randomIndex = cloudBalance.randomSeed.nextInt(highScoreSize);
        return foragerMap.lastEntry().getValue().get(randomIndex);
    }

    @Override
    public void optimalPlanning() {

        ScoreLong currScore = scoreCalculator.calculateScore();
        logger.info("ini score : " + currScore);

        ScoreLong bestScore = new ScoreLong(currScore);

        // 초기 해를 best score 로 저장
        for (int i = 0; i <= scoreArraySize; i++) {
            scoreArray[i] = new ScoreLong(currScore);
        }


        int nstep = 0; //

        long calstartTime = System.currentTimeMillis(); // 시작 시간
        int elapsedTime = 0; // 엔진 실행 이후 얼마나 경과되었는가?
        int displayedTime = 0; //몇 초마다 보여줄 것인가?
        long bestScoreTime = calstartTime; //best score 갱신 시간 기록


        Map<CloudProcess, CloudComputer> backupBestScoreAnswer = new HashMap<CloudProcess, CloudComputer>();


        List<AbstractMove> moveList = new ArrayList<AbstractMove>();
        moveList.addAll(CloudComputerChangeMoveFactory.createMoveList(cloudBalance));
        moveList.addAll(CloudProcessSwapFactory.createMoveList(cloudBalance));
        RandomList<AbstractMove> randomMoveList = new RandomList<>(moveList);

        do {
            nstep ++;

            // 이 놈을 어디에 둘 것인가??
            ScoreLong prevSetpScore = scoreArray[nstep % scoreArraySize];

            // next Move의 score를 가져오기
            AbstractMove nexMove = getNextMove(randomMoveList, currScore, prevSetpScore);
            if (nexMove == null) {
                continue;
            }
            randomMoveList.restoreAllCandi();

//            logger.info("nsetp" +nstep);
            nexMove.doMove(scoreCalculator); // 현재 move를 반영

            ScoreLong nextScore = scoreCalculator.calculateScore();

            currScore.assign(nextScore);

            if (currScore.compareTo(prevSetpScore) >= 0) {
                prevSetpScore.assign(currScore);
            }


            long calEndTime = System.currentTimeMillis();
            int currentTime = (int) (calEndTime - calstartTime) / 1000;


            if (currScore.compareTo(bestScore) > 0) {
                bestScore.assign(currScore);

                //domove는 이미 되어있다고 본다.
//                backupBestScoreAnswer.clear();
//                for (CloudProcess cloudProcess : cloudBalance.getProcessList()) {
//                    backupBestScoreAnswer.put(cloudProcess, cloudProcess.getComputer());
//                }
                bestScoreTime = calEndTime;
            }

            //1초에 한번씩 display
            if (currentTime != displayedTime && currentTime % 1 == 0) {
                displayedTime = currentTime;
                logger.info(String.format("%11d", nstep) + " best : " + bestScore.toString() + " time " + currentTime);
            }

            // 10초마다 한 번씩 검사
            if (currentTime != elapsedTime && currentTime % 10 == 0) {
                elapsedTime = currentTime;

                //30초 이후로 10초마다 score array size를 줄인다.
//                if (elapsedTime >= 30) {
//                    scoreArraySize = scoreArraySize/2;
//                }

                // 최대 수행 시간 초과시 종료
                if (elapsedTime >= maxRunningTime) {
                    break;
                }
            }

            // 해 개선이 이루어지지 않으면 종료
//            if ((calEndTime - bestScoreTime) / 1000 >= maxPermitIdleTime) {
//                break;
//            }
        } while (true);


        //final score 처리
        for (CloudProcess cloudProcess : cloudBalance.getProcessList()) {
            CloudComputer bestAnswer = backupBestScoreAnswer.get(cloudProcess);
            cloudProcess.setComputer(bestAnswer);
        }
        scoreCalculator.resetWorkingSolution(cloudBalance);
        logger.info("final score ==" + scoreCalculator.calculateScore());

    }
}
