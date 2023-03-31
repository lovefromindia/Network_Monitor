package com.monitoring.app.util;
import java.util.Queue;

public class WorkerThread extends Thread {

    //volatile so that change is visible
    private volatile Queue<Runnable> taskQueue;
    private volatile boolean acceptingTask;
    private Runnable currentTask;

    //so that it can acquire lock on the taskQueue
    // given by the WorkerThreadPool for getting the task from the queue
    public WorkerThread(Queue<Runnable> taskQueue){
        this.taskQueue = taskQueue;
        this.acceptingTask = true;
    }

    //if user has shutdown the eventLoop
    //then all the workerThreads will have acceptingTask = false
    //in that case once the taskQueue becomes empty
    // both conditions turns false and workerThread stops running
    public void run() {
        while(acceptingTask || taskQueue.size()>0) {
            synchronized (taskQueue) {
                while (taskQueue.isEmpty()) {
                    try {
                        taskQueue.wait();
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                }
                currentTask = taskQueue.poll();
                taskQueue.notifyAll();
            }

            //this is outside synchronized block so that
            //other workerThreads don't have to wait for
            //this thread (for acquiring lock on taskQueue)
            // to run the task and can run next task in the queue (if any)
            currentTask.run();
        }
    }

    public void stopAccepting() {
        acceptingTask = false;
    }
}
