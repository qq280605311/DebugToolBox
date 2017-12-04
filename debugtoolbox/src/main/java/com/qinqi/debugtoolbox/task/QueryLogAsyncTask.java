package com.qinqi.debugtoolbox.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.qinqi.debugtoolbox.log.DebugBoxLog;
import com.qinqi.debugtoolbox.log.LogViewerAdapter;
import com.qinqi.debugtoolbox.manager.DbManager;

import java.util.List;

/**
 * Created by qinqi on 2016/12/8.
 */
public class QueryLogAsyncTask extends AsyncTask<Void, Void, List<DebugBoxLog>> {

    private LogViewerAdapter adapter;
    private ProgressDialog progressDialog;

    public QueryLogAsyncTask(LogViewerAdapter logViewerAdapter, ProgressDialog dialog) {
        super();
        this.adapter = logViewerAdapter;
        this.progressDialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected List<DebugBoxLog> doInBackground(Void... params) {
        List<DebugBoxLog> list = DbManager.getInstance().queryAllDebugBoxLog();
        return list;
    }

    @Override
    protected void onPostExecute(List<DebugBoxLog> debugBoxLogs) {
        adapter.setData(debugBoxLogs);
        adapter.notifyDataSetChanged();

        progressDialog.dismiss();
    }
}
