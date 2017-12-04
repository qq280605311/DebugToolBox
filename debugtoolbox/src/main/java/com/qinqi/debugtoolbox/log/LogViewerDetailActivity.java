package com.qinqi.debugtoolbox.log;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;
import com.qinqi.debugtoolbox.util.JsonFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import me.codeboy.android.aligntextview.AlignTextView;

/**
 * Created by qinqi on 2016/11/23.
 */

public class LogViewerDetailActivity extends Activity {
    public static final String TAG = "LogViewerDetailActivity";

    private TextView mStateTV;
    private TextView mTimeTV;
    private TextView mPidTV;
    private TextView mTagTV;
    private AlignTextView mMsgTV;

    private DebugBoxLog mLogBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);

        setTitle("LogViewerDetail");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mLogBean = (DebugBoxLog) getIntent().getSerializableExtra(TAG);

        initView();
        initMembers();
    }

    private void initView() {
        mStateTV = (TextView) findViewById(R.id.tv_log_detail_state);
        mTimeTV = (TextView) findViewById(R.id.tv_log_detail_time);
        mPidTV = (TextView) findViewById(R.id.tv_log_detail_pid);
        mTagTV = (TextView) findViewById(R.id.tv_log_detail_tag);
        mMsgTV = (AlignTextView) findViewById(R.id.tv_log_detail_msg);
    }

    private void initMembers() {
        if (!DebugToolBoxUtils.isEmpty(mLogBean)) {
            if (mLogBean.getState() == DebugBoxLog.STATE_D) {
                mStateTV.setText("log state: Debug");
                mStateTV.setTextColor(getResources().getColor(R.color.color_006fba));
                mTimeTV.setTextColor(getResources().getColor(R.color.color_006fba));
                mPidTV.setTextColor(getResources().getColor(R.color.color_006fba));
                mTagTV.setTextColor(getResources().getColor(R.color.color_006fba));
                mMsgTV.setTextColor(getResources().getColor(R.color.color_006fba));
            } else if (mLogBean.getState() == DebugBoxLog.STATE_E) {
                mStateTV.setText("log state: Error");
                mStateTV.setTextColor(getResources().getColor(R.color.color_ff0000));
                mTimeTV.setTextColor(getResources().getColor(R.color.color_ff0000));
                mPidTV.setTextColor(getResources().getColor(R.color.color_ff0000));
                mTagTV.setTextColor(getResources().getColor(R.color.color_ff0000));
                mMsgTV.setTextColor(getResources().getColor(R.color.color_ff0000));
            } else if (mLogBean.getState() == DebugBoxLog.STATE_I) {
                mStateTV.setText("log state: Info");
                mStateTV.setTextColor(getResources().getColor(R.color.color_000000));
                mTimeTV.setTextColor(getResources().getColor(R.color.color_000000));
                mPidTV.setTextColor(getResources().getColor(R.color.color_000000));
                mTagTV.setTextColor(getResources().getColor(R.color.color_000000));
                mMsgTV.setTextColor(getResources().getColor(R.color.color_000000));
            } else if (mLogBean.getState() == DebugBoxLog.STATE_V) {
                mStateTV.setText("log state: Verbose");
                mStateTV.setTextColor(getResources().getColor(R.color.color_48bb31));
                mTimeTV.setTextColor(getResources().getColor(R.color.color_48bb31));
                mPidTV.setTextColor(getResources().getColor(R.color.color_48bb31));
                mTagTV.setTextColor(getResources().getColor(R.color.color_48bb31));
                mMsgTV.setTextColor(getResources().getColor(R.color.color_48bb31));
            } else if (mLogBean.getState() == DebugBoxLog.STATE_W) {
                mStateTV.setText("log state: Warn");
                mStateTV.setTextColor(getResources().getColor(R.color.color_ec732b));
                mTimeTV.setTextColor(getResources().getColor(R.color.color_ec732b));
                mPidTV.setTextColor(getResources().getColor(R.color.color_ec732b));
                mTagTV.setTextColor(getResources().getColor(R.color.color_ec732b));
                mMsgTV.setTextColor(getResources().getColor(R.color.color_ec732b));
            } else {//默认为 log.I
                mStateTV.setText("log state: Info");
                mStateTV.setTextColor(getResources().getColor(R.color.color_000000));
                mTimeTV.setTextColor(getResources().getColor(R.color.color_000000));
                mPidTV.setTextColor(getResources().getColor(R.color.color_000000));
                mTagTV.setTextColor(getResources().getColor(R.color.color_000000));
                mMsgTV.setTextColor(getResources().getColor(R.color.color_000000));
            }

            mTimeTV.setText("time:" + DebugToolBoxUtils.formatDateString(new Date(mLogBean.getTime()), "yyyy-MM-dd HH:mm:ss:SSS"));
            mPidTV.setText("pid:" + mLogBean.getPid());
            mTagTV.setText("tag:" + mLogBean.getTag());
            String msg = mLogBean.getMessage();
            if(msg.startsWith("{") && msg.endsWith("}")){
                mMsgTV.setText("msg:" + JsonFormat.format(msg));
            }else{
                mMsgTV.setText("msg:" + msg);
            }
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
//        intent.putExtra(Intent.EXTRA_TEXT, mLogBean.toString());
//        startActivity(Intent.createChooser(intent, getString(R.string.debug_box_ntd_share_with)));

        BufferedWriter bw = null;
        try {
            File file = DebugToolBoxUtils.getFileDir("log");
            file = new File(file, "log_" + DebugToolBoxUtils.formatDateString(new Date(), "yyyyMMdd_HHmmss")
                    + ".log");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            bw.write(mLogBean.toString());
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
