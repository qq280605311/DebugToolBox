package com.qinqi.debugtoolbox.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.qinqi.debugtoolbox.manager.DebugToolBoxManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 工具类
 * Created by qinqi on 16/11/8.
 */
public class DebugToolBoxUtils {
    public final static String kCpuInfoMaxFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
    public final static String kCpuInfoMinFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
    public final static String kCpuInfoCurFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";

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

    /**
     * 获取cpu 频率（单位KHZ）
     *
     * @return
     */
    public static int getCpuFreq(String patch) {
        int result = 0;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(patch);
            br = new BufferedReader(fr);
            String text = br.readLine();
            result = Integer.parseInt(text.trim());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

        return result;
    }

    /**
     * 获取cpu 型号
     *
     * @return
     */
    public static String getCpuName() {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     * 获取 cpu 数量
     *
     * @return
     */
    public static int getNumCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取内存
     *
     * @return
     */
    public static String getTotalRam() {//GB
        String path = "/proc/meminfo";
        String firstLine = null;
        float totalRam = 0;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (firstLine != null) {
            totalRam = Float.valueOf(firstLine) / (1024 * 1024);
        }

        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(totalRam) + "GB";//返回1GB/2GB/3GB/4GB
    }

    /**
     * 根据IP获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromIp() {
        String mac_s = "unknown";
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp).append(":");
            } else {
                if (n != len - 1) {
                    hs = hs.append(stmp).append(":");
                } else {
                    hs = hs.append(stmp);
                }
            }
        }
        return String.valueOf(hs);
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取屏幕分辨率
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getScreenResolution(Activity context) {
        Point point = new Point();
        context.getWindowManager().getDefaultDisplay().getRealSize(point);
        return point.y + " * " + point.x;
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static String getScreenDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return String.valueOf(dm.density);
    }

    /**
     * 获取运营商名字
     */
    public static String getOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            String operator = tm.getSimOperator();
            if (operator != null) {
                if (operator.equals("46000") || operator.equals("46002")) {
                    return "中国移动";
                } else if (operator.equals("46001")) {
                    return "中国联通";
                } else if (operator.equals("46003")) {
                    return "中国电信";
                }
            }
        }
        return "unknown";
    }
}
