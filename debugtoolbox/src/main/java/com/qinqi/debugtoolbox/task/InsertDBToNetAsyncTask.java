package com.qinqi.debugtoolbox.task;

import android.os.AsyncTask;

import com.qinqi.debugtoolbox.manager.DbManager;
import com.qinqi.debugtoolbox.okhttp.NetTraffics;

/**
 * Created by qinqi on 2016/12/8.
 */
public class InsertDBToNetAsyncTask extends AsyncTask<NetTraffics, Void, Void> {
    @Override
    protected Void doInBackground(NetTraffics... params) {
        DbManager.getInstance().insertNetTraffics(params[0]);
        return null;
    }
}
