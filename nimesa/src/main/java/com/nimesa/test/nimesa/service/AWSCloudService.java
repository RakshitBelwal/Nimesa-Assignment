package com.nimesa.test.nimesa.service;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.nimesa.test.nimesa.model.Job;
import com.nimesa.test.nimesa.model.ServiceResult;
import com.nimesa.test.nimesa.repository.ServiceResultRepository;
import com.nimesa.test.nimesa.repository.JobRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AWSCloudService {

    private final AmazonEC2 amazonEC2;

    private final AmazonS3 amazonS3;

    private final ServiceResultRepository serviceResultRepository;

    private final JobRepository jobRepository;

    @Async
    @Transactional
    public CompletableFuture<Void> discoverEC2Instances(Long jobId) {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        DescribeInstancesResult result = amazonEC2.describeInstances(request);

        List<String> instanceIds = result.getReservations().stream()
                .flatMap(reservation -> reservation.getInstances().stream())
                .map(Instance::getInstanceId)
                .collect(Collectors.toList());

        instanceIds.forEach(instanceId -> {
            ServiceResult serviceResult = new ServiceResult();
            serviceResult.setJobId(jobId);
            serviceResult.setService("EC2");
            serviceResult.setResult(instanceId);
            serviceResultRepository.save(serviceResult);
        });

        updateJobStatus(jobId, "Success");
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Transactional
    public CompletableFuture<Void> discoverS3Buckets(Long jobId) {
        List<Bucket> buckets = amazonS3.listBuckets();

        buckets.forEach(bucket -> {
            ServiceResult result = new ServiceResult();
            result.setJobId(jobId);
            result.setService("S3");
            result.setResult(bucket.getName());
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
        return serviceResultRepository.findByJobId(1L)
                .stream()
                .map(ServiceResult::getResult)
                .collect(Collectors.toList());
    }
}


