package com.qinqi.debugtoolbox.log;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.manager.DbManager;
import com.qinqi.debugtoolbox.task.QueryLogAsyncTask;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;
import com.qinqi.debugtoolbox.view.AmazingListView;

/**
 * Created by qinqi on 2016/11/23.
 */
public class LogViewerListActivtiy extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG = "LogViewerListActivity";
    private AmazingListView mLogViewerALV;

    private LogViewerAdapter mAdapter;
    private Handler mHandler;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_viewer);

        setTitle("LogViewer");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initMembers();
    }

    private void initView() {
        mLogViewerALV = (AmazingListView) findViewById(R.id.alv_log_viewer_show);
    }

    private void initMembers() {
        mProgressDialog = new ProgressDialog(LogViewerListActivtiy.this);
        mProgressDialog.setMessage("加载中，请稍等。。。");

        mHandler = new Handler();
        mAdapter = new LogViewerAdapter(this);
        mLogViewerALV.setPinnedHeaderView(LayoutInflater.from(this).inflate(R.layout.item_network_traffics_header, mLogViewerALV, false));
        mLogViewerALV.setOnItemClickListener(this);

        mLogViewerALV.setAdapter(mAdapter);

        initData();
    }

    private void initData() {
        QueryLogAsyncTask task = new QueryLogAsyncTask(mAdapter, mProgressDialog);
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Object obj = parent.getItemAtPosition(position);
        if (obj instanceof DebugBoxLog) {
            DebugBoxLog log = (DebugBoxLog) obj;
            if (!DebugToolBoxUtils.isEmpty(log)) {
                Intent intent = new Intent(LogViewerListActivtiy.this, LogViewerDetailActivity.class);
                intent.putExtra(LogViewerDetailActivity.TAG, log);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_log_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
        } else if (i == R.id.debug_box_log_viewer_refresh) {
            initData();
        } else if (i == R.id.debug_box_log_viewer_delete) {
            Toast.makeText(LogViewerListActivtiy.this, "clear net log db", Toast.LENGTH_SHORT).show();
            DbManager.getInstance().clearDebugBoxLog();
            initData();
        }
        return true;
    }

}
