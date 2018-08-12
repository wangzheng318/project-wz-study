package com.wz.study.rpc.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Contract {
    /**
     * 对外发布的服务的接口地址
     */
    Class<?> value();

    /**
     * 版本号
     * @return
     */
    String version();
}
