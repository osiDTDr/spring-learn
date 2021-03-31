package com.bright.spring.learn.cas.filter;

import com.bright.spring.learn.properties.ConfigPropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * auth filter
 *
 * @author zhengyuan
 * @since 2021/01/09
 */
@Component
public class AuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Value("${cas.ignore.url}")
    private String casIgnoreUrl;
    private List<String> casIgnoreUrlList;

    @Override
    public void init(FilterConfig filterConfig) {
        casIgnoreUrlList = Arrays.asList(casIgnoreUrl.split(","));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (isRequestUrlExcluded(request)) {
            logger.debug("request is ignored.");
            filterChain.doFilter(request, response);
            return;
        }
        logger.debug("request need auth");
        // Temporarily pass the request first, and then need to add verification
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    protected boolean isRequestUrlExcluded(final HttpServletRequest request) {
        if (CollectionUtils.isEmpty(casIgnoreUrlList)) {
            return false;
        }
        String requestUri = getRequestUrl(request);
        if (requestUri.contains("http")) {
            requestUri = requestUri.replace("//", "");
            requestUri = requestUri.substring(requestUri.indexOf("/"));
        }
        if (requestUri.contains("?")) {
            requestUri = requestUri.substring(0, requestUri.indexOf("?"));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("url is: {}", requestUri);
        }

        if (requestUri.endsWith("woff2")) {
            return true;
        }
        return casIgnoreUrlList.contains(requestUri);
    }

    /**
     * 获取当前请求的url
     */
    protected String getRequestUrl(HttpServletRequest httpRequest) {
        String requestURI = httpRequest.getRequestURI();
        String contentPath = httpRequest.getContextPath();

        if (requestURI.contains(";jsessionid=")) {
            int start = requestURI.indexOf(";jsessionid=");
            int end = requestURI.indexOf("?");
            if (end == -1) {
                requestURI = requestURI.substring(0, start);
            } else {
                requestURI = requestURI.substring(0, start) + requestURI.substring(end);
            }
        }

        if (!contentPath.equals("/")) {
            requestURI = requestURI.replaceFirst(contentPath, "");
        }
        return requestURI;
    }
}
