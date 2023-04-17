package com.monitoring.app;

import com.monitoring.app.models.profile.LinuxDeviceProfile;
import com.monitoring.app.models.profile.NetworkDeviceProfile;
import com.monitoring.app.models.profile.Profile;
import com.monitoring.app.util.Poller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    static boolean eventLoopStarted = false;
    static final int DEFAULT_THREADS = 5;
    static final int DEFAULT_INTERVAL = 10;
    private static final String[][] menu = {{"Discovery","Exit"},{"Linux Device","Network Device"}};
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static final Poller eventLoop = new Poller(DEFAULT_THREADS);

    private static int getMenu(int menuIndex, String question) {
        int choice = 0;

        System.out.println(question);
        for(int index = 0;index < menu[menuIndex].length;index++){
            System.out.println((index+1) + ". " + menu[menuIndex][index]);
        }

        try{
            choice = Integer.parseInt(reader.readLine());
        }catch (Exception exception){
            exception.printStackTrace();
        }

        return choice;
    }

    public static int getMainMenu(){
        try {

            int choice = getMenu(0, "Enter Choice?");

            if (choice == 1) {

                String ip, userName, password, profileName;

                System.out.print("Enter ProfileName: ");
                profileName = reader.readLine();

                System.out.print("Enter IP Address: ");
                ip = reader.readLine();

                choice = getMenu(1, "Enter Device Type?");

                if (choice == 1) {

                    System.out.print("Enter Username: ");
                    userName = reader.readLine();

                    System.out.print("Enter Password: ");
                    password = reader.readLine();

                    System.out.print("Enter Port: ");
                    int port = Integer.parseInt(reader.readLine());

                    System.out.print("Enter Poll Interval: ");
                    int pollInterval = Integer.max(Integer.parseInt(reader.readLine()),DEFAULT_INTERVAL);

                    Profile profile = LinuxDeviceProfile.makeProfile(profileName, ip, userName, password, port);

                    if(profile == null) {
                        System.out.println("Profile Name is already taken!!");
                        return 0;
                    }

                    eventLoop.provision(profile,pollInterval);

                    if(!eventLoopStarted){
                        new Thread(()->eventLoop.start(),"Poller").start();
                        eventLoopStarted = true;
                    }

                } else if (choice == 2) {

                    System.out.println("Network Device Selected");

                    Profile profile = NetworkDeviceProfile.makeProfile(profileName, ip);

                    if(profile == null) {
                        System.out.println("Profile Name is already taken!!");
                        return 0;
                    }

                    eventLoop.provision(profile,DEFAULT_INTERVAL);

                    if(!eventLoopStarted){
                        new Thread(()->eventLoop.start(),"Poller").start();
                        eventLoopStarted = true;
                    }

                } else {
                    System.out.println("Invalid Choice!");
                }

            } else if (choice == 2) {

                System.out.print("Exiting");
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.print(".");
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                }
                return 1;

            } else {
                System.out.println("Invalid Choice " + choice);
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        while(getMainMenu() == 0){
            System.out.println("\n....................\n");
        }
    }
}