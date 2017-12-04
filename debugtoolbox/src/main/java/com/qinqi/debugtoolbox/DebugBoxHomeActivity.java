package com.qinqi.debugtoolbox;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import com.github.moduth.blockcanary.ui.DisplayActivity;
import com.qinqi.debugtoolbox.crash.CrashReportsActivity;
import com.qinqi.debugtoolbox.dbinspector.activities.DbInspectorActivity;
import com.qinqi.debugtoolbox.deviceinfo.DeviceInformationActivity;
import com.qinqi.debugtoolbox.log.LogViewerListActivtiy;
import com.qinqi.debugtoolbox.manager.PermissionManager;
import com.qinqi.debugtoolbox.okhttp.NetTrafficsListActivity;
import com.qinqi.debugtoolbox.option.OptionActivity;
import com.qinqi.debugtoolbox.util.DataCleanManager;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;

import java.util.List;

public class DebugBoxHomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_box_home);

        initTitle();
        initView();
        initPermission();
    }

    private void initView() {
        findViewById(R.id.ll_debug_box_developer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开发者选项
                startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
            }
        });
        findViewById(R.id.ll_debug_box_clear_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除数据

                AlertDialog.Builder builder = new AlertDialog.Builder(DebugBoxHomeActivity.this);
                builder.setCancelable(true);
                builder.setMessage("是否清除本地数据");
                builder.setTitle("注意");
                builder.setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        DataCleanManager.cleanApplicationData(DebugBoxHomeActivity.this);

//                        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });
        findViewById(R.id.ll_debug_box_database).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //database
                startActivity(new Intent(DebugBoxHomeActivity.this, DbInspectorActivity.class));
            }
        });
        findViewById(R.id.ll_debug_box_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Debug debug_box log
                startActivity(new Intent(DebugBoxHomeActivity.this, LogViewerListActivtiy.class));
            }
        });
        findViewById(R.id.ll_debug_box_sp_editor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //外部存储编辑器
            }
        });

        findViewById(R.id.ll_debug_box_leak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //泄露报告
                startActivity(new Intent(DebugBoxHomeActivity.this, com.squareup.leakcanary.internal.DisplayLeakActivity.class));
            }
        });
        findViewById(R.id.ll_debug_box_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //阻塞报告
                startActivity(new Intent(DebugBoxHomeActivity.this, DisplayActivity.class));
            }
        });
        findViewById(R.id.ll_debug_box_network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //网络报告
                startActivity(new Intent(DebugBoxHomeActivity.this, NetTrafficsListActivity.class));
            }
        });
        findViewById(R.id.ll_debug_box_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //崩溃报告
                startActivity(new Intent(DebugBoxHomeActivity.this, CrashReportsActivity.class));
            }
        });
        findViewById(R.id.ll_debug_box_device_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设备基础信息
                startActivity(new Intent(DebugBoxHomeActivity.this, DeviceInformationActivity.class));
            }
        });
        findViewById(R.id.ll_debug_box_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DebugBoxHomeActivity.this, OptionActivity.class));
            }
        });
    }

    private void initTitle() {
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = DebugToolBoxUtils.getAppName();

        if (!DebugToolBoxUtils.isEmpty(title)) {
            getActionBar().setSubtitle(title + " " + DebugToolBoxUtils.getVersionCodeAndName(this));
        }
    }

    private void initPermission() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        List<String> listPer = PermissionManager.getInstance().getRequestPermissions(permissions, DebugBoxHomeActivity.this);
        if (listPer.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermissionManager.getInstance().requestPermissions(DebugBoxHomeActivity.this, listPer);
            } else {
                for (String strPer : listPer) {
                    if (strPer.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) || strPer.equals(Manifest.permission.READ_PHONE_STATE)) {
                        PermissionManager.getInstance().createDialog(getString(R.string.permission_request_str), DebugBoxHomeActivity.this);
                    }
                }
            }
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_debug_box_home, menu);
//        return true;
//    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
