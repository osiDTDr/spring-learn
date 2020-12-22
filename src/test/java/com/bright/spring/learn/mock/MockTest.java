package com.bright.spring.learn.mock;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MockTest {
    private static final Logger logger = LoggerFactory.getLogger(MockTest.class);
    @Autowired
    private MockDataMap mockDataMap;

    @Test
    public void testMockData() {
        logger.info("mock data is {}", mockDataMap.getMap());
    }
}
