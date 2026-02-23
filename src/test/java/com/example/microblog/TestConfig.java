package com.example.microblog;

import io.awspring.cloud.s3.S3Template;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    @Bean
    public S3Client s3Client() {
        return mock(S3Client.class);
    }

    @Bean
    public S3Presigner s3Presigner() {
        return mock(S3Presigner.class);
    }

    @Bean
    public S3Template s3Template() {
        return mock(S3Template.class);
    }
}
