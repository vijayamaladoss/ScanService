package com.bofa.cde.scanservice.service;

import com.bofa.cde.scanservice.model.CodeScanJob;
import com.bofa.cde.scanservice.repository.CodeScanJobRepository;
import com.bofa.cde.scanservice.util.JobDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Service
public class CodeScanService {

    private final CodeScanJobRepository scanJobRepository;
    private final JobDispatcher<Void> jobDispatcher;

    @Autowired
    public CodeScanService(CodeScanJobRepository scanJobRepository, JobDispatcher<Void> jobDispatcher) {
        this.scanJobRepository = scanJobRepository;
        this.jobDispatcher = jobDispatcher;
    }


    public void createScanJob(String sourceCodePath) {
        CodeScanJob scanJob = new CodeScanJob();
        scanJob.setSourceCodePath(sourceCodePath);
        scanJob.setStatus("unprocessed");
        scanJobRepository.save(scanJob);
    }

    // Method to consume jobs
    public void consumeJobs() {
        while (true) {
            // Fetch unprocessed job from job queue
            List<CodeScanJob> scanJobs = scanJobRepository.findByStatus("unprocessed");

            if (!scanJobs.isEmpty()) {
                CodeScanJob scanJob = scanJobs.get(0);
                scanJob.setStatus("in_progress");

                try {
                    scanJobRepository.save(scanJob);

                    jobDispatcher.dispatchJob(() -> {
                        // Execute the command-line tool. You'll need to replace this with your actual command.
                        ProcessBuilder processBuilder = new ProcessBuilder("your-command", scanJob.getSourceCodePath());
                        try {
                            Process process = processBuilder.start();
                            process.waitFor();
                            scanJob.setStatus("completed");
                            scanJobRepository.save(scanJob);
                        } catch (Exception e) {
                            // Handle exception
                            scanJob.setStatus("failed");
                            scanJobRepository.save(scanJob);
                        }
                        return null; // As this is a Void Callable, return null.
                    });
                } catch (OptimisticLockingFailureException ex) {
                    // Job was picked up by another instance, retry
                    continue;
                }
            } else {
                // If there's no job to process, sleep for a while before trying again
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // Handle exception
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Don't forget to add a @PreDestroy method to shutdown the JobDispatcher
    @PreDestroy
    public void cleanup() {
        jobDispatcher.shutdown();
    }
}
