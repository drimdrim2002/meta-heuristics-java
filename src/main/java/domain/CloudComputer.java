package domain;

import java.io.Serializable;

public class CloudComputer implements Serializable {

    private int id;
    private int cpuPower; // in gigahertz
    private int memory; // in gigabyte RAM
    private int networkBandwidth; // in gigabyte per hour
    private int cost; // in euro per month

    public CloudComputer() {
    }

    public CloudComputer(int id, int cpuPower, int memory, int networkBandwidth, int cost) {
        this.id = id;
        this.cpuPower = cpuPower;
        this.memory = memory;
        this.networkBandwidth = networkBandwidth;
        this.cost = cost;
    }

    public int getCpuPower() {
        return cpuPower;
    }

    @Override
    public String toString() {
        return "CloudComputer{" +
                "id=" + id +
                ", cpuPower=" + cpuPower +
                ", memory=" + memory +
                ", networkBandwidth=" + networkBandwidth +
                ", cost=" + cost +
                '}';
    }

    public void setCpuPower(int cpuPower) {
        this.cpuPower = cpuPower;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getNetworkBandwidth() {
        return networkBandwidth;
    }

    public void setNetworkBandwidth(int networkBandwidth) {
        this.networkBandwidth = networkBandwidth;
    }

    public int getCost() {
        return cost;
    }


    public void setCost(int cost) {
        this.cost = cost;
    }
}
