package com.bright.spring.learn.cas.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口身份认证注解类，用于校验请求方接口身份信息。通过校验接口参数{@code token}验证请求身份是否合法。
 *
 * @author zhengyuan
 * @since 2021/01/05
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestAuthValidator {

    /**
     * 判断是否需要校验接口身份信息
     *
     * @return true: 接口需要做校验
     */
    boolean valid() default false;

    /**
     * 是否默认提供双向认证标记，默认为开启
     *
     * @return true: 需要双向认证
     */
    boolean twoWay() default true;
}