package com.sunyw.xyz.annotation;

import java.lang.annotation.*;

/**
 * routeLimiter注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RouteLimiter {

    /**
     * 每秒产生的令牌数量
     *
     * @return int
     */
    int repleniShrate() default 0;

    /**
     * 桶内令牌容量
     *
     * @return int
     */
    int burstCapacity() default 0;

    /**
     * 每次获取令牌数量
     *
     * @return int
     */
    int acquiredQuantity() default 0;
}
