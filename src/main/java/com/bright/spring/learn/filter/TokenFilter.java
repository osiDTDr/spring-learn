package com.bright.spring.learn.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * validate request header token
 *
 * @author zhengyuan
 * @since 2020/12/18
 */
public class TokenFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String token = ((HttpServletRequest) servletRequest).getHeader("Token");
        logger.debug("token is {}", token);
        if (validateToken(token)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean validateToken(String token) {
        return StringUtils.startsWith(token, "Token");
    }
}
