package com.monitoring.app.services;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandMapper {

    private CommandMapper(){}

    static {
        commandMap = new LinkedHashMap<>();
        initCommandMapper();
    }

    public static Map<String,String> commandMap;
    private static void initCommandMapper(){
        commandMap.put("system.cpu.uptime","cat /proc/uptime | awk '{printf \"%d %d %d\\n\", ($1/3600),(($1%3600)/60),($1%60)}'");
        commandMap.put("system.cpu.load_avg","uptime | awk -F'(:|,)' '{print $(NF-2),$(NF-1),$NF}'");
        commandMap.put("system.cpu.avg_utilization","mpstat | tail -n 1 | awk '{print (100-$NF)}'");
        commandMap.put("system.cpu.user_utilization","mpstat | tail -n 1 | awk '{print $(NF-9)}'");
        commandMap.put("system.cpu.sys_utilization","mpstat | tail -n 1 | awk '{print $(NF-7)}'");
        commandMap.put("system.cpu.iowait_utilization","mpstat | tail -n 1 | awk '{print $(NF-6)}'");
        commandMap.put("system.memory.total_phy_mem","free -m | tail -n 2 | head -n 1 | awk '{printf \"%0.2f\\n\",$2/1024.0}'");
        commandMap.put("system.memory.used_phy_mem","free -h | tail -n 2 | head -n 1 | awk '{printf \"%.2f\\n\",($3/$2)}'");
        commandMap.put("system.memory.avail_phy_mem","free -h | tail -n 2 | head -n 1 | awk '{printf \"%.2f\\n\",($NF/$2)}'");
        commandMap.put("system.memory.total_swap_mem","free -m | tail -n 1 | awk '{printf \"%.2f\\n\",$2/1024.0}'");
        commandMap.put("system.memory.used_swap_mem","free -m | tail -n 1 | awk '{printf \"%.2f\\n\",($3/$2)*100.0}'");
        commandMap.put("system.memory.avail_swap_mem","free -m | tail -n 1 | awk '{printf \"%.2f\\n\",100-(($3/$2)*100.0)}'");
        commandMap.put("system.disk.used_disk_mem","df --total | tail -n1 | awk '{printf \"%.2f\\n\",$5}'");
        commandMap.put("system.network.interfaces","ifconfig -s | awk 'NR>1{print $1}'");
    }

    public static String getCommand(String command){
        return commandMap.get(command);
    }

}
