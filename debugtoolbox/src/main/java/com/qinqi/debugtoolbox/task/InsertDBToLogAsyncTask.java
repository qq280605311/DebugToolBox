package com.qinqi.debugtoolbox.task;

import android.os.AsyncTask;

import com.qinqi.debugtoolbox.log.DebugBoxLog;
import com.qinqi.debugtoolbox.manager.DbManager;


/**
 * Created by qinqi on 2016/12/8.
 */

public class InsertDBToLogAsyncTask extends AsyncTask<DebugBoxLog, Void, Void> {
    @Override
    protected Void doInBackground(DebugBoxLog... params) {
        DbManager.getInstance().insertDebugBoxLog(params[0]);
        return null;
    }
}
