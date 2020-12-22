package com.bright.spring.learn.mock;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * read mock data
 *
 * @author zhengyuan
 * @since 2020/12/18
 */
@Component
@ConfigurationProperties(prefix = "mock")
@PropertySource(value = "classpath:mock.properties", encoding = "utf-8")
public class MockDataMap {
    private Map<String, String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getValueFromMap(String key) {
        return map.get(key);
    }
}
