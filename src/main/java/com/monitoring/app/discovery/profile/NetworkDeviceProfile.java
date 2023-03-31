package com.monitoring.app.discovery.profile;

import com.monitoring.app.database.DB;
import com.monitoring.app.database.NetworkDeviceDB;
import com.monitoring.app.discovery.PingApp;

import java.util.Date;

public final class NetworkDeviceProfile implements Profile {
    private final String profileName;
    private final String ip;
    private final DB db;

    private NetworkDeviceProfile(String profileName, String ip){
        this.profileName = profileName;
        this.ip = ip;
        this.db = new NetworkDeviceDB(this.profileName);
    }

    public static synchronized Profile makeProfile(String profileName, String ip) {

        if(Profile.checkProfileName(profileName) || !Profile.validateIP(ip))
            return null;

        return new NetworkDeviceProfile(profileName,ip);

    }

    @Override
    public boolean poll() {
        int status = PingApp.checkStatus(this.ip);
        this.db.write(new Date().toString());
        if(status == -1){
            this.db.write("ERROR");
            this.db.write("-----------------------------------------");
            return false;
        }
        this.db.write((status==1?"UP":"DOWN"));
        this.db.write("-----------------------------------------");
        return true;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getIP() {
        return ip;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileName='" + profileName + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
