package com.qinqi.debugtoolbox.deviceinfo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.TextView;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;

import static com.qinqi.debugtoolbox.util.DebugToolBoxUtils.kCpuInfoMaxFreqFilePath;

/**
 * Created by qinqi on 2017/7/6.
 */
public class DeviceInformationActivity extends Activity {
    private TextView mPhoneBrand;               //手机品牌
    private TextView mPhoneModel;               //手机型号
    private TextView mSystemName;               //系统名
    private TextView mBootloader;               //Bootloader
    private TextView mSerial;                   //Serial
    private TextView mHardware;                 //Hardware
    private TextView mAndroidEdition;           //android 版本
    private TextView mCPUModel;                 //cpu 型号
    private TextView mCPUNum;                   //cpu 个数
    private TextView mCPUHz;                    //cpu 频率
    private TextView mIMEI;                     //IMEI
    private TextView mIMSI;                     //IMSI
    private TextView mMemory;                   //内存大小
    private TextView mScreenResolution;         //屏幕分辨率
    private TextView mScreenDensity;            //屏幕密度
    private TextView mSystemVersionNum;         //系统版本编号
    private TextView mMACAddress;               //MAC 地址
    private TextView mOperator;                 //运营商

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        setTitle("Device Information");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initMembers();
    }

    private void initView() {
        mPhoneBrand = (TextView) findViewById(R.id.tv_device_info_phone_brand);
        mPhoneModel = (TextView) findViewById(R.id.tv_device_info_phone_model);
        mSystemName = (TextView) findViewById(R.id.tv_device_info_system_name);
        mBootloader = (TextView) findViewById(R.id.tv_device_info_bootloader);
        mSerial = (TextView) findViewById(R.id.tv_device_info_serial);
        mHardware = (TextView) findViewById(R.id.tv_device_info_hardware);
        mAndroidEdition = (TextView) findViewById(R.id.tv_device_info_android_edition);
        mCPUModel = (TextView) findViewById(R.id.tv_device_info_cpu_model);
        mCPUNum = (TextView) findViewById(R.id.tv_device_info_cpu_num);
        mCPUHz = (TextView) findViewById(R.id.tv_device_info_cpu_hz);
        mIMEI = (TextView) findViewById(R.id.tv_device_info_imei);
        mIMSI = (TextView) findViewById(R.id.tv_device_info_imsi);
        mMemory = (TextView) findViewById(R.id.tv_device_info_memory);
        mScreenResolution = (TextView) findViewById(R.id.tv_device_info_screen_resolution);
        mScreenDensity = (TextView) findViewById(R.id.tv_device_info_screen_density);
        mSystemVersionNum = (TextView) findViewById(R.id.tv_device_info_system_version_num);
        mMACAddress = (TextView) findViewById(R.id.tv_device_info_mac_address);
        mOperator = (TextView) findViewById(R.id.tv_device_info_operator);
    }

    private void initMembers() {
        mPhoneBrand.setText(Build.BRAND);
        mPhoneModel.setText(Build.MODEL);
        mSystemName.setText(Build.DISPLAY);
        mBootloader.setText(Build.BOOTLOADER);
        mSerial.setText(Build.SERIAL);
        mHardware.setText(Build.HARDWARE);
        mAndroidEdition.setText(Build.VERSION.RELEASE);

        mCPUModel.setText(DebugToolBoxUtils.getCpuName());
        mCPUNum.setText(String.valueOf(DebugToolBoxUtils.getNumCores()));
        mCPUHz.setText(String.valueOf(DebugToolBoxUtils.getCpuFreq(kCpuInfoMaxFreqFilePath)));

        TelephonyManager tm = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mIMEI.setText(!DebugToolBoxUtils.isEmpty(tm.getDeviceId()) ? tm.getDeviceId() : "unknown");
            mIMSI.setText(!DebugToolBoxUtils.isEmpty(tm.getSubscriberId()) ? tm.getSubscriberId() : "unknown");
        } else {
            mIMEI.setText("unknown");
            mIMSI.setText("unknown");
        }

        mMemory.setText(DebugToolBoxUtils.getTotalRam());
        mScreenResolution.setText(DebugToolBoxUtils.getScreenResolution(this));
        mScreenDensity.setText(DebugToolBoxUtils.getScreenDensity(this));
        mSystemVersionNum.setText(Build.ID);
        mMACAddress.setText(DebugToolBoxUtils.getLocalMacAddressFromIp());
        mOperator.setText(DebugToolBoxUtils.getOperatorName(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
