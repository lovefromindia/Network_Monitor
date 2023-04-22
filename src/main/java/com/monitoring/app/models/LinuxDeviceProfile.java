package com.monitoring.app.models;

import com.monitoring.app.services.PollingService;

public final class LinuxDeviceProfile implements Profile {
    private final String profileName;
    private final String ip;
    private final String userName;
    private final String password;
    private final int port;
    private LinuxDeviceProfile(String profileName, String ip, String userName, String password, int port){
        this.profileName = profileName;
        this.ip = ip;
        this.userName = userName;
        this.password = password;
        this.port = port;
    }

    public static synchronized Profile makeProfile(String profileName, String ip, String userName, String password, int port) {

        if(Profile.checkProfileName(profileName) || !Profile.validateIP(ip))
            return null;

        return new LinuxDeviceProfile(profileName,ip,userName,password,port);
    }

    public String getProfileName() {
        return profileName;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileName='" + profileName + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

    @Override
    public void poll() {
        PollingService.poll(this);
    }
}
