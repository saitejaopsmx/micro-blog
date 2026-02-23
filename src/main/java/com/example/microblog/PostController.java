package com.example.microblog;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final S3Service s3Service;

    public PostController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) throws IOException {
        return s3Service.createPost(post);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable String id) throws IOException {
        return s3Service.getPost(id);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return s3Service.getAllPosts();
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable String id) {
        s3Service.deletePost(id);
    }
}
