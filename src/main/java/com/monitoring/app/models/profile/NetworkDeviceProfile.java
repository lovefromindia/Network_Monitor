package com.monitoring.app.models.profile;

import com.monitoring.app.services.PollingService;

public final class NetworkDeviceProfile implements Profile {
    private final String profileName;
    private final String ip;

    private NetworkDeviceProfile(String profileName, String ip){
        this.profileName = profileName;
        this.ip = ip;
    }

    public static synchronized Profile makeProfile(String profileName, String ip) {

        if(Profile.checkProfileName(profileName) || !Profile.validateIP(ip))
            return null;

        return new NetworkDeviceProfile(profileName,ip);

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

    @Override
    public void poll() {
        PollingService.poll(this);
    }
}
