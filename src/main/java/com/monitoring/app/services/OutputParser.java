package com.monitoring.app.services;

import java.util.*;

public class OutputParser {

    private OutputParser(){}

    //parsing in java because fping pipelining is not possible in bash
    public static Map<String,String> parseFping(String result){

        Map<String, String> results = new HashMap<>();
        try {

            Arrays.stream(result
                            .split(":")[1]
                            .split(","))
                    .forEach(s -> {
                        String[] keyValues = s.split("=");
                        if (keyValues.length == 2) {
                            String[] keys = keyValues[0].split("/");
                            String[] values = keyValues[1].split("/");
                            for (int i = 0; i < keys.length; i++) {
                                results.put(keys[i].strip(), values[i].strip());
                            }
                        }
                    });

        }catch(Exception exception){
            System.out.println(exception.getMessage());
            results.clear();
        }
        return results;
    }

    public static Map<String,Map<String, String>> parseFree(String result){

        List<List<String>> resultTable = new ArrayList<>();
        Arrays.stream(result
                        .split("\n"))
                        .forEach(line -> {
                            resultTable.add(Arrays.asList(line.replace(":"," ").split("\\s+")));
                        });


        Map<String,Map<String,String>> results = new HashMap<>();
        results.put("Mem",new HashMap<>());
        results.put("Swap",new HashMap<>());
        for(int i = 1; i < resultTable.size(); i++){
            for(int j = 1; j < resultTable.get(i).size(); j++){
                results.get(resultTable.get(i).get(0)).put(resultTable.get(0).get(j),resultTable.get(i).get(j));
            }
        }
        return results;

    }

    public static Map<String,String> parseVmstat(String result){
        List<List<String>> resultTable = new ArrayList<>();
        Arrays.stream(result
                        .split("\n"))
                            .forEach(line ->
                                    resultTable.add(Arrays.asList(line.split("\\s+")))
                            );

        Map<String,String> results = new HashMap<>();
        for(int i = 0;i < resultTable.get(0).size();i++){
            if(resultTable.get(0).get(i).length()>0)
                results.put(resultTable.get(0).get(i),resultTable.get(1).get(i));
        }
        return results;
    }

    public static List<Map<String,String>> parseMpstat(String result){

        List<Map<String,String>> results = new ArrayList<>();
        Arrays.stream(result
                        .split("\n"))
                        .forEach(line -> {
                            Map<String,String> keyValueMap = new HashMap<>();
                            Arrays.stream(line.replaceAll("\\{|\\},|\\}", "")
                                            .split(","))
                                            .forEach(keyValue -> {
                                                String []keyValueArr = keyValue.split(":");
                                                if(keyValueArr.length==2) {
                                                    keyValueMap.put(keyValueArr[0].strip(),keyValueArr[1].strip());
                                                }
                                            });
                                   results.add(keyValueMap);
                        });

        return results;

    }

    public static Map<String,Map<String,String>> parseIostat(String result){

        Map<String,Map<String,String>> results = new HashMap<>();
        ArrayList<String> keys = new ArrayList<>();
        Arrays.stream(result.split("\n"))
                .forEach(line -> {
                    if(line.length()>0) {
                        if(line.contains("Device")){
                            Arrays.stream(line.split("\s+")).forEach(keys::add);
                        }
                        else{
                            Map<String,String> loopInfo = new HashMap<>();
                            List<String> values = Arrays.asList(line.split("\s+"));
                            if(!values.isEmpty()) {
                                for (int i = 1; i < keys.size(); i++) {
                                    loopInfo.put(keys.get(i),values.get(i));
                                }
                                results.put(values.get(0),loopInfo);
                            }
                        }
                    }
                });
        return results;

    }

    public static List<Map<String,String>> parsePs(String result){

        List<Map<String,String>> results = new ArrayList<>();

        List<String> keys = new ArrayList<>();

        Arrays.stream(result
                        .split("\n"))
                .forEach(line -> {
                    if(line.contains("PID")){
                        Arrays.stream(line.split("\s+")).forEach(key->{
                            if(key.strip().length()>0){
                                keys.add(key);
                            }
                        });
                    }
                    else {
                        Map<String,String> metrics = new HashMap<>();

                        ArrayList<String> values = new ArrayList<>();
                        Arrays.stream(line.split("\s+")).forEach(value-> {
                            if(value.strip().length()>0) {
                                values.add(value);
                            }
                        });
                        for(int i = 0;i < values.size();i++){
                            metrics.put(keys.get(i),values.get(i));
                        }
                        results.add(metrics);
                    }
                });
        return results;
    }

}
