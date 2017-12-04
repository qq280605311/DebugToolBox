package com.qinqi.debugtoolbox.log;

/**
 * Created by pscj on 2016/11/21.
 */

public class DefaultLogAdapter implements LogAdapter {
    @Override
    public void d(String tag, String msg) {
        android.util.Log.d(tag,msg);
    }

    @Override
    public void e(String tag, String msg) {
        android.util.Log.e(tag,msg);
    }

    @Override
    public void i(String tag, String msg) {
        android.util.Log.i(tag,msg);
    }

    @Override
    public void v(String tag, String msg) {
        android.util.Log.v(tag,msg);
    }

    @Override
    public void w(String tag, String msg) {
        android.util.Log.w(tag,msg);
    }
}
