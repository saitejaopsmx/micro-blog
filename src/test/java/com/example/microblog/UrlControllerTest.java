package com.example.microblog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlController.class)
@Import(SecurityConfig.class)
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    @WithMockUser
    public void testGetUrlContent() throws Exception {
        String url = "https://example.com";
        String expectedContent = "Example Domain";
        when(restTemplate.getForObject(url, String.class)).thenReturn(expectedContent);

        mockMvc.perform(get("/url").param("url", url))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    public void testGetUrlContentUnauthenticated() throws Exception {
        mockMvc.perform(get("/url").param("url", "https://example.com"))
                .andExpect(status().isUnauthorized());
    }
}
