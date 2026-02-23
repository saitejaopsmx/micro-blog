package com.example.microblog;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@EnableAutoConfiguration(exclude = S3AutoConfiguration.class)
@Import(TestConfig.class)
class MicroBlogApplicationTests {

	@Test
	void contextLoads() {
	}

}
