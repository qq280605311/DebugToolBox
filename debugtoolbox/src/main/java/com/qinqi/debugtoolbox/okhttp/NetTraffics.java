package com.qinqi.debugtoolbox.okhttp;


import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;

/**
 * 网络请求数据对象类
 * Created by qinqi on 2016/11/15.
 */
public class NetTraffics {
    private int id;
    private long time;
    private String url;
    private int elapsedTime;
    private String method;
    private String requestHeader;
    private String requestBody;
    private String requestMediaType;
    private int code;
    private String message;
    private String protocol;
    private String responseBody;
    private String responseHeader;
    private String responseMediaType;
    private long responseContentLength;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRequestMediaType() {
        return requestMediaType;
    }

    public void setRequestMediaType(String requestMediaType) {
        this.requestMediaType = requestMediaType;
    }

    public String getResponseMediaType() {
        return responseMediaType;
    }

    public void setResponseMediaType(String responseMediaType) {
        this.responseMediaType = responseMediaType;
    }

    public long getResponseContentLength() {
        return responseContentLength;
    }

    public void setResponseContentLength(long responseContentLength) {
        this.responseContentLength = responseContentLength;
    }

    @Override
    public String toString() {
        return "NetTraffics{" +
                "id=" + id +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", elapsedTime=" + elapsedTime +
                ", method='" + method + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", requestMediaType='" + requestMediaType + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", protocol='" + protocol + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", responseHeader='" + responseHeader + '\'' +
                ", responseMediaType='" + responseMediaType + '\'' +
                ", responseContentLength='" + responseContentLength + '\'' +
                '}';
    }

    public String toShare() {
        String request_header = "";
        String request_body = "\n";
        String response_body = responseBody;

        if (!DebugToolBoxUtils.isEmpty(requestHeader)) {
            request_header = requestHeader + "\n";
        }

        if (!DebugToolBoxUtils.isEmpty(requestBody)) {
            request_body = requestBody + "\n";
        }

//        if(!DebugToolBoxUtils.isEmpty(responseMediaType) && responseMediaType.contains("json")){
//            response_body = JsonFormat.format(responseMediaType);
//        }

        return method + " " + url + "\n" +
                request_header +
                request_body +
                protocol.toUpperCase() + " " + code + " " + message + "\n" +
                responseHeader + "\n" +
                response_body + "\n";
    }
}
