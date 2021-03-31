package com.bright.spring.learn.properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * test properties file read
 *
 * @author zhengyuan
 * @since 2021/01/05
 */
public class ConfigPropertiesReaderTest {

    @Test
    public void testGetValue() {
        String value = ConfigPropertiesReader.getInstance().getValue("server.port");
        assert StringUtils.equals(value, "8080");
    }
}
