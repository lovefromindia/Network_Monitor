package com.monitoring.app.models.profile;

public interface Profile {

    //validates ip by regex
    static boolean validateIP(String ip){
        return true;
    }


    //returns true if ProfileName is already taken or false otherwise
    static boolean checkProfileName(String profileName){
        return false;
    }


    //polling method
    void poll();

}
