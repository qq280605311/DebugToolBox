package com.qinqi.debugtoolbox_no_op.log;

/**
 * Created by pscj on 2016/11/21.
 */
public interface LogAdapter {
    void d(String tag, String msg);
    void e(String tag, String msg);
    void i(String tag, String msg);
    void v(String tag, String msg);
    void w(String tag, String msg);
}
