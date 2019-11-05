package com.company;

import java.util.Scanner;

public class Main {
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Router Simulator application\n-----------------------------------");
        System.out.println("What is number of available WI-FI Connections?");
        int connections = in.nextInt();
        System.out.println("What is number of devices Clients want to connect?");
        int devices = in.nextInt();
        Router network = new Router(connections);
        Device input [] = new Device[devices];
        for (int i = 1; i <= devices; i++) {
            System.out.print(String.format("Enter device %d name: ", i));
            in.nextLine();
            String name = in.nextLine();
            System.out.print(String.format("Enter device %d type: ", i));
            int t = in.nextInt();
            System.out.println();
            Device tempDevice;
            switch (t) {
                case 0:
                    tempDevice = new Device(network,name, Device.Type.Android);
                    break;
                case 1:
                    tempDevice = new Device(network,name, Device.Type.PC);
                    break;
                case 2:
                    tempDevice = new Device(network,name, Device.Type.Tablet);
                    break;
                case 3:
                    tempDevice = new Device(network,name, Device.Type.TV);
                    break;
                case 4:
                    tempDevice = new Device(network,name, Device.Type.IPhone);
                    break;
                case 5:
                    tempDevice = new Device(network,name, Device.Type.Laptop);
                    break;
                default:
                    tempDevice = new Device(network,name, Device.Type.Other);
            }
            input[i-1] = tempDevice;
        }
        for (int i = 0 ; i<devices ; ++i)
        {
            input[i].start();
        }

    }
}
