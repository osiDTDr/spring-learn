package com.bright.spring.learn.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * first test filter
 *
 * @author zhengyuan
 * @since 2020/12/18
 */
public class Test1Filter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(Test1Filter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.info("process Test1Filter, filter uri is {}", request.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
