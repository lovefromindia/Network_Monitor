package com.monitoring.app.services;

import com.monitoring.app.database.DB;
import com.monitoring.app.database.LinuxDeviceDB;
import com.monitoring.app.database.NetworkDeviceDB;
import com.monitoring.app.models.LinuxDeviceProfile;
import com.monitoring.app.models.NetworkDeviceProfile;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class PollingService {

    private PollingService(){}

    public static void poll(NetworkDeviceProfile profile){

        JSONObject pollingData = new JSONObject();

        String key = String.valueOf(Instant.now().getEpochSecond());
        pollingData.put(key,new JSONObject());

        DB dbWriter = NetworkDeviceDB.getInstance();

        int status = PingApp.checkStatus(profile.getIP());

        pollingData.getJSONObject(key).put("status",status);

        if(status == -1){
            pollingData.getJSONObject(key).put("message","ERROR");
        }
        else {
            pollingData.getJSONObject(key).put("message",(status == 1 ? "UP" : "DOWN"));
        }

        dbWriter.storeData(profile.getProfileName(),pollingData);

    }

    public static void poll(LinuxDeviceProfile profile) {

        JSONObject pollingData = new JSONObject();

        String key = String.valueOf(Instant.now().getEpochSecond());
        pollingData.put(key,new JSONObject());

        DB dbWriter = LinuxDeviceDB.getInstance();

        int status = PingApp.checkStatus(profile.getIP());
        pollingData.getJSONObject(key).put("status",status);

        if(status == -1){
            pollingData.getJSONObject(key).put("message","ERROR");
        }
        else if(status == 0){
            pollingData.getJSONObject(key).put("message","DOWN");
        }
        else {
            pollingData.getJSONObject(key).put("message","UP");
            Map<String,String> output = new LinkedHashMap<>();
            SSHApp.executeCommands(profile.getUserName(), profile.getPassword(), profile.getIP(), profile.getPort(), output);
            output.forEach((metric,result) -> pollingData.getJSONObject(key).put(metric,Arrays.toString(result.strip().split("[ \n]+"))));
            if(output.size() == 0){
                pollingData.getJSONObject(key).put("message","SSH_ERROR");
            }
        }
        dbWriter.storeData(profile.getProfileName(),pollingData);
    }

}
