package com.bright.spring.learn.http.cookie;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * cookie test
 *
 * @author zhengyuan
 * @since 2020/12/22
 */
@RestController
@RequestMapping("/cookie")
public class CookieController {

    @GetMapping
    public String getCookie(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Map<String, String> cookies = CookieUtils.getCookies(httpServletRequest);
        return "";
    }
}
