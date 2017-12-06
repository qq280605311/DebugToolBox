package com.qinqi.debugtoolbox_no_op.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.qinqi.debugtoolbox_no_op.log.Logger;
import com.qinqi.debugtoolbox_no_op.util.DebugToolBoxUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Locale;

/**
 * 收集崩溃
 * Created by pscj on 2015-07-29.
 */
public class CrashReportsHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    public void init(Context context) {
        mContext = context;
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        saveLog(ex);
        mDefaultCrashHandler.uncaughtException(thread, ex);
    }

    /**
     * 保存异常使用的方法
     *
     * @param ex
     */
    public void saveLog(Throwable ex, Object... extParams) {
        Logger.e("error", ex.getMessage());
        BufferedWriter bw = null;
        try {
            File file = DebugToolBoxUtils.getFileDir("crash");
            file = new File(file, "crash_" + DebugToolBoxUtils.formatDateString(new Date(), "yyyyMMdd_HHmmss")
                    + ".log");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            bw.write(dealErrorMsg(ex, file, extParams));
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

//    private void showNotify() {
//        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
//        mBuilder.setContentTitle("Crash " + DebugToolBoxUtils.formatDateString(new Date(), "MM-dd hh:mm:ss:SSS"));
//        mBuilder.setContentText("Click for more detail");
//        Intent notificationIntent = new Intent(mContext, CrashReportsActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
//        mBuilder.setContentIntent(contentIntent)
//                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
//                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
//                .setOngoing(true)
//                .setSmallIcon(R.drawable.drw_notification_crash_icon)
//                .setDefaults(
//                        Notification.DEFAULT_LIGHTS |
//                                Notification.DEFAULT_VIBRATE |
//                                Notification.DEFAULT_SOUND);
//        Notification notify = mBuilder.build();
//        notify.flags = Notification.FLAG_AUTO_CANCEL;
//        mNotificationManager.notify(1, notify);
//    }

//    private File getCrashDir() {
//        String dir = "crash";
//        File file = new File(DebugToolBoxUtils.getLocalDir(), dir);
//        if (!file.getParentFile().exists()) {
//            file.getParentFile().mkdirs();
//        }
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        return file;
//    }

    private String dealErrorMsg(Throwable ex, File file, Object... extParams)
            throws PackageManager.NameNotFoundException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();

        PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        sb.append("packageName=").append(packageInfo.packageName);
        sb.append("\n");
        sb.append("datetime=").append(DebugToolBoxUtils.formatDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        sb.append("\n");
        sb.append("versionName=").append(packageInfo.versionName);
        sb.append("\n\n");
        sb.append("versionCode=").append(packageInfo.versionCode);
        sb.append("\n");


        Locale locale = null;
        try {
            locale = mContext.getResources().getConfiguration().locale;
        } catch (Exception e) {
        }
        // 手机硬件信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);// 暴力反射获取私有字段
            sb.append(field.getName().toLowerCase(locale));
            sb.append("=");
            sb.append(field.get(null).toString());
            sb.append("\n");
        }
        sb.append("country=").append(locale.getCountry()).append("\n");
        sb.append("language=").append(locale.getLanguage()).append("\n");
        if (!DebugToolBoxUtils.isEmpty(extParams)) {
            for (Object line : extParams) {
                sb.append(String.valueOf(line));
                sb.append("\n");
            }
        }
        sb.append("\n");
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer, true));
        sb.append(writer.toString());
        return sb.toString();
    }

}
