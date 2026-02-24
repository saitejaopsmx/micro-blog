package com.example.microblog;

import java.io.IOException;
import java.util.List;

public interface StorageService {
    Post createPost(Post post) throws IOException;
    Post getPost(String id) throws IOException;
    List<Post> getAllPosts() throws IOException;
    void deletePost(String id) throws IOException;
    List<String> getPostUrlsByTitle(String keyword) throws IOException;
}
