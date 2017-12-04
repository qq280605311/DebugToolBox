package com.qinqi.debugtoolbox.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 2016/6/28.
 */
public class PermissionManager {

    public static int PERMISSION_REQUEST_CODE = 0;
    public static String STR_CONTANT = "您已经关闭了通讯录 访问权限,为了保证此功能正常使用,请前往系统设置页面开启!";
    public static String STR_SAVE = "您已经关闭了存储空间 访问权限,为了保证功能正常使用,请前往系统设置页面开启!";
    static PermissionManager _instance = null;

    public static synchronized PermissionManager getInstance() {
        if (_instance == null) {
            _instance = new PermissionManager();
        }
        return _instance;
    }

    public static void createDialog(String msg, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PermissionManager.getInstance().startAppSettings(context);
                System.exit(0);
            }
        });
        builder.create().show();
    }

    public static void createDialogCanCancle(final Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(STR_CONTANT);
        builder.setTitle("提示");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PermissionManager.getInstance().startAppSettings(context);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 获取需要请求的权限
     */
    public List<String> getRequestPermissions(String[] permissions, Activity activity) {
        List<String> listPermissions = new ArrayList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (lacksPermission(activity.getApplicationContext(), permission)) {
                    listPermissions.add(permission);
                }
            }
        } else {
            for (String permission : permissions) {
                if (!isHasPermission(activity.getApplicationContext(), permission)) {
                    listPermissions.add(permission);
                }
            }
        }
        return listPermissions;
    }


    /**
     * 判断是否缺少某个权限>=6.0
     */
    private boolean lacksPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(permission) ==
                    PackageManager.PERMISSION_DENIED;
        } else {
            return false;
        }
    }

    /**
     * 检查是否缺少某权限<6.0
     */
    public boolean isHasPermission(Context context, String perStr) {
        PackageManager pm = context.getPackageManager();
        return (PackageManager.PERMISSION_GRANTED == pm.checkPermission(perStr, context.getPackageName()));
    }

    /**
     * 请求权限
     */
    public void requestPermissions(Activity activity, List<String> listPer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(listPer.toArray(new String[listPer.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 启动应用的设置,进入手动配置权限页面
     */
    public void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

}
