package com.ptb.uranus.manager.result;


import com.alibaba.fastjson.JSON;

/**
 * JSONResult : Response JSONResult for RESTful,封装返回JSON格式的数据
 *
 * @author StarZou
 * @since 2014年5月26日 上午10:51:46
 */

public class JSONResult<T> extends Result {

    private static final long serialVersionUID = 7880907731807860636L;

    public static JSONResult newErrorResult(int statusCode,String message) {
        return new JSONResult(null,message,false,statusCode);
    }

    public static JSONResult newErrorResult(String message) {
        return new JSONResult(null,message,false,0);
    }

    /**
     * 数据
     */
    private T data;


    public T getData() {
        return data;
    }

    public JSONResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public JSONResult() {
        super();
    }

    /**
     * 自定义返回的结果
     *
     * @param data
     * @param message
     * @param success
     */
    public JSONResult(T data, String message, boolean success) {
        this.data = data;
        super.setMessage(message);
        super.setSuccess(success);
    }

    public JSONResult(T data, String message, boolean success,int errorCode) {
        this.data = data;
        super.setMessage(message);
        super.setSuccess(success);
        super.setStatusCode(errorCode);
    }



    /**
     * 成功返回数据和消息
     *
     * @param data
     * @param message
     */
    public JSONResult(T data, String message) {
        this.data = data;
        super.setMessage(message);
        super.setSuccess(true);
    }



    /**
     * 成功返回数据
     *
     * @param data
     */
    public JSONResult(T data) {
        this.data = data;
        super.setSuccess(true);
    }


    public String toJson() {
        return JSON.toJSONString(this);
    }
}