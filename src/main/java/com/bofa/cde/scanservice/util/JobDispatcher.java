package com.bofa.cde.scanservice.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class JobDispatcher<T> {

    private final ExecutorService executorService;

    public JobDispatcher() {
        this.executorService = Executors.newCachedThreadPool();
    }

    public void dispatchJob(Callable<T> job) {
        executorService.submit(job);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
