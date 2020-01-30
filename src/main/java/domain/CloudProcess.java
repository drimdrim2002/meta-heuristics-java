package domain;

import java.io.Serializable;

public class CloudProcess implements Serializable {

    private int id;
    private int requiredCpuPower; // in gigahertz
    private int requiredMemory; // in gigabyte RAM
    private int requiredNetworkBandwidth; // in gigabyte per hour


    public CloudProcess() {
    }

    public CloudProcess(int id, int requiredCpuPower, int requiredMemory, int requiredNetworkBandwidth) {
        this.id = id;
        this.requiredCpuPower = requiredCpuPower;
        this.requiredMemory = requiredMemory;
        this.requiredNetworkBandwidth = requiredNetworkBandwidth;
    }

    public int getRequiredCpuPower() {
        return requiredCpuPower;
    }

    public void setRequiredCpuPower(int requiredCpuPower) {
        this.requiredCpuPower = requiredCpuPower;
    }

    public int getRequiredMemory() {
        return requiredMemory;
    }

    public void setRequiredMemory(int requiredMemory) {
        this.requiredMemory = requiredMemory;
    }

    public int getRequiredNetworkBandwidth() {
        return requiredNetworkBandwidth;
    }

    @Override
    public String toString() {
        return "CloudProcess{" +
                "id=" + id +
                ", requiredCpuPower=" + requiredCpuPower +
                ", requiredMemory=" + requiredMemory +
                ", requiredNetworkBandwidth=" + requiredNetworkBandwidth +
                ", computer=" + computer +
                '}';
    }

    public void setRequiredNetworkBandwidth(int requiredNetworkBandwidth) {
        this.requiredNetworkBandwidth = requiredNetworkBandwidth;
    }


    // Planning variables: changes during planning, between score calculations.
    private CloudComputer computer;

    public CloudComputer getComputer() {
        return computer;
    }

    public void setComputer(CloudComputer computer) {
        this.computer = computer;
    }

}
