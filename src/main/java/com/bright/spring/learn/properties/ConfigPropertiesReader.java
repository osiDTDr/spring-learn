package com.bright.spring.learn.properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 基础配置信息加载类，读取配置目录的config.properties。
 *
 * @author zhengyuan
 * @since 2021/01/05
 */
public class ConfigPropertiesReader extends PropertiesReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigPropertiesReader.class);

    private static final String CONFIG_PATH = "conf/config.properties";
    private static final String INSTALLATION_PATH = "conf/installation.properties";
    private static final String COMPONENT_PATH_KEY = "@componentPath";
    public Properties installationProperties = new Properties();

    private ConfigPropertiesReader() {
        readInstallation();
        props = super.loadProperties();
        props.putAll(installationProperties);
    }

    private void readInstallation() {
        InputStream is = null;
        try {
            String bicPath = System.getProperty("user.dir");
            String configPath = bicPath + File.separator + INSTALLATION_PATH;
            is = new FileInputStream(configPath);
            this.installationProperties.load(is);
        } catch (Exception e) {
            logger.error("read the installation properties fail.", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }


    /**
     * 获取配置文件的路径，若配置文件不存在，则使用默认配置文件路径
     *
     * @return {@link InputStream} config file input stream
     * @throws Exception exception
     */
    @Override
    protected InputStream getPropertiesInputStream() throws Exception {
        File configFile = new File(getComponentInstallPath() + "/" + CONFIG_PATH);
        if (!configFile.exists()) {
            logger.warn("Get bic config properties file {} can not found, default config properties will be load on classpath.", configFile.getAbsolutePath());
            return ConfigPropertiesReader.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Get bic config properties file {}.", configFile.getAbsolutePath());
        }
        return new FileInputStream(configFile);

    }

    public String getComponentInstallPath() {
        return this.installationProperties.getProperty(COMPONENT_PATH_KEY);
    }

    /**
     * 获取配置信息
     *
     * @param key config key
     * @return config value
     */
    public String getValue(String key) {
        return props.getProperty(key);
    }

    public static ConfigPropertiesReader getInstance() {
        return Singleton.instance;
    }

    /**
     * 单例模式
     */
    private static class Singleton {
        private static final ConfigPropertiesReader instance = new ConfigPropertiesReader();
    }
}
