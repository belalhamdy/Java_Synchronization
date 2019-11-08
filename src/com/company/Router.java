package com.company;
import java.util.ArrayList;
import java.util.List;
public class Router {
    private List<Device> queue = new ArrayList<>();
    private Semaphore online;
    Router (int MaxConnections) {
        online = new Semaphore(MaxConnections);
    }
    void addDevice(Device curr) {
        online.acquire();
        queue.add(curr);
    }
    void removeDevice(Device curr)  {
        queue.remove(curr);
        online.release();
    }
    List<Device> getOnlineDevices(){
        return queue;
    }

}
