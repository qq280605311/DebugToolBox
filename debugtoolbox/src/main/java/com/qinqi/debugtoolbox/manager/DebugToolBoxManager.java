package com.qinqi.debugtoolbox.manager;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.view.ViewConfiguration;

import com.github.moduth.blockcanary.BlockCanary;
import com.qinqi.debugtoolbox.DebugBoxHomeActivity;
import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.crash.CrashReportsHandler;
import com.qinqi.debugtoolbox.log.LogAdapter;
import com.qinqi.debugtoolbox.log.Logger;
import com.qinqi.debugtoolbox.okhttp.LoggingInterceptor;
import com.qinqi.debugtoolbox.util.AppBlockCanaryContext;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;
import com.squareup.leakcanary.LeakCanary;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;
import static android.support.v4.app.NotificationCompat.FLAG_ONGOING_EVENT;

/**
 * Created by qinqi on 2016/11/9.
 */
public class DebugToolBoxManager {
    private static Application hostApplication = null;

    public static Builder init(Application application) {
        hostApplication = application;
        return new Builder(application);
    }

    public static Application getHostContext() {
        return hostApplication;
    }

    private static void showNotify(Application application) {
        NotificationManager mNotificationManager = (NotificationManager) application.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(application);
        mBuilder.setContentTitle("DebugToolBox-" + DebugToolBoxUtils.getAppName());
        mBuilder.setContentText("Click here to enter the debugtoolbox");
        Intent notificationIntent = new Intent(application, DebugBoxHomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(application, 0, notificationIntent, 0);
        mBuilder.setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)
                .setSmallIcon(R.drawable.drw_notification_icon);
        Notification notify = mBuilder.build();
        notify.flags = FLAG_ONGOING_EVENT;
        mNotificationManager.notify(0, notify);
    }

    public static void openDebugToolBox(Context context) {
        context.startActivity(new Intent(context, DebugBoxHomeActivity.class));
    }

    public static class Builder {
        private Application application;

        private boolean crashEnable = true;
        private boolean strictEnable = false;
        private boolean leakCanaryEnable = true;
        private boolean blockCanaryEnable = true;

        private LogAdapter logAdapter = null;
        private OkHttpClient okHttpClient = null;
        private boolean showNotify = true;

        public Builder(Application app) {
            this.application = app;
        }

        public Builder enabledCrashLog(boolean enabled) {
            this.crashEnable = enabled;
            return this;
        }

        public Builder enabledStrictMode(boolean enabled) {
            this.strictEnable = enabled;
            return this;
        }

        public Builder enabledLeakCanary(boolean enabled) {
            this.leakCanaryEnable = enabled;
            return this;
        }

        public Builder enabledBlockCanary(boolean enabled) {
            this.blockCanaryEnable = enabled;
            return this;
        }

        public Builder setLogger(LogAdapter adapter) {
            this.logAdapter = adapter;
            return this;
        }

        public Builder setOkhttpLogger(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder showNotify(boolean showed) {
            this.showNotify = showed;
            return this;
        }

        public void run() {
            if (crashEnable) {
                new CrashReportsHandler().init(application);//崩溃模块初始化
            }
            if (strictEnable) {
                if (SDK_INT >= GINGERBREAD) {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                            .detectAll() //
                            .penaltyLog() //
                            .penaltyDeath() //
                            .build());
                }
            }
            if (leakCanaryEnable) {
                if (!LeakCanary.isInAnalyzerProcess(application)) {
                    LeakCanary.install(application);//内存泄漏模块初始化
                }
            }
            if (blockCanaryEnable) {
                BlockCanary.install(application, new AppBlockCanaryContext(application)).start();//阻塞模块初始化
            }
            //init db
            if (logAdapter != null || okHttpClient != null) {
                DbManager.getInstance().init(application);
            }

            if (okHttpClient != null) {
                List<Interceptor> list = new ArrayList<>();
                for (Interceptor item : okHttpClient.networkInterceptors()) {
                    list.add(item);
                }

                list.add(new LoggingInterceptor());
                try {
                    /*去除final修饰符的影响，将字段设为可修改的*/
                    Field modifiersField = OkHttpClient.class.getDeclaredField("networkInterceptors");
                    modifiersField.setAccessible(true);
                    modifiersField.set(okHttpClient, list);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (logAdapter != null) {
                Logger.initLogcat(logAdapter);
            }

            if (showNotify) {
                DebugToolBoxManager.showNotify(application);
            }

            displayMenuKey();
        }

        /**
         * 显示菜单键
         * 部分机器界面上无法显示出右上角的菜单键，此段代码可将其在右上角显示出来
         */
        private void displayMenuKey(){
            try {
                ViewConfiguration config = ViewConfiguration.get(application);
                Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
                if(menuKeyField != null) {
                    menuKeyField.setAccessible(true);
                    menuKeyField.setBoolean(config, false);
                }
            } catch (Exception ex) {
                // Ignore
            }
        }
    }
}
