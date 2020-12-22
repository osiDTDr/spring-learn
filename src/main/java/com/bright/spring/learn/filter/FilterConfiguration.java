package com.bright.spring.learn.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Data
@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<Test1Filter> test1Filter() {
        FilterRegistrationBean<Test1Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new Test1Filter());
        registrationBean.setOrder(1);   // 优先级
        registrationBean.addUrlPatterns("/*");  // 拦截url
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Test2Filter> test2Filter() {
        FilterRegistrationBean<Test2Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new Test2Filter());
        registrationBean.setOrder(2);   // 优先级
        registrationBean.addUrlPatterns("/*");  // 拦截url
        return registrationBean;
    }
}
