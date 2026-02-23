package com.example.microblog;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Template s3Template;
    private final String bucketName;
    private final ObjectMapper objectMapper;
    private final S3Presigner s3Presigner;

    public S3Service(S3Template s3Template, @Value("${aws.s3.bucket-name}") String bucketName, S3Presigner s3Presigner) {
        this.s3Template = s3Template;
        this.bucketName = bucketName;
        this.objectMapper = new ObjectMapper();
        this.s3Presigner = s3Presigner;
    }

    public Post createPost(Post post) throws IOException {
        String id = UUID.randomUUID().toString();
        post.setId(id);
        s3Template.store(bucketName, id, objectMapper.writeValueAsString(post));
        return post;
    }

    public Post getPost(String id) throws IOException {
        String content = s3Template.read(bucketName, id, String.class);
        return objectMapper.readValue(content, Post.class);
    }

    public List<Post> getAllPosts() {
        return s3Template.listObjects(bucketName, "").stream()
                .map(S3Resource::getFilename)
                .map(key -> {
                    try {
                        return getPost(key);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public void deletePost(String id) {
        s3Template.deleteObject(bucketName, id);
    }

    public List<String> getPostUrlsByTitle(String keyword) {
        return s3Template.listObjects(bucketName, "").stream()
                .map(S3Resource::getFilename)
                .filter(key -> {
                    try {
                        Post post = getPost(key);
                        return post.getTitle().contains(keyword);
                    } catch (IOException e) {
                        return false;
                    }
                })
                .map(key -> {
                    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(10))
                            .getObjectRequest(req -> req.bucket(bucketName).key(key))
                            .build();
                    return s3Presigner.presignGetObject(presignRequest).url().toString();
                })
                .collect(Collectors.toList());
    }
}
