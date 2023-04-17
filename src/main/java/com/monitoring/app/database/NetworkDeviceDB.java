package com.monitoring.app.database;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class NetworkDeviceDB implements DB{

    private static final String path = "/home/mihir/IdeaProjects/NetworkMonitoringDevice/src/main/java/com/monitoring/app/polldata/networkdevicedb/";

    private static DB db;
    private NetworkDeviceDB(){}

    public static DB getInstance(){
        if(db==null){
            synchronized (NetworkDeviceDB.class){
                if(db==null)
                    db = new NetworkDeviceDB();
            }
        }
        return db;
    }

    @Override
    public boolean storeData(String profileName, JSONObject pollData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+profileName+".txt",true))){
            writer.write(pollData.toString() + "\n");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }


}