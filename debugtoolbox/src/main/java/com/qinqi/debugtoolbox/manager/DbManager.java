package com.qinqi.debugtoolbox.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qinqi.debugtoolbox.db.DbHelper;
import com.qinqi.debugtoolbox.db.Tables;
import com.qinqi.debugtoolbox.log.DebugBoxLog;
import com.qinqi.debugtoolbox.okhttp.NetTraffics;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 * Created by qinqi on 2016/11/16.
 */
public class DbManager {
    private static DbManager mDbManager;
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private DbManager() {
    }

    public static DbManager getInstance() {
        if (mDbManager == null) {
            mDbManager = new DbManager();
        }
        return mDbManager;
    }

    public void init(Context context) {
        String databaseName = "debugtoolbox_" + DebugToolBoxUtils.getVersionCodeAndName(context) + ".db";
        mDbHelper = new DbHelper(context, databaseName);
        mDb = mDbHelper.getWritableDatabase();
    }

    /**
     * NetTraffics 转 ContentValues
     *
     * @param traffics 网络对象
     * @return ContentValues
     */
    private ContentValues getNetTrafficsContentValues(NetTraffics traffics) {
        ContentValues values = new ContentValues();
        values.put(Tables.NetTrafficsTable.Field.TIME, traffics.getTime());
        values.put(Tables.NetTrafficsTable.Field.ELAPSED_TIME, traffics.getElapsedTime());
        values.put(Tables.NetTrafficsTable.Field.URL, traffics.getUrl());
        values.put(Tables.NetTrafficsTable.Field.METHOD, traffics.getMethod());
        values.put(Tables.NetTrafficsTable.Field.REQUEST_HEADER, traffics.getRequestHeader());
        values.put(Tables.NetTrafficsTable.Field.REQUEST_BODY, traffics.getRequestBody());
        values.put(Tables.NetTrafficsTable.Field.REQUEST_MEDIA_TYPE, traffics.getRequestMediaType());
        values.put(Tables.NetTrafficsTable.Field.CODE, traffics.getCode());
        values.put(Tables.NetTrafficsTable.Field.RESPONSE_HEADER, traffics.getResponseHeader());
        values.put(Tables.NetTrafficsTable.Field.RESPONSE_BODY, traffics.getResponseBody());
        values.put(Tables.NetTrafficsTable.Field.RESPONSE_MEDIA_TYPE, traffics.getResponseMediaType());
        values.put(Tables.NetTrafficsTable.Field.RESPONSE_CONTENT_LENGTH, traffics.getResponseContentLength());
        values.put(Tables.NetTrafficsTable.Field.MESSAGE, traffics.getMessage());
        values.put(Tables.NetTrafficsTable.Field.PROTOCOL, traffics.getProtocol());
        return values;
    }

