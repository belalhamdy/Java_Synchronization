package com.company;

import java.util.Random;

public class Device extends Thread {
    public static final int maxTimeout = 10000, minTimeout = 3000;
    int timeout;

    public enum Type {
        Android, PC, Tablet, TV, IPhone, Laptop, Other;

        static Type fromInteger(int v){
            return Type.values()[v-1];
        }
    }

    private final String name;
    private final Type type;
    private final Router router;

    /**
     * Constructs a new device with given name and type. The online time for the device is initialized randomly between minTimeout and maxTimeout
     */
    Device(Router router, String name, Type type) {
        this(router, name, type, new Random().nextInt(maxTimeout - minTimeout + 1) + minTimeout);
    }

    /**
     * Constructs a new device with given name and type. The online time for the device is initialized with timeout
     */
    Device(Router router, String name, Type type, int timeout) {
        this.router = router;
        this.name = name;
        this.type = type;
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return this.name + "(" + this.type.name() + ") - ";
    }

    private void connectToRouter() {
        System.out.println(this + " arrived");
        router.addDevice(this);
    }

    private void doOnlineWork() {
        System.out.println(this + " performs online activity");
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logout();
    }

    private void logout() {
        System.out.println(this + " Logout");
        router.removeDevice(this);
    }

    @Override
    public void run() {
        connectToRouter();
        doOnlineWork();
        logout();
    }
}
