package com.example.microblog;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@EnableAutoConfiguration(exclude = S3AutoConfiguration.class)
class MicroBlogApplicationTests {

	@DynamicPropertySource
	static void registerStorageProperties(DynamicPropertyRegistry registry) {
		registry.add("storage.type", () -> "local");
	}

	@Test
	void contextLoads() {
	}

}
