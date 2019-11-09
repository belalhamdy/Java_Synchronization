package com.company;

import javax.swing.*;
import java.util.Random;

public class Device extends Thread {
    public static final int maxTimeout = 10000, minTimeout = 3000;
    int timeout;
    public int id;
    private String connectTimeStamp, workTimeStamp, logoutTimeStamp;

    public enum Type {
        Android, PC, Tablet, TV, IPhone, Laptop, Other;

        static Type fromInteger(int v) {
            return Type.values()[v - 1];
        }

        public static String[] toArray() {
            return new String[]{"Android", "PC", "Tablet", "TV", "IPhone", "Laptop", "Other"};
        }

        public String toString() {
            return this.name();
        }
    }

    private final String name;
    private final Type type;
    private final Router router;
    private final GUI.ProgressManager prg;

    /**
     * Constructs a new device with given name and type. The online time for the device is initialized randomly between minTimeout and maxTimeout
     */
    Device(Router router, String name, Type type, GUI.ProgressManager prg) {
        this(router, name, type, prg, new Random().nextInt(maxTimeout - minTimeout + 1) + minTimeout);
    }

    /**
     * Constructs a new device with given name and type. The online time for the device is initialized with timeout
     */
    Device(Router router, String name, Type type, GUI.ProgressManager prg, int timeout) {
        this.router = router;
        this.name = name;
        this.type = type;
        this.timeout = timeout;
        this.prg = prg;
        id = Network.getId();
    }

    @Override
    public String toString() {
        return this.name + "(" + this.type.toString() + ") " + this.timeout;
    }

    private void connectToRouter() {

        connectTimeStamp = Network.getTimeStampFormatted();
        System.out.println(this + " arrived");
        router.addDevice(this);
    }

    private void doOnlineWork() {
        workTimeStamp = Network.getTimeStampFormatted();
        router.removeFromQueue(this);
        System.out.println(this + " performs online activity");
        try {
            if (prg == null) {
                Thread.sleep(timeout);
            } else {
                int slept = 0;
                while (slept < timeout && router.getAlive()) {
                    prg.setValue(slept * 100 / timeout);
                    Thread.sleep(20);
                    slept += 20;
                }
                prg.setValue(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        logoutTimeStamp = Network.getTimeStampFormatted();

        System.out.println(this + " Logout");
        router.removeDevice(this);
    }

    public String[] serialize() {
        return new String[]{name, type.toString(), connectTimeStamp, workTimeStamp, logoutTimeStamp};
    }

    @Override
    public void run() {
        connectToRouter();
        if (!router.getAlive()) return;
        doOnlineWork();
        logout();
        Network.myGUI.log(serialize());
    }
}
