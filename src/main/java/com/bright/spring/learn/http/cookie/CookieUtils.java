package com.bright.spring.learn.http.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * cookie utils class
 *
 * @author zhengyuan
 * @since 2020/12/22
 */
public class CookieUtils {
    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * Add the given {@code ResponseCookie}.
     *
     * @param response Represents a reactive server-side HTTP response.
     * @param name     the cookie name
     * @param value    the cookie value
     * @param host     cookie "Domain" attribute
     */
    public static void setCookie(ServerHttpResponse response, String name,
                                 String value, String host) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .domain(host)
                .path("/")
                .httpOnly(true)
                .build();
        response.addCookie(cookie);
    }

    /**
     * Return the cookie value or an empty string (never {@code null}).
     *
     * @param request Represents a reactive server-side HTTP request.
     * @param name    the key
     * @return Return the cookie value or an empty string (never {@code null}).
     */
    public static String getCookie(ServerHttpRequest request, String name) {
        HttpCookie httpCookie = request.getCookies().getFirst(name);
        return httpCookie != null ? httpCookie.getValue() : null;
    }

    /**
     * get all cookies with this specified request
     *
     * @param request servlet request
     * @return cookie key-value
     */
    public static Map<String, String> getCookies(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            logger.error("current request have no cookie");
        } else {
            cookieMap = Arrays.stream(cookies).collect(
                    Collectors.toMap(Cookie::getName, Cookie::getValue, (k1, k2) -> k1));
        }
        return cookieMap;
    }

    /**
     * 创建cookie，并将新cookie添加到“响应对象”response中
     *
     * @param response servlet response
     */
    public static void addCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("name_test", "value_test");  // 创建新cookie
        cookie.setMaxAge(5 * 60);   // 设置存在时间为5分钟
        cookie.setPath("/");    // 设置作用域
        response.addCookie(cookie); // 将cookie添加到response的cookie数组中返回给客户端
    }

    /**
     * edit cookie
     *
     * @param request  servlet request
     * @param response servlet response
     */
    public static void editCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            logger.error("current request have no cookie");
        } else {
            for (Cookie cookie : cookies) {
                //迭代时如果发现与指定cookieName相同的cookie，就修改相关数据
                if (cookie.getName().equals("name_test")) {
                    cookie.setValue("new_value");//修改value
                    cookie.setPath("/");
                    cookie.setMaxAge(10 * 60);// 修改存活时间
                    response.addCookie(cookie);//将修改过的cookie存入response，替换掉旧的同名cookie
                    break;
                }
            }
        }
    }

    /**
     * delete cookie
     *
     * @param request  servlet request
     * @param response servlet response
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            logger.error("current request have no cookie");
        } else {
            for (Cookie cookie : cookies) {
                //如果找到同名cookie，就将value设置为null，将存活时间设置为0，再替换掉原cookie，这样就相当于删除了。
                if (cookie.getName().equals("name_test")) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }
}
