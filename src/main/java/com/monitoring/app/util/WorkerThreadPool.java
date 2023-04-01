package com.monitoring.app.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WorkerThreadPool {
    private volatile boolean firstTask;
    private final int MAX_THREADS;

    //holds runnable task which users submits
    //users has no interaction with threads directly and use submit() to submit task
    private volatile Queue<Runnable> taskQueue;

    //holds the actual limited number of workerThreads which do the work by
    //taking task from taskQueue
    private ArrayList<WorkerThread> workerThreads;
    public WorkerThreadPool(int size){
        taskQueue = new LinkedList<>();
        workerThreads = new ArrayList<>();
        firstTask = true;
        MAX_THREADS = size;
    }

    //user interacts with this method
    //taskQueue is synchronized so that any workerThread don't access it
    //during addition of new task
    public void submit(Runnable task){

        synchronized (taskQueue) {
            taskQueue.offer(task);
            taskQueue.notifyAll();

            //lazy initialization of all the worker threads when first task is submitted
            if(firstTask) {

                for(int i = 0;i < this.MAX_THREADS;i++){
                    workerThreads.add(new WorkerThread(taskQueue));
                }

                //starts running all the workerThreads
                for (int i = 0; i < workerThreads.size(); i++) {
                    workerThreads.get(i).start();
                }

                //turning off so that only one time WorkerThreads are created :)
                firstTask = false;
            }
        }
    }

    public void shutdown(){
        for (WorkerThread w :
                workerThreads) {
            w.stopAccepting();
        }
    }

}
