package move;

import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CloudComputerChangeMoveFactory {
    public static List<CloudComputerChangeMove> createMoveList(CloudBalance cloudBalance) {
        List<CloudComputerChangeMove> moveList = new ArrayList<>();
        List<CloudComputer> cloudComputerList = cloudBalance.getComputerList();
        for (CloudProcess cloudProcess : cloudBalance.getProcessList()) {
            for (CloudComputer cloudComputer : cloudComputerList) {
                moveList.add(new CloudComputerChangeMove(cloudProcess, cloudComputer));
            }
        }
        return moveList;
    }
}
