package com.bright.spring.learn.cas.interceptor;

import com.bright.spring.learn.cas.annotation.RestAuthValidator;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest接口身份认证拦截器，验证接口调用者是否合法，通过注解{@link RestAuthValidator}配置是否需要开启校验。
 *
 * @author zhengyuan
 * @since 2021/01/05
 */
public class RestAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod methodHandler = (HandlerMethod) handler;
        RestAuthValidator auth = methodHandler.getMethodAnnotation(RestAuthValidator.class);
        if (auth == null || !auth.valid()) { // 不进行身份校验
            return true;
        }
        // TODO: 2021/1/5 身份校验直接在这里做掉
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // do nothing
    }
}
