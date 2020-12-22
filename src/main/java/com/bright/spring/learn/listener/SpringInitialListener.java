package com.bright.spring.learn.listener;

import com.bright.spring.learn.mock.MockDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SpringInitialListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SpringInitialListener.class);

    @Autowired
    private MockDataMap mockDataMap;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        logger.info("mock data is {}", mockDataMap.getMap());
    }
}
