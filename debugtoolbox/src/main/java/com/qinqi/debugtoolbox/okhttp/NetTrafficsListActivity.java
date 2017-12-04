package com.qinqi.debugtoolbox.okhttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.manager.DbManager;
import com.qinqi.debugtoolbox.task.QueryNetAsyncTask;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;
import com.qinqi.debugtoolbox.view.AmazingListView;

/**
 * Created by qinqi on 2016/11/15.
 */
public class NetTrafficsListActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG = "NetTrafficsListActivity";
    private AmazingListView mNetTrafficsALV;

    private NetTrafficsAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_traffics_list);

        setTitle("NetTraffics");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        initMembers();
    }

    private void init() {
        mNetTrafficsALV = (AmazingListView) findViewById(R.id.alv_network_traffics_show);
    }

    private void initMembers() {
        mProgressDialog = new ProgressDialog(NetTrafficsListActivity.this);
        mProgressDialog.setMessage("加载中，请稍等。。。");

        mNetTrafficsALV.setPinnedHeaderView(LayoutInflater.from(this).inflate(R.layout.item_network_traffics_header, mNetTrafficsALV, false));

        mAdapter = new NetTrafficsAdapter(this);
        mNetTrafficsALV.setAdapter(mAdapter);
        mNetTrafficsALV.setOnItemClickListener(this);

        initData();
    }

    private void initData() {
        QueryNetAsyncTask task = new QueryNetAsyncTask(mAdapter, mProgressDialog);
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Object obj = parent.getItemAtPosition(position);
        if (obj instanceof NetTraffics) {
            NetTraffics traffics = (NetTraffics) obj;
            if (!DebugToolBoxUtils.isEmpty(traffics)) {
                Intent intent = new Intent(NetTrafficsListActivity.this, NetTrafficsDetailActivity.class);
                intent.putExtra(NetTrafficsDetailActivity.DATA_NET_TRAFFICS_ID, traffics.getId());
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_net_traffics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.debug_box_net_traffics_delete) {
            Toast.makeText(NetTrafficsListActivity.this, "clear net traffics db", Toast.LENGTH_SHORT).show();
            DbManager.getInstance().clearNetTrafficsDb();
            initData();
        }
        return super.onOptionsItemSelected(item);
    }
}
