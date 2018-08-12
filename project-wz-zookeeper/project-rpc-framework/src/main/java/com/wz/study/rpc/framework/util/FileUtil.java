package com.wz.study.rpc.framework.util;

import java.io.Closeable;

public class FileUtil {

    public static void close(Closeable closeable){
        if(null != closeable){
            try {
                closeable.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
