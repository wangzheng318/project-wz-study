package com.wz.study.rpc.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Method {
    /**
     * 是否幂等,默认支持幂等
     * @return
     */
    boolean idempotent() default true;

    /**
     * 超时时间
     * @return
     */
    long timeout() default 5000L;

    /**
     * 重试次数
     * @return
     */
    int retryTimes() default 0;
}
