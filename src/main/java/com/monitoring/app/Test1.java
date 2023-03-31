package com.monitoring.app;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test1 {

    public static void main(String[] args) {
        System.out.println(new Date().toString());
        ExecutorService workerThreadPool = Executors.newFixedThreadPool(10);
//        eventLoop.provision(Profile.makeProfile("mihir","localhost","mihir","A",22),5);
//        new Thread(() -> eventLoop.start(),"Starter").start();
//        eventLoop.provision(Profile.makeProfile("mihir","localhost","mihir","B",22),5);
//        eventLoop.provision(Profile.makeProfile("mihir","localhost","mihir","C",22),5);
//        eventLoop.provision(Profile.makeProfile("mihir","localhost","mihir","D",22),5);
        for(int i = 0;i < 1000;i++) {
            int finalI = i;
            workerThreadPool.submit(() -> {
                try {
                    for(long j = 0;j < 100000000000L;j++);
//                    System.out.println("Task: "+ finalI);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            });
        }
        workerThreadPool.shutdown();
    }
}
