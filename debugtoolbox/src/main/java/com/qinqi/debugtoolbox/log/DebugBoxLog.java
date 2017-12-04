package com.qinqi.debugtoolbox.log;

import android.os.Process;

import java.io.Serializable;

/**
 * Created by qinqi on 2016/11/17.
 */
public class DebugBoxLog implements Serializable {
    public static final int STATE_D = 1;
    public static final int STATE_E = 2;
    public static final int STATE_I = 3;
    public static final int STATE_V = 4;
    public static final int STATE_W = 5;
    private static int PID = Process.myPid();

    private int pid;
    private long time;
    private String tag;
    private String message;
    private int state;

    public static DebugBoxLog getDebugBoxLog(String tag, String msg, int state) {
        DebugBoxLog log = new DebugBoxLog();
        log.setPid(PID);
        log.setTime(System.currentTimeMillis());
        log.setTag(tag);
        log.setMessage(msg);
        log.setState(state);
        return log;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DebugBoxLog{" +
                "pid=" + pid +
                ", time=" + time +
                ", tag='" + tag + '\'' +
                ", message='" + message + '\'' +
                ", state=" + state +
                '}';
    }
}
