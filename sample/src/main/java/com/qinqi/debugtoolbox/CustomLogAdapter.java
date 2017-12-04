package com.qinqi.debugtoolbox;

import com.orhanobut.logger.Logger;

import net.panatrip.debugtoolbox.log.LogAdapter;

/**
 * Created by pscj on 2016/11/21.
 */

public class CustomLogAdapter implements LogAdapter {
    @Override
    public void d(String tag, String msg) {
        Logger.t(tag).d(msg);
    }

    @Override
    public void e(String tag, String msg) {
        Logger.t(tag).e(msg);
    }

    @Override
    public void i(String tag, String msg) {
        Logger.t(tag).i(msg);
    }

    @Override
    public void v(String tag, String msg) {
        Logger.t(tag).v(msg);
    }

    @Override
    public void w(String tag, String msg) {
        Logger.t(tag).w(msg);
    }
}
