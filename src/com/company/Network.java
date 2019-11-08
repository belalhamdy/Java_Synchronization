package com.company;

import java.util.Scanner;

public class Network {
    private static Scanner in = new Scanner(System.in);

    public static void mainConsole(String[] args) {
        System.out.println("Welcome to Router Simulator application\n-----------------------------------");
        System.out.println("What is number of available WI-FI Connections?");
        int connections = in.nextInt();
        System.out.println("What is number of devices Clients want to connect?");
        int devices = in.nextInt();
        Router router = new Router(connections);
        Device[] input = new Device[devices];
        for (int i = 1; i <= devices; i++) {
            System.out.print(String.format("Enter device %d name: ", i));
            in.nextLine();
            String name = in.nextLine();
            System.out.print(String.format("Enter device %d type: ", i));
            int t = in.nextInt();
            System.out.println();
            Device tempDevice = new Device(router, name, Device.Type.fromInteger(t));
            input[i - 1] = tempDevice;
        }
        for (int i = 0; i < devices; ++i) {
            input[i].start();
        }

    }
}
