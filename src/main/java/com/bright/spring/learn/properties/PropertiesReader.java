package com.bright.spring.learn.properties;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Properties文件读取基类
 */
public abstract class PropertiesReader {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesReader.class);

    protected Properties props;

    /**
     * 加载Properties配置文件
     *
     * @return {@link Properties} 配置
     */
    protected Properties loadProperties() {
        Properties props;
        InputStreamReader inputStreamReader = null;
        try {
            props = new Properties();
            inputStreamReader = new InputStreamReader(getPropertiesInputStream(), StandardCharsets.UTF_8);
            props.load(inputStreamReader);
            inputStreamReader.close();
        } catch (Exception e) {
            logger.error("Load bic properties file error ", e);
            return new Properties();
        } finally {
            IOUtils.closeQuietly(inputStreamReader);
        }
        logger.info("Load bic properties file success.");
        return props;
    }

    /**
     * 获取配置文件路径
     *
     * @return {@link InputStream} 输入流
     * @throws Exception exception
     */
    protected abstract InputStream getPropertiesInputStream() throws Exception;
}
