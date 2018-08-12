package com.wz.study.rpc.framework.registry;

public interface Register {
    /**
     * 注册中心，注册服务信息
     * @param serviceName 服务名称，一般是接口名+版本号
     * @param serverAddress 服务地址，一般是ip:port
     */
    void register(String serviceName, String serverAddress);
}
