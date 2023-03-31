package com.monitoring.app.discovery;

import com.monitoring.app.outputparser.OutputParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public final class PingApp {
    public static final int checkStatus(String ip){

        try {

            ProcessBuilder builder = new ProcessBuilder();
            builder.command(buildCommand(ip));
            builder.redirectErrorStream(true);

            Process process = builder.start();
            int exitCode = process.waitFor();
            if(exitCode != 0 && exitCode != 1){
                return -1;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while((line=reader.readLine()) != null){
                result.append(line);
            }

            return OutputParser.parseFping(result.toString()).get("%loss").equals("0%")?1:0;

        }catch(Exception exception){
            System.out.println(exception.getMessage());
            return -1;
        }
    }
    private static List<String> buildCommand(String ip) {
        List<String> command = new ArrayList<>();
        command.add("fping");
        command.add("-c3");
        command.add("-q");
        command.add(ip);
        return command;
    }

}
