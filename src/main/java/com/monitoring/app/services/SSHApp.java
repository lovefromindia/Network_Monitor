package com.monitoring.app.services;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public final class SSHApp {

    private SSHApp(){}

    public static boolean executeCommands(String username, String password,
                                          String host, int port, Map<String,String> output){

        Session session = null;

        try{
            //for sending multiple commands, we can reuse the same session
            JSch jsch = new JSch();
            JSch.setLogger(new com.jcraft.jsch.Logger(){

                Path path = Paths.get("/home/mihir/IdeaProjects/NetworkMonitoringDevice/src/main/java/com/monitoring/app/logging/log.txt");
                @Override
                public boolean isEnabled(int level){
                    return true;
                }
                public void log(int level, String message){
                    try {
                        StandardOpenOption option =
                                !Files.exists(path) ? StandardOpenOption.CREATE : StandardOpenOption.APPEND;
                        Files.write(path, java.util.Arrays.asList(message), option);
                    } catch (Exception e) {
                        System.err.println(message);
                    }
                }
            });

            jsch.setKnownHosts("/home/mihir/.ssh/known_hosts");
            jsch.addIdentity("/home/mihir/.ssh/id_rsa");
            session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);

            session.connect(5000);


            Session finalSession = session;
            CommandMapper.commandMap.forEach((commandName, command)->{
                ChannelExec channel = null;
                try {
                    channel = (ChannelExec) finalSession.openChannel("exec");
                    channel.setCommand(command);
                    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                    channel.setOutputStream(responseStream);
                    channel.connect();

                    while (channel.isConnected()) {
                        Thread.sleep(100);
                    }
                    output.put(commandName,responseStream.toString());
                }catch (Exception exception){
                    output.put(commandName,"ERROR");
                }finally {
                    if (channel != null) {
                        channel.disconnect();
                    }
                }
            });

        } catch(Exception exception){
            System.out.println(exception.getMessage());
            return false;
        }finally {
            if (session != null) {
                session.disconnect();
            }
        }
        return true;
    }

}
