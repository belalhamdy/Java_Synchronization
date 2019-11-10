package com.company;

public class Semaphore {
    private int value;

    protected Semaphore(int initial) {
        value = initial ;
    }

    public synchronized void acquire() {
        value-- ;
        if (value < 0)
            try {
                wait() ;
            } catch (  InterruptedException ignored ){}
    }

    public synchronized void release() {
        value++ ;
        if (value <= 0) notify();
    }

}