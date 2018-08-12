package com.wz.study.rpc.framework.transportation;

import com.wz.study.rpc.framework.transportation.dto.RpcRequest;

public interface Transport {
  Object send(RpcRequest rpcRequest);
}
