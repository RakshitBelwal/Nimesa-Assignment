package com.nimesa.test.nimesa.service;

import com.nimesa.test.nimesa.model.Job;
import com.nimesa.test.nimesa.model.ServiceResult;
import com.nimesa.test.nimesa.repository.ServiceResultRepository;
import com.nimesa.test.nimesa.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AWSCloudService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ServiceResultRepository serviceResultRepository;

    @Async
    public CompletableFuture<Void> discoverEC2Instances(Long jobId) {
        // Simulate EC2 discovery
        List<String> instanceIds = List.of("i-1234567890abcdef0", "i-abcdef1234567890");

        instanceIds.forEach(instanceId -> {
            ServiceResult result = new ServiceResult();
            result.setJobId(jobId);
            result.setService("EC2");
            result.setResult(instanceId);
            serviceResultRepository.save(result);
        });

        updateJobStatus(jobId, "Success");
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> discoverS3Buckets(Long jobId) {
        // Simulate S3 discovery
        List<String> bucketNames = List.of("bucket1", "bucket2");

        bucketNames.forEach(bucketName -> {
            ServiceResult result = new ServiceResult();
            result.setJobId(jobId);
            result.setService("S3");
            result.setResult(bucketName);
            serviceResultRepository.save(result);
        });

        updateJobStatus(jobId, "Success");
        return CompletableFuture.completedFuture(null);
    }

    private void updateJobStatus(Long jobId, String status) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        jobRepository.save(job);
    }

    public Long createJob() {
        Job job = new Job();
        job.setStatus("In Progress");
        jobRepository.save(job);
        return job.getId();
    }

    public String getJobStatus(Long jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found")).getStatus();
    }

    public List<String> getServiceResults(String serviceName) {
        return serviceResultRepository.findByService(serviceName)
                .stream()
                .map(ServiceResult::getResult)
                .collect(Collectors.toList());
    }

    public List<String> getS3BucketObjects(String bucketName) {
        return serviceResultRepository.findByServiceAndPattern("S3", bucketName)
                .stream()
                .map(ServiceResult::getResult)
                .collect(Collectors.toList());
    }
}


