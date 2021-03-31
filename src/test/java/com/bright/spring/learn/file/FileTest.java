package com.bright.spring.learn.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class FileTest {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    /**
     * 在每次测试执行前构建mvc环境
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testUpload() {
        try {
            String result = mockMvc.perform(
                    MockMvcRequestBuilders
                            .multipart("/file/upload")
                            .file(
                                    new MockMultipartFile("file", "test.txt", ",multipart/form-data", "hello upload".getBytes(StandardCharsets.UTF_8))
                            )
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn().getResponse().getContentAsString();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
