package com.monitoring.app.discovery.profile;

import com.monitoring.app.database.DB;
import com.monitoring.app.database.LinuxDeviceDB;
import com.monitoring.app.discovery.PingApp;
import com.monitoring.app.discovery.SSHApp;
import com.monitoring.app.outputparser.OutputParser;

import java.util.ArrayList;
import java.util.Date;

public final class LinuxDeviceProfile implements Profile {
    private final String profileName;
    private final String ip;
    private final String userName;
    private final String password;
    private final int port;
    private final DB db;

    private LinuxDeviceProfile(String profileName, String ip, String userName, String password, int port){
        this.profileName = profileName;
        this.ip = ip;
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.db = new LinuxDeviceDB(this.profileName);
    }

    public static synchronized Profile makeProfile(String profileName, String ip, String userName, String password, int port) {

        if(Profile.checkProfileName(profileName) || !Profile.validateIP(ip))
            return null;

        return new LinuxDeviceProfile(profileName,ip,userName,password,port);
    }

    @Override
    public synchronized boolean poll() {

        if(PingApp.checkStatus(this.ip) == 1){
            ArrayList<String> commands = new ArrayList<>();
            this.db.write(new Date().toString());
            commands.add("free -h");
            commands.add("vmstat -aSk | awk 'NR>1'");
            commands.add("iostat -d | awk 'NR>1'");
            commands.add("ps -eo pid,thcount,%cpu,%mem,rss,vsz --sort=-%cpu,-%mem | head -n 11");
            commands.add("mpstat -P ALL -o JSON | grep  \"{\\\"cpu\\\":\"");
            ArrayList<String> output = new ArrayList<>();
            if(!SSHApp.executeCommands(this.profileName,this.password,this.ip,this.port,commands,output)){
                this.db.write(" SSH ERROR");
                this.db.write("-----------------------------------------");
                return false;
            }
            this.db.write(OutputParser.parseFree(output.get(0)).toString());
            this.db.write(OutputParser.parseVmstat(output.get(1)).toString());
            this.db.write(OutputParser.parseIostat(output.get(2)).toString());
            this.db.write(OutputParser.parsePs(output.get(3)).toString());
            this.db.write(OutputParser.parseMpstat(output.get(4)).toString());
            this.db.write("-----------------------------------------");
        }
        else{
            this.db.write(" DEVICE DOWN");
            this.db.write("-----------------------------------------");
            return false;
        }
        return true;

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

}
