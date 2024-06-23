package com.example.demo.controller;

import com.example.demo.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aws")
@RequiredArgsConstructor
public class AwsController {

    private final AwsService awsService;

    @GetMapping("/s3/buckets")
    public Optional<List<String>> listS3Buckets() {
        return awsService.listS3Buckets();
    }

    @GetMapping("/ec2/instances")
    public Optional<List<String>> listEc2Instances() {
        return awsService.listEc2Instances();
    }
}
