package com.wz.study.rpc.service;

import com.wz.study.rpc.framework.annotation.Contract;
import com.wz.study.rpc.framework.annotation.Method;
import sun.misc.Contended;

/**
 * rpc接口调用
 */
@Contract(name = "ServiceDemo",description = "服务示例")
public interface ServiceDemo {
    /**
     *
     * @param message
     * @return
     */
    @Method(idempotent = false,retryTimes = 3,timeout = 3000L)
    String echo(String message);
}