    /**
     * Cursor 转 NetTraffics
     *
     * @param cursor
     * @return
     */
    private NetTraffics getNetTrafficsByCursor(Cursor cursor) {
        NetTraffics traffics = new NetTraffics();
        traffics.setId(cursor.getInt(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.ID)));
        traffics.setTime(cursor.getLong(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.TIME)));
        traffics.setElapsedTime(cursor.getInt(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.ELAPSED_TIME)));
        traffics.setUrl(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.URL)));
        traffics.setMethod(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.METHOD)));
        traffics.setRequestHeader(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.REQUEST_HEADER)));
        traffics.setRequestBody(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.REQUEST_BODY)));
        traffics.setRequestMediaType(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.REQUEST_MEDIA_TYPE)));
        traffics.setCode(cursor.getInt(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.CODE)));
        traffics.setResponseHeader(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.RESPONSE_HEADER)));
        traffics.setResponseBody(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.RESPONSE_BODY)));
        traffics.setResponseMediaType(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.RESPONSE_MEDIA_TYPE)));
        traffics.setResponseContentLength(cursor.getLong(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.RESPONSE_CONTENT_LENGTH)));
        traffics.setMessage(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.MESSAGE)));
        traffics.setProtocol(cursor.getString(cursor.getColumnIndex(Tables.NetTrafficsTable.Field.PROTOCOL)));

        return traffics;
    }

    private ContentValues getDebugBoxLogContentValues(DebugBoxLog log) {
        ContentValues values = new ContentValues();
        values.put(Tables.DebugBoxLogTable.Field.PID, log.getPid());
        values.put(Tables.DebugBoxLogTable.Field.TIME, log.getTime());
        values.put(Tables.DebugBoxLogTable.Field.TAG, log.getTag());
        values.put(Tables.DebugBoxLogTable.Field.MESSAGE, log.getMessage());
        values.put(Tables.DebugBoxLogTable.Field.STATE, log.getState());
        return values;
    }

    /**
     * 插入 NetTraffics
     *
     * @param traffics 网络请求监听对象
     */
    public void insertNetTraffics(NetTraffics traffics) {
        ContentValues values = getNetTrafficsContentValues(traffics);
        int id = (int) mDb.insert(Tables.NetTrafficsTable.TABLE_NAME, null, values);
    }

    /**
     * 查询所有的 NetTraffics
     *
     * @return list
     */
    public List<NetTraffics> queryAllNetTraffics() {
        List<NetTraffics> list = new ArrayList<>();

        String sql = "select * from " + Tables.NetTrafficsTable.TABLE_NAME + " order by time desc";
        Cursor cursor = mDb.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            NetTraffics traffics = getNetTrafficsByCursor(cursor);
            list.add(traffics);
        }
        cursor.close();
        return list;
    }

    /**
     * 根据 ID 查询 NetTraffics
     *
     * @param id 主键ID
     * @return NetTraffics
     */
    public NetTraffics queryNetTrafficsById(int id) {
        String sql = "select * from " + Tables.NetTrafficsTable.TABLE_NAME + " where _id = " + id;
        Cursor cursor = mDb.rawQuery(sql, null);
        NetTraffics traffics = new NetTraffics();
        while (cursor.moveToNext()) {
            traffics = getNetTrafficsByCursor(cursor);
        }
        cursor.close();
        return traffics;
    }

    /**
     * 根据时间查询 NetTraffics
     *
     * @param time 当前时间
     * @return NetTraffics
     */
    public List<NetTraffics> queryNetTrafficsListByTime(long time) {
        long startDate = 1;
        long endDate = 2;

        List<NetTraffics> list = new ArrayList<>();
        String sqlStr = "select * from " + Tables.NetTrafficsTable.TABLE_NAME +
                " where " + Tables.NetTrafficsTable.Field.TIME + " >= " + startDate +
                " and date <= " + endDate + " order by date asc";
        Cursor cursor = mDb.rawQuery(sqlStr, null);
        while (cursor.moveToNext()) {
            NetTraffics traffics = getNetTrafficsByCursor(cursor);
            list.add(traffics);
        }
        cursor.close();
        return list;
    }

    /**
     * 插入 DebugBoxLog
     *
     * @param log 日志
     */
    public void insertDebugBoxLog(DebugBoxLog log) {
        ContentValues values = getDebugBoxLogContentValues(log);
        mDb.insert(Tables.DebugBoxLogTable.TABLE_NAME, null, values);
    }

    /**
     * 查询所有 DebugBoxLog
     *
     * @return
     */
    public List<DebugBoxLog> queryAllDebugBoxLog() {
        List<DebugBoxLog> list = new ArrayList<>();
        String sql = "select * from " + Tables.DebugBoxLogTable.TABLE_NAME + " order by time desc";
        Cursor cursor = mDb.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            DebugBoxLog log = new DebugBoxLog();
            log.setPid(cursor.getInt(cursor.getColumnIndex(Tables.DebugBoxLogTable.Field.PID)));
            log.setTime(cursor.getLong(cursor.getColumnIndex(Tables.DebugBoxLogTable.Field.TIME)));
            log.setTag(cursor.getString(cursor.getColumnIndex(Tables.DebugBoxLogTable.Field.TAG)));
            log.setMessage(cursor.getString(cursor.getColumnIndex(Tables.DebugBoxLogTable.Field.MESSAGE)));
            log.setState(cursor.getInt(cursor.getColumnIndex(Tables.DebugBoxLogTable.Field.STATE)));

            list.add(log);
        }
        cursor.close();
        return list;
    }

    public void clearNetTrafficsDb() {
        mDb.delete(Tables.NetTrafficsTable.TABLE_NAME, null, null);
    }

    public void clearDebugBoxLog() {
        mDb.delete(Tables.DebugBoxLogTable.TABLE_NAME, null, null);
    }
}
