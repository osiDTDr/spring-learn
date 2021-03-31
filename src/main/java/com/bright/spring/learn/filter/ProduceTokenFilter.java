package com.bright.spring.learn.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * add Token to request header
 *
 * @author zhengyuan
 * @since 2020/12/18
 */
public class ProduceTokenFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ProduceTokenFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
        // 获得请求参数中的token值
        String token = ((HttpServletRequest) servletRequest).getHeader("Token");
        logger.debug("token from header is {}", token);
        if (StringUtils.isEmpty(token)) {
            // 如果请求中没有这个参数，则进行过滤加一个header头
            requestWrapper.addHeader("Token", generateToken());
            filterChain.doFilter(requestWrapper, servletResponse); // Goes to default servlet.
        }
        filterChain.doFilter(servletRequest, servletResponse); // Goes to default servlet.
    }

    private String generateToken() {
        return "Token_".concat(String.valueOf(System.currentTimeMillis()));
    }
}
