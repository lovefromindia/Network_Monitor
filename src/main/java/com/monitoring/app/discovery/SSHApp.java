package com.monitoring.app.discovery;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.List;

public final class SSHApp {

    private SSHApp(){}

    public static boolean executeCommands(String username, String password,
                                          String host, int port, List<String> commands, List<String> output){

        Session session=null;
        ChannelExec channel=null;

        try{
            //for sending multiple commands, we can reuse the same session
            JSch jsch = new JSch();
            jsch.setKnownHosts("/home/mihir/.ssh/known_hosts");
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.connect(500);

            for(String command: commands) {

                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);
                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                channel.setOutputStream(responseStream);
                channel.connect();

                while (channel.isConnected()) {
                    Thread.sleep(100);
                }
                output.add(responseStream.toString());
            }
        } catch(Exception exception){
            System.out.println(exception.getMessage());
            return false;
        }finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return true;
    }

}
