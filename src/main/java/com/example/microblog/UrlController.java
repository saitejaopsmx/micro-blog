package com.example.microblog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UrlController {

    private final RestTemplate restTemplate;

    public UrlController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/url")
    public String getUrlContent(@RequestParam String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
