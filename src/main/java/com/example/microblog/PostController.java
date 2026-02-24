package com.example.microblog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final StorageService storageService;

    public PostController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Operation(summary = "Create a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public Post createPost(@RequestBody Post post) throws IOException {
        return storageService.createPost(post);
    }

    @Operation(summary = "Get a post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{id}")
    public Post getPost(@PathVariable String id) throws IOException {
        return storageService.getPost(id);
    }

    @Operation(summary = "Get all posts")
    @GetMapping
    public List<Post> getAllPosts() throws IOException {
        return storageService.getAllPosts();
    }

    @Operation(summary = "Delete a post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable String id) throws IOException {
        storageService.deletePost(id);
    }

    @Operation(summary = "Get post URLs by title keyword")
    @GetMapping("/urls")
    public List<String> getPostUrlsByTitle(@RequestParam String keyword) throws IOException {
        return storageService.getPostUrlsByTitle(keyword);
    }
}
