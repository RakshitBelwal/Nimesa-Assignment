package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwsService {

    private final S3Client s3Client;
    private final Ec2Client ec2Client;

    public Optional<List<String>> listS3Buckets() {
        return Optional.of(s3Client.listBuckets().buckets().stream()
                .map(response -> response.name())
                .collect(Collectors.toList()));
    }

    public Optional<List<String>> listEc2Instances() {
        return Optional.of(ec2Client.describeInstances().reservations().stream()
                .flatMap(reservation -> reservation.instances().stream())
                .map(Instance::instanceId)
                .collect(Collectors.toList()));
    }
}
