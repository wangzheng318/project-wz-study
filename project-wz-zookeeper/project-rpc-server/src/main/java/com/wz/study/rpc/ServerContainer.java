package com.wz.study.rpc;

import com.wz.study.rpc.framework.Exporter;
import com.wz.study.rpc.framework.registry.Register;
import com.wz.study.rpc.framework.registry.impl.RegisterCenterImpl;

public class ServerContainer {

    public static void main(String[] args) {
        String scanPackage = "com.wz.study.rpc.server.impl";
        Register register = new RegisterCenterImpl();
        String serviceAddress = "127.0.0.1:8083";
        Exporter exporter = new Exporter(register, serviceAddress);
        exporter.publish(scanPackage);
    }
}
