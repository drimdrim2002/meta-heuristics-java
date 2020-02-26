package move;

import domain.CloudBalance;
import domain.CloudProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CloudProcessSwapFactory {

    public static List<AbstractMove> createMoveList(CloudBalance cloudBalance) {
        List<CloudProcess> cloudProcessList = cloudBalance.getProcessList();
        List<AbstractMove> moveList = new ArrayList<>();

        for (ListIterator<CloudProcess> leftIt = cloudProcessList.listIterator(); leftIt.hasNext();) {
            CloudProcess leftCloudProcess = leftIt.next();
            for (ListIterator<CloudProcess> rightIt = cloudProcessList.listIterator(leftIt.nextIndex()); rightIt.hasNext();) {
                CloudProcess rightCloudProcess = rightIt.next();
                moveList.add(new CloudProcessSwapMove(leftCloudProcess, rightCloudProcess));
            }
        }
        return moveList;
    }
}
