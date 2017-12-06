package com.qinqi.debugtoolbox_no_op.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;


import com.qinqi.debugtoolbox_no_op.manager.DebugToolBoxManager;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * 工具类
 * Created by qinqi on 16/11/8.
 */
public class DebugToolBoxUtils {

    /**
     * 非空判断
     *
     * @param obj 传入参数
     * @return 是否非空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Collection<?>) {
            // 检查各种Collection是否为空(List,Queue,Set)
            return ((Collection<?>) obj).isEmpty();
        } else if (obj instanceof Map<?, ?>) {
            // 检查各种Map
            return ((Map<?, ?>) obj).isEmpty();
        } else if (obj instanceof CharSequence) {
            // 检查各种CharSequence
            return ((CharSequence) obj).length() == 0;
        } else if (obj.getClass().isArray()) {
            // 检查各种base array
            // return Array.getLength(obj) == 0;
            if (obj instanceof Object[]) {
                return ((Object[]) obj).length == 0;
            } else if (obj instanceof int[]) {
                return ((int[]) obj).length == 0;
            } else if (obj instanceof long[]) {
                return ((long[]) obj).length == 0;
            } else if (obj instanceof short[]) {
                return ((short[]) obj).length == 0;
            } else if (obj instanceof double[]) {
                return ((double[]) obj).length == 0;
            } else if (obj instanceof float[]) {
                return ((float[]) obj).length == 0;
            } else if (obj instanceof boolean[]) {
                return ((boolean[]) obj).length == 0;
            } else if (obj instanceof char[]) {
                return ((char[]) obj).length == 0;
            } else if (obj instanceof byte[]) {
                return ((byte[]) obj).length == 0;
            }
        }
        return false;
    }

    /**
     * 日期>>>>>>>>字符串
     *
     * @param date      日期
     * @param formatter 转换格式
     * @return 日期
     */
    public static String formatDateString(Date date, String formatter) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatter, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 保留小数点后几位
     *
     * @param value   数值
     * @param decimal 小数点后保留的位数
     * @return 转换后的小数
     */
    public static float getDecimalToFloat(double value, int decimal) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);

        return bd.floatValue();
    }

    /**
     * 获取版本名和版本号
     *
     * @param context
     * @return
     */
    public static String getVersionCodeAndName(Context context) {
        String versionCodeAndName = null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCodeAndName = packageInfo.versionName + "." + packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCodeAndName = "1.100.100.100";
        }

        return versionCodeAndName;
    }

    /**
     * 获取程序名
     *
     * @return
     */
    public static String getAppName() {
        String appName = "";
        PackageManager packageManager = DebugToolBoxManager.getHostContext().getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(DebugToolBoxManager.getHostContext().getPackageName(), 0);
            String packageName = packageManager.getApplicationLabel(applicationInfo).toString();
            if (!DebugToolBoxUtils.isEmpty(packageName)) {
                appName = packageManager.getApplicationLabel(applicationInfo).toString();
            } else {
                appName = DebugToolBoxManager.getHostContext().getPackageName();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appName;
    }

    /**
     * 获取文件路径
     *
     * @param dir
     * @return
     */
    public static File getFileDir(String dir) {
//        String dir = "crash";
        File file = new File(getLocalDir(), dir);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    /**
     * 获取文件本地路径
     *
     * @return
     */
    public static File getLocalDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return new File(Environment.getExternalStorageDirectory(),
                    "Android/data/" + DebugToolBoxManager.getHostContext().getPackageName() + "/");
        } else {
            return DebugToolBoxManager.getHostContext().getFilesDir();
        }
    }

    /**
     * 获取当日初始时间
     *
     * @param time
     * @return
     */
    public static long getDayStartTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * 获取网络操作耗时
     *
     * @param time
     * @return
     */
    public static String getElapsedTime(int time) {
        String elapsed_time = "";
        if (time > 9999) {
            elapsed_time = time / 1000 + "s";
        } else {
            elapsed_time = time + "ms";
        }
        return elapsed_time;
    }

    /**
     * 获取网络 ContentLength
     *
     * @param length
     * @return
     */
    public static String getLength(long length) {
//        int sub_index = 0;
        String show = "";
        if (length >= 1073741824) {
//            sub_index = String.valueOf((float) length / 1073741824).indexOf(".");
//            show = ((float) length / 1073741824 + "000").substring(0, sub_index + 3) + "GB";

            show = String.valueOf((int) (length / 1073741824) + "GB");
        } else if (length >= 1048576) {
//            sub_index = (String.valueOf((float) length / 1048576)).indexOf(".");
//            show = ((float) length / 1048576 + "000").substring(0, sub_index + 3) + "M";

            show = String.valueOf((int) (length / 1048576) + "M");
        } else if (length >= 1024) {
//            sub_index = (String.valueOf((float) length / 1024)).indexOf(".");
//            show = ((float) length / 1024 + "000").substring(0, sub_index + 3) + "K";

            show = String.valueOf((int) (length / 1024) + "K");
        } else if (length < 1024)
            show = String.valueOf(length) + "";

        return show;
    }
}
