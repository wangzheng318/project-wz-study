package com.wz.study.rpc.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {
    /**
     * 契约
     * @return
     */
    Class contract();

    /**
     * 版本号
     * @return
     */
    String version();
}
