package com.qinqi.debugtoolbox;

import android.app.Application;

import net.panatrip.debugtoolbox.manager.DebugToolBoxManager;

import okhttp3.OkHttpClient;

/**
 * Created by qinqi on 2016/11/9.
 */

public class MyApplication extends Application {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void onCreate() {
        super.onCreate();
        DBUtil.InitDB(this);

        DebugToolBoxManager.init(this)
                .setOkhttpLogger(mOkHttpClient)
                .setLogger(new CustomLogAdapter())
                .run();
    }

    public static OkHttpClient getOkHttpClientInstance() {
       return  mOkHttpClient;
    }
}
