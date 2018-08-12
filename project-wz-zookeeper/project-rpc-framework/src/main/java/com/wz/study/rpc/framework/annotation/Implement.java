package com.wz.study.rpc.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Implement {
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
