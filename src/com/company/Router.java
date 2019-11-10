package com.company;

import java.util.ArrayList;
import java.util.List;

public class Router {
    private final List<Device> onlineDevices = new ArrayList<>();
    private Semaphore online;
    private boolean alive = false;

    public void setMaxConnections(int MaxConnections) {
        alive = true;
        online = new Semaphore(MaxConnections);
    }

    public void forceStopWork() {
        alive = false;
        for(Device d : onlineDevices){
            d.interrupt();
        }
    }

    public boolean getAlive() {
        return alive;
    }

    void connectDevice(Device curr) {
        online.acquire();
        synchronized (onlineDevices){
            onlineDevices.add(curr);
        }
        Network.removeDevice(curr.getID());
    }

    void disconnectDevice(Device curr) {
        synchronized (onlineDevices) {
            onlineDevices.remove(curr);
        }
        online.release();
    }
/*
    List<Device> getOnlineDevices() {
        return onlineDevices;
    }
*/
}
