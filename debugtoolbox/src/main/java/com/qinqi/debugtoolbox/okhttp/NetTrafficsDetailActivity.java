package com.qinqi.debugtoolbox.okhttp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.log.Logger;
import com.qinqi.debugtoolbox.manager.DbManager;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;
import com.qinqi.debugtoolbox.util.JsonFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by qinqi on 2016/11/17.
 */
public class NetTrafficsDetailActivity extends Activity {
    public static String DATA_NET_TRAFFICS_ID = "DATA_NET_TRAFFICS_ID";

    private TextView mMethodTV;
    private TextView mRequestHeaderTV;
    private TextView mRequestBodyTV;
    private TextView mResponseCodeTV;
    private TextView mResponseHeaderTV;
    private TextView mResponseBodyTV;

    private NetTraffics mTraffics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_traffics_detail);

        setTitle("NetTrafficsDetail");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(DATA_NET_TRAFFICS_ID, 0);

        initView();
        initMembers(id);
    }

    private void initView() {
        mMethodTV = (TextView) findViewById(R.id.tv_ntd_method);
        mRequestHeaderTV = (TextView) findViewById(R.id.tv_ntd_request_header);
        mRequestBodyTV = (TextView) findViewById(R.id.tv_ntd_request_body);
        mResponseCodeTV = (TextView) findViewById(R.id.tv_ntd_response_code);
        mResponseHeaderTV = (TextView) findViewById(R.id.tv_ntd_response_header);
        mResponseBodyTV = (TextView) findViewById(R.id.tv_ntd_response_body);
    }

    private void initMembers(int id) {
        mTraffics = DbManager.getInstance().queryNetTrafficsById(id);
        if (!DebugToolBoxUtils.isEmpty(mTraffics)) {
            mMethodTV.setText(mTraffics.getMethod() + "  " + mTraffics.getUrl());
            if (!DebugToolBoxUtils.isEmpty(mTraffics.getRequestHeader())) {
                mRequestHeaderTV.setVisibility(View.VISIBLE);
                mRequestHeaderTV.setText(mTraffics.getRequestHeader());
            } else {
                mRequestHeaderTV.setVisibility(View.GONE);
            }
            if (!DebugToolBoxUtils.isEmpty(mTraffics.getRequestBody())) {
                mRequestBodyTV.setVisibility(View.VISIBLE);
                if (mTraffics.getRequestMediaType().contains("json")) {
                    mRequestBodyTV.setText(JsonFormat.format(mTraffics.getRequestBody()));
                } else {
                    mRequestBodyTV.setText(mTraffics.getRequestBody());
                }
            } else {
                mRequestBodyTV.setVisibility(View.GONE);
            }
            mResponseCodeTV.setText(mTraffics.getProtocol().toUpperCase() + " " + mTraffics.getCode() + " " + mTraffics.getMessage());
            mResponseHeaderTV.setText(mTraffics.getResponseHeader());

            if (mTraffics.getResponseMediaType().contains("json")) {
                mResponseBodyTV.setText(JsonFormat.format(mTraffics.getResponseBody()));
            } else {
                mResponseBodyTV.setText(mTraffics.getResponseBody());
            }
        } else {
            Toast.makeText(this, "未找到 NetTraffics", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_net_traffics_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.debug_box_net_traffics_detail_share) {
            shareBlock();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareBlock() {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, mTraffics.toShare());
//        startActivity(Intent.createChooser(intent, getString(R.string.debug_box_ntd_share_with)));

        BufferedWriter bw = null;

        try {
            File file = DebugToolBoxUtils.getFileDir("netTraffics");
            file = new File(file, "net_traffics_" + DebugToolBoxUtils.formatDateString(new Date(), "yyyyMMdd_HHmmss")
                    + ".log");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            bw.write(mTraffics.toShare());
            bw.flush();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/octet-stream");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
            startActivity(Intent.createChooser(intent, getString(R.string.debug_box_ntd_share_with)));

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("NetTrafficsDetailActivity",e.getMessage());
            Toast.makeText(this,"未知错误，分享失败", Toast.LENGTH_SHORT).show();
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
}
