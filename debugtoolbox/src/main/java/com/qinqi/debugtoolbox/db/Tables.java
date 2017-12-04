package com.qinqi.debugtoolbox.db;

/**
 * Created by qinqi on 2016/11/16.
 */
public class Tables {
    public static class NetTrafficsTable {
        public static final String TABLE_NAME = "network_traffics";
        public static class Field{
            public static final String ID = "_id";//主键id
            public static final String TIME = "time";//日志存储时间
            public static final String ELAPSED_TIME = "elapsed_time";//网络调用耗时
            public static final String URL = "url";
            public static final String METHOD = "method";
            public static final String REQUEST_HEADER = "request_header";
            public static final String REQUEST_BODY = "request_body";
            public static final String CODE = "code";
            public static final String RESPONSE_HEADER = "response_header";
            public static final String RESPONSE_BODY = "response_body";
            public static final String MESSAGE = "message";
            public static final String PROTOCOL = "protocol";
            public static final String REQUEST_MEDIA_TYPE = "request_media_type";
            public static final String RESPONSE_MEDIA_TYPE = "response_media_type";
            public static final String RESPONSE_CONTENT_LENGTH = "response_content_length";
        }
    }

    public static class DebugBoxLogTable {
        public static final String TABLE_NAME = "debugtoolbox_log";
        public static class Field{
            public static final String ID = "_id";
            public static final String PID = "pid";
            public static final String TIME = "time";
            public static final String TAG = "tag";
            public static final String MESSAGE = "message";
            public static final String STATE = "state";
        }
    }
}
