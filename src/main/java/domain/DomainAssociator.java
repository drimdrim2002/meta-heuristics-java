package domain;

import java.util.List;

public class DomainAssociator {

    public static void associate(CloudBalance cloudBalance) {
        List<CloudComputer> computerList = cloudBalance.getComputerList();
        List<CloudProcess> processList = cloudBalance.getProcessList();

        for (CloudComputer cloudComputer : computerList) {
            cloudComputer.setAvailProcessList(processList);
        }

    }
}
