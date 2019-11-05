package com.company;
import java.util.ArrayList;
import java.util.List;
//import java.util.concurrent.Semaphore;

public class Router {
    List<Device> queue = new ArrayList<>();
    private Semaphore release,online;
    Router (int MaxConnections)
    {
        release = new Semaphore(0);
        online = new Semaphore(MaxConnections);
    }
    void add(Device curr) throws InterruptedException {
        online.acquire();
        queue.add(curr);
        curr.Online();
        //release.release();
    }
    void remove(Device curr) throws InterruptedException {
        //release.acquire();
        queue.remove(curr);
        online.release();
    }

}
