package com.qinqi.debugtoolbox.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.qinqi.debugtoolbox.manager.DbManager;
import com.qinqi.debugtoolbox.okhttp.NetTraffics;
import com.qinqi.debugtoolbox.okhttp.NetTrafficsAdapter;

import java.util.List;

/**
 * Created by qinqi on 2016/12/8.
 */

public class QueryNetAsyncTask extends AsyncTask<Void, Void, List<NetTraffics>> {

    private NetTrafficsAdapter adapter;
    private ProgressDialog progressDialog;

    public QueryNetAsyncTask(NetTrafficsAdapter netTrafficsAdapter, ProgressDialog dialog) {
        super();
        this.adapter = netTrafficsAdapter;
        this.progressDialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected List<NetTraffics> doInBackground(Void... params) {
        List<NetTraffics> list = DbManager.getInstance().queryAllNetTraffics();
        return list;
    }

    @Override
    protected void onPostExecute(List<NetTraffics> netTrafficses) {
        adapter.setData(netTrafficses);
        adapter.notifyDataSetChanged();

        progressDialog.dismiss();
    }
}
