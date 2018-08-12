package com.wz.study.rpc.framework.transportation.impl;

import com.wz.study.rpc.framework.transportation.Transport;
import com.wz.study.rpc.framework.transportation.dto.RpcRequest;
import com.wz.study.rpc.framework.util.FileUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpTransport implements Transport {

    private String hostName;
    private int port;

    public TcpTransport(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    @Override
    public Object send(RpcRequest rpcRequest) {
        Socket socket = null;
        InputStream in = null;
        ObjectInputStream objectInputStream = null;
        OutputStream out = null;
        ObjectOutputStream objectOutputStream= null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(hostName,port));
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);

            in = socket.getInputStream();
            objectInputStream = new ObjectInputStream(in);
            return objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            FileUtil.close(objectInputStream);
            FileUtil.close(in);
            FileUtil.close(objectOutputStream);
            FileUtil.close(out);
        }
        return null;
    }
}
