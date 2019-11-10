package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Router {
    private final List<Device> onlineDevices = new ArrayList<>();
    private int[] port;
    private Semaphore online;
    private boolean alive;

    public void setMaxConnections(int maxConnections){
        alive = true;
        online = new Semaphore(maxConnections);
        port = new int[maxConnections];
        Arrays.fill(port, -1);
    }

    public void forceStopWork() {
        alive = false;
        for (Device d : onlineDevices) {
            d.interrupt();
        }
    }

    public boolean getAlive() {
        return alive;
    }

    void connectDevice(Device curr) {
        online.acquire();

        synchronized (port) {
            assignPort(curr);
        }
        synchronized (onlineDevices){
            onlineDevices.add(curr);
        }
        Network.removeDevice(curr.getID());
    }

    void disconnectDevice(Device curr) {
        synchronized (onlineDevices) {
            onlineDevices.remove(curr);
        }
        synchronized (port){
            unassignPort(curr);
        }
        online.release();
    }

    private void assignPort(Device curr) {
        for (int i = 0; i < port.length; ++i) {
            if (port[i] == -1) {
                port[i] = curr.getID();
                ++i;
                curr.setPort(i);
                break;
            }
        }
    }

    private void unassignPort(Device curr) {
        for (int i = 0; i < port.length; ++i) {
            if (port[i] == curr.getID()) {
                port[i] = -1;
                ++i;
                break;
            }
        }
    }
}