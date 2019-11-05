package com.company;

public class Semaphore {
    private int value;

    protected Semaphore() { value = 0 ; }

    protected Semaphore(int initial) {
        value = initial ;
    }

    public synchronized void acquire() throws InterruptedException {
        value-- ;
        if (value < 0)
            try {
                wait() ;
            } catch (  InterruptedException e )
            {
                throw e;
            }
    }

    public synchronized void release() {
        value++ ;
        if (value <= 0) notify() ;
    }

}
