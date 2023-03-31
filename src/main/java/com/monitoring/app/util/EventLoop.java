package com.monitoring.app.util;

import com.monitoring.app.discovery.profile.Profile;

import java.time.Instant;
import java.util.PriorityQueue;

public final class EventLoop {

    private static class Query{
        private long timeStamp;
        private final Profile profile;
        private final long interval;

        Query(Profile profile, long timeStamp, long interval) {
            this.profile = profile;
            this.timeStamp = timeStamp;
            this.interval = interval;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }
        public long getTimeStamp() {
            return timeStamp;
        }
        public long getInterval() {
            return interval;
        }

        @Override
        public String toString() {
            return "Query{" +
                    "timeStamp=" + timeStamp +
                    ", profile='" + profile + '\'' +
                    '}';
        }
    }

    private final PriorityQueue<Query> pollingEvents;
    private final WorkerThreadPool workerThreadPool;

    //sort the taskQueue by timestamp in increasing order
    public EventLoop(int threadPoolSize){
        pollingEvents = new PriorityQueue<>((obj, other) -> obj.getTimeStamp()-other.getTimeStamp()<0L?-1:1);
        workerThreadPool = new WorkerThreadPool(threadPoolSize);
    }

    //register ip and interval
    public boolean provision(Profile profile, int interval){
        try {
            pollingEvents.add(new Query(profile, Instant.now().getEpochSecond(), interval));
        }catch(Exception exception){
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }

    //start the polling
    public void start() {

        while (!pollingEvents.isEmpty()) {
            
            if(pollingEvents.peek().getTimeStamp()>Instant.now().getEpochSecond())
                continue;

            Query query = pollingEvents.peek();
            pollingEvents.poll();

            workerThreadPool.submit(query.profile::poll);

            query.setTimeStamp(query.getTimeStamp() + query.getInterval());

            pollingEvents.offer(query);

        }

    }
}