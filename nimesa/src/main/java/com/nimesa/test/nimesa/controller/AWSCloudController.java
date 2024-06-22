package com.nimesa.test.nimesa.controller;

import com.nimesa.test.nimesa.model.ServiceResult;
import com.nimesa.test.nimesa.repository.JobRepository;
import com.nimesa.test.nimesa.service.AWSCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cloud/aws")
public class AWSCloudController {

    @Autowired
    private AWSCloudService awsCloudService;

    @Autowired
    private JobRepository jobRepository;

    @PostMapping("/services")
    public Long discoverServices(@RequestBody List<String> services) {
        Long jobId = awsCloudService.createJob();

        services.forEach(service -> {
            if ("EC2".equalsIgnoreCase(service)) {
                awsCloudService.discoverEC2Instances(jobId);
            } else if ("S3".equalsIgnoreCase(service)) {
                awsCloudService.discoverS3Buckets(jobId);
            }
        });

        return jobId;
    }

    @GetMapping("/job/{jobId}")
    public String getJobResult(@PathVariable Long jobId) {
        return awsCloudService.getJobStatus(jobId);
    }

    @GetMapping("/result/{service}")
    public List<String> getDiscoveryResult(@PathVariable String service) {
        return awsCloudService.getServiceResults(service);
    }

    @GetMapping("/s3/count/{bucketName}")
    public int getS3BucketObjectCount(@PathVariable String bucketName) {
        return awsCloudService.getS3BucketObjects(bucketName).size();
    }

    @GetMapping("/s3/like")
    public List<String> getS3BucketObjectLike(@RequestParam String bucketName, @RequestParam String pattern) {
        return awsCloudService.getS3BucketObjects(bucketName)
                .stream()
                .filter(name -> name.contains(pattern))
                .collect(Collectors.toList());
    }
}
