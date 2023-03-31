package com.monitoring.app;

import com.monitoring.app.discovery.SSHApp;

import java.util.ArrayList;

public class Test2 {
    public static void main(String[] args) {
//        new Thread(()->{
//            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
//            try {
//                Thread.sleep(1000);
//                System.out.println(Thread.currentThread().getName());
//                String name = r.readLine();
//                System.out.println("Name: " + name);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        },"Thread1").start();
//        new Thread(()->{
//            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
//            try {
//                Thread.sleep(1000);
//                System.out.println(Thread.currentThread().getName());
//                String name = r.readLine();
//                System.out.println("Name: " + name);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        },"Thread2").start();

//        System.out.println(PingApp.checkStatus("10.20.40.103"));

        ArrayList<String> commands = new ArrayList<>();
        commands.add("free -h");
        commands.add("vmstat -aSk | awk 'NR>1'");
        commands.add("iostat -d | awk 'NR>1'");
        commands.add("ps -eo pid,thcount,%cpu,%mem,rss,vsz --sort=-%cpu,-%mem | head -n 11");
        commands.add("mpstat -P ALL -o JSON | grep  \"{\\\"cpu\\\":\"");
        ArrayList<String> output = new ArrayList<>();
        SSHApp.executeCommands("dhaval","ddbera95",
                "10.20.40.103",22,commands,output);

//        System.out.println(OutputParser.parseFree(output.get(0)));
//        System.out.println(OutputParser.parseVmstat(output.get(1)));
//        System.out.println(OutputParser.parseIostat(output.get(2)));
//        System.out.println(OutputParser.parsePs(output.get(3)));
//        System.out.println(OutputParser.parseMpstat(output.get(4)));
    }
}
