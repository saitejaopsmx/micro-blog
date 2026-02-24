package com.example.microblog;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalStorageService implements StorageService {

    private final Path root = Paths.get(System.getProperty("user.home"), "posts");
    private final ObjectMapper objectMapper;

    public LocalStorageService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Post createPost(Post post) throws IOException {
        String id = UUID.randomUUID().toString();
        post.setId(id);
        Files.write(this.root.resolve(id), objectMapper.writeValueAsBytes(post));
        return post;
    }

    @Override
    public Post getPost(String id) throws IOException {
        byte[] bytes = Files.readAllBytes(this.root.resolve(id));
        return objectMapper.readValue(bytes, Post.class);
    }

    @Override
    public List<Post> getAllPosts() throws IOException {
        try (Stream<Path> stream = Files.walk(this.root, 1)) {
            return stream.filter(path -> !path.equals(this.root))
                    .map(path -> {
                        try {
                            return getPost(path.getFileName().toString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deletePost(String id) throws IOException {
        Files.delete(this.root.resolve(id));
    }

    @Override
    public List<String> getPostUrlsByTitle(String keyword) throws IOException {
        try (Stream<Path> stream = Files.walk(this.root, 1)) {
            return stream.filter(path -> !path.equals(this.root))
                    .filter(path -> {
                        try {
                            Post post = getPost(path.getFileName().toString());
                            return post.getTitle().contains(keyword);
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .map(path -> path.toAbsolutePath().toString())
                    .collect(Collectors.toList());
        }
    }
}
