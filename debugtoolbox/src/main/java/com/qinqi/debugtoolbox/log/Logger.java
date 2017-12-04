package com.qinqi.debugtoolbox.log;

import android.os.Handler;

import com.qinqi.debugtoolbox.task.InsertDBToLogAsyncTask;

/**
 * Created by pscj on 2016-04-12.
 */
public class Logger {
    static final boolean LOG_ENABLE = true;
    static LogAdapter logcat = new DefaultLogAdapter();
    static Handler handler = new Handler();
    public static void initLogcat(LogAdapter adapter){
        logcat = adapter;
    }
    public static void d(String tag, String msg) {
        if(LOG_ENABLE){
            if(logcat != null) {
                logcat.d(tag, msg);
                saveLog(tag,msg,DebugBoxLog.STATE_D);
            }
        }
    }

    public static void e(String tag, String msg) {
        if(LOG_ENABLE){
            if(logcat != null) {
                logcat.e(tag, msg);
                saveLog(tag,msg,DebugBoxLog.STATE_E);
            }
        }
    }
    public static void i(String tag, String msg) {
        if(LOG_ENABLE){
            if(logcat != null) {
                logcat.i(tag, msg);
                saveLog(tag,msg,DebugBoxLog.STATE_I);
            }
        }
    }
    public static void v(String tag, String msg) {
        if(LOG_ENABLE){
            if(logcat != null) {
                logcat.v(tag, msg);
                saveLog(tag,msg,DebugBoxLog.STATE_V);
            }
        }
    }
    public static void w(String tag, String msg) {
        if(LOG_ENABLE){
            if(logcat != null) {
                logcat.w(tag, msg);
                saveLog(tag,msg,DebugBoxLog.STATE_W);
            }
        }
    }

    private static void saveLog(final String tag , final String msg, final int state){
        DebugBoxLog log = DebugBoxLog.getDebugBoxLog(tag,msg,state);

        InsertDBToLogAsyncTask task = new InsertDBToLogAsyncTask();
        task.execute(log);
    }
}
