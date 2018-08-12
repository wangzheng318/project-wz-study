package com.wz.study.rpc.framework.transportation.dto;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 1834016017660335123L;
    private boolean isSuccess;
    private String errorCode;
    private String errorMessage;
    private Object data;

    public RpcResponse() {
        //默认操作成功
        this.isSuccess = true;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
