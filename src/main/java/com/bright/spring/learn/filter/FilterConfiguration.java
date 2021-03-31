package com.bright.spring.learn.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<ProduceTokenFilter> test1Filter() {
        FilterRegistrationBean<ProduceTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ProduceTokenFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);   // 优先级
        registrationBean.addUrlPatterns("/*");  // 拦截url
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<TokenFilter> test2Filter() {
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenFilter());
        registrationBean.setOrder(2);   // 优先级
        registrationBean.addUrlPatterns("/*");  // 拦截url
        return registrationBean;
    }
}
