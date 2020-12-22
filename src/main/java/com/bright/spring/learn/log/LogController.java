package com.bright.spring.learn.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * dynamic change log level
 *
 * @author zhengyuan
 * @since 2020/12/18
 */
@RestController
@RequestMapping("/log")
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private LoggingSystem loggingSystem;

    @PutMapping
    public String changeLogLevel(@RequestParam("path") String path,
                                 @RequestParam("level") String level) {
        final LogLevel logLevel;
        switch (level) {
            case "TRACE":
                logLevel = LogLevel.TRACE;
                break;
            case "DEBUG":
                logLevel = LogLevel.DEBUG;
                break;
            case "INFO":
                logLevel = LogLevel.INFO;
                break;
            case "WARN":
                logLevel = LogLevel.WARN;
                break;
            case "ERROR":
                logLevel = LogLevel.ERROR;
                break;
            case "FATAL":
                logLevel = LogLevel.FATAL;
                break;
            case "OFF":
                logLevel = LogLevel.OFF;
                break;
            default:
                logger.error("invalid log level {}", level);
                return String.format("invalid log level for %s", level);
        }
        try {
            loggingSystem.setLogLevel(path, logLevel);
        } catch (Exception e) {
            logger.error("change log level error, path is {}, level is {}", path, level, e);
        }
        return "change log level success";
    }
}
