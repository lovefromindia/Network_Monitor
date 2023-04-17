package com.monitoring.app.database;

import org.json.JSONObject;

public interface DB {

    //here profileName will be used to locate the file in which to store data
    //pollData will be stored as key:value pair
    boolean storeData (String profileName, JSONObject pollData);

}
