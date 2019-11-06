package com.company;

import java.util.Random;

public class Device extends Thread {
    final int maxTimeOut = 10000, minTimeOut = 3000;
    int timeout;
    private Router router;

    public enum Type {
        Android, PC, Tablet, TV, IPhone, Laptop, Other
    }

    private final String name;
    private final Type type;

    Device(Router router, String name, Type type) {
        this.name = name;
        this.type = type;
        this.router = router;
        this.timeout = new Random().nextInt(maxTimeOut - minTimeOut + 1) + minTimeOut;
    }

    @Override
    public String toString() {
        return this.name + "(" + this.type.name() + ") - ";
    }

    private void Connect() {
        System.out.println(this + " arrived");
        router.add(this);
    }

    public void Online() {
        System.out.println(this + " performs online activity");
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logout();
    }

    private void Logout() {
        System.out.println(this + " Logout");
        router.remove(this);
    }

    @Override
    public void run() {
        Connect();
    }
}
