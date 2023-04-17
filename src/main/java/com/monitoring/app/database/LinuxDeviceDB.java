package com.monitoring.app.database;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class LinuxDeviceDB implements DB{
    private static final String path = "/home/mihir/IdeaProjects/NetworkMonitoringDevice/src/main/java/com/monitoring/app/polldata/linuxdevicedb/";
    private static DB db;

    private LinuxDeviceDB(){}

    public static DB getInstance(){
       if(db==null){
           synchronized (LinuxDeviceDB.class){
               if(db==null)
                   db = new LinuxDeviceDB();
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
