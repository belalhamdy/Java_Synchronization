package com.company;
import java.util.LinkedList;
import java.util.Queue;
//import java.util.concurrent.Semaphore;

public class Router {
    Queue<Device> queue = new LinkedList<>();
    private Semaphore release,online;
    Router (int MaxConnections)
    {
        release = new Semaphore(0);
        online = new Semaphore(MaxConnections);
    }
    public void add (Device curr) throws InterruptedException {
        online.acquire();
        queue.add(curr);
        curr.Online();
        release.release();
    }
    public void remove () throws InterruptedException {
        //release.acquire();
        queue.remove();
        online.release();
    }

}
