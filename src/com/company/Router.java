package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Router {
    private List<Device> queue = new ArrayList<>();
    private Semaphore online;
    private boolean alive = true;

    public void setMaxConnections(int MaxConnections) {
        online = new Semaphore(MaxConnections);
    }

    public void interrupt() {
        queue.clear();
        alive = false;
    }

    public boolean getAlive() {
        return alive;
    }

    void addDevice(Device curr) {
        online.acquire();
        queue.add(curr);
    }

    void removeDevice(Device curr) {
        queue.remove(curr);
        online.release();
    }

    void removeFromQueue(Device curr) {
        Network.removeDevice(curr.id);
    }

    List<Device> getOnlineDevices() {
        return queue;
    }

}
