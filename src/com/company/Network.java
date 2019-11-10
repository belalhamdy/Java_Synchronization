package com.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Vector;

public class Network {
    public static GUI myGUI;

    private static Router router = new Router();
    private static Vector<Device> deviceQueue = new Vector<>();
    private static int id = 0;
    private static boolean inSimulation = false;

    /*
    public static void mainConsole(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Router Simulator application\n-----------------------------------");
        System.out.println("What is number of available WI-FI Connections?");
        int connections = in.nextInt();
        System.out.println("What is number of devices Clients want to connect?");
        int devices = in.nextInt();
        //router = new Router(connections);
        Device[] input = new Device[devices];
        for (int i = 1; i <= devices; i++) {
            System.out.print(String.format("Enter device %d name: ", i));
            in.nextLine();
            String name = in.nextLine();
            System.out.print(String.format("Enter device %d type: ", i));
            int t = in.nextInt();
            System.out.println();
            Device tempDevice = new Device(router, name, Device.Type.fromInteger(t), null);
            input[i - 1] = tempDevice;
        }
        for (int i = 0; i < devices; ++i) {
            input[i].start();
        }

    }
    */

    public static void startSimulation(int connections) throws Exception {
        if (deviceQueue.size() < 1) throw new Exception("No devices in queue");
        inSimulation = true;
        router.setMaxConnections(connections);
        for (Device curr : deviceQueue) {
            curr.start();
        }
    }

    public static void addDevice(String name, int type, int timeout) {
        Device tempDevice;
        if (timeout > 0)
            tempDevice = new Device(router, name, Device.Type.fromInteger(type), myGUI.addProgressBar(name), timeout);
        else
            tempDevice = new Device(router, name, Device.Type.fromInteger(type), myGUI.addProgressBar(name));
        deviceQueue.add(tempDevice);
        if (inSimulation) tempDevice.start();
    }

    public static void removeDevice(int id) {
        for (Device curr : deviceQueue) {
            if (curr.id == id) {
                deviceQueue.remove(curr);
                break;
            }
        }
        myGUI.updateQueue(deviceQueue);
    }

    public static Vector<Device> getDeviceQueue() {
        return deviceQueue;
    }

    public static void clearData() {
        deviceQueue.removeAllElements();
        router.interrupt();
        inSimulation = false;
    }

    public static String getTimeStampFormatted() {
        return DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(getTimeStamp());
    }

    private static LocalDateTime getTimeStamp() {
        return LocalDateTime.now();
    }

    public static int getId() {
        return id++;
    }
}
