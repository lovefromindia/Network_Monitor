package com.monitoring.app.database;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class NetworkDeviceDB implements DB{

    private static final String path = "/home/mihir/IdeaProjects/NetworkMonitoringDevice/src/main/java/com/monitoring/app/polldata/networkdevicedb/";

    private final String profileName;
    public NetworkDeviceDB(String profileName){
        this.profileName = profileName;
    }

    @Override
    public synchronized boolean write(String pollData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+profileName+".txt",true))){
            writer.write(pollData+"\n");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }

}