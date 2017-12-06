package com.qinqi.debugtoolbox_no_op.manager;

import android.app.Application;
import android.content.Context;

import com.qinqi.debugtoolbox_no_op.crash.CrashReportsHandler;
import com.qinqi.debugtoolbox_no_op.log.LogAdapter;

import okhttp3.OkHttpClient;

/**
 * Created by qinqi on 2016/11/9.
 */
public class DebugToolBoxManager {
    private static Application hostApplication = null;

    public static Builder init(Application application) {
        hostApplication = application;
        return new Builder(application);
    }
    public static Application getHostContext(){
        return hostApplication;
    }


    public static void openDebugToolBox(Context context) {
    }

    public static class Builder{
        private Application application;

        private boolean crashEnable = true;
        private LogAdapter logAdapter = null;
        private OkHttpClient okHttpClient = null;

        public Builder(Application app){
            application = app;
        }
        public Builder enabledCrashLog(boolean enabled) {
            this.crashEnable = enabled;
            return this;
        }

        public Builder enabledStrictMode(boolean enabled){
            return this;
        }
        public Builder enabledLeakCanary(boolean enabled){
            return this;
        }
        public Builder enabledBlockCanary(boolean enabled){ return this; }
        public Builder setLogger(LogAdapter adapter){
            return this;
        }
        public Builder setOkhttpLogger(OkHttpClient okHttpClient){
            return this;
        }
        public Builder showNotify(boolean showed){
            return this;
        }

        public void run() {
            if (crashEnable) {
                new CrashReportsHandler().init(application);//崩溃模块初始化
            }
        }
    }
}
