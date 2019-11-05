package com.company;

import java.util.Random;

public class Device extends Thread {
    final int timeout = 4000;
    private Router router;
    public static enum Type{
        Android(0), PC(1), Tablet(2), TV(3), IPhone(4), Laptop(5),Other(6);
        private int value;
        Type(int v)
        {
            value = v;
        }
        int getTypeValue()
        {
            return value;
        }
    }
    private final String name;
    private final Type type;
    Device(Router router,String name, Type type)
    {
        this.name = name;
        this.type = type;
        this.router = router;
    }
    @Override
    public String toString()
    {
        return this.name + " - " + this.type;
    }
    private void Connect() throws InterruptedException {
        System.out.println(this + " Connected");
        router.add(this);
    }
    public void Online () throws InterruptedException {
        System.out.println(this + " Online");
        try {
            Thread.sleep(getTimeOut());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logout();
    }
    private void Logout () throws InterruptedException {
        System.out.println(this + " Logout");
        router.remove();
    }
    private int getTimeOut()
    {
        return (int)(Math.random()*((10000-3000)+1))+3000;
    }
    @Override
    public void run()
    {
        try {
            Connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
