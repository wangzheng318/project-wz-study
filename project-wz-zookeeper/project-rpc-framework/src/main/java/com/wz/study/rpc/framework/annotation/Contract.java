package com.wz.study.rpc.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Contract {
    /**
     * 契约名称
     */
   String name();

    /**
     * 描述信息
     * @return
     */
   String description();
}
