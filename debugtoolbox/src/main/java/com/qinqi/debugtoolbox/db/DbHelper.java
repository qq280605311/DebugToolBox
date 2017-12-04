package com.qinqi.debugtoolbox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建类
 * Created by qinqi on 2016/11/16.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "debugtoolbox_versionName.version_code.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String net_traffics =
                "CREATE TABLE IF NOT EXISTS " + Tables.NetTrafficsTable.TABLE_NAME + " (" +
                        Tables.NetTrafficsTable.Field.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Tables.NetTrafficsTable.Field.TIME + " DATETIME," +
                        Tables.NetTrafficsTable.Field.URL + " TEXT," +
                        Tables.NetTrafficsTable.Field.ELAPSED_TIME + " INTEGER," +
                        Tables.NetTrafficsTable.Field.METHOD + " TEXT," +
                        Tables.NetTrafficsTable.Field.REQUEST_HEADER + " TEXT," +
                        Tables.NetTrafficsTable.Field.REQUEST_BODY + " TEXT," +
                        Tables.NetTrafficsTable.Field.REQUEST_MEDIA_TYPE + " TEXT," +
                        Tables.NetTrafficsTable.Field.CODE + " TEXT," +
                        Tables.NetTrafficsTable.Field.MESSAGE + " TEXT," +
                        Tables.NetTrafficsTable.Field.PROTOCOL + " TEXT," +
                        Tables.NetTrafficsTable.Field.RESPONSE_HEADER + " TEXT," +
                        Tables.NetTrafficsTable.Field.RESPONSE_BODY + " TEXT," +
                        Tables.NetTrafficsTable.Field.RESPONSE_MEDIA_TYPE + " TEXT," +
                        Tables.NetTrafficsTable.Field.RESPONSE_CONTENT_LENGTH + " INTEGER " +
                        ")";

        String log = "CREATE TABLE IF NOT EXISTS " + Tables.DebugBoxLogTable.TABLE_NAME + " (" +
                Tables.DebugBoxLogTable.Field.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Tables.DebugBoxLogTable.Field.PID + " INTEGER," +
                Tables.DebugBoxLogTable.Field.TIME + " DATETIME," +
                Tables.DebugBoxLogTable.Field.TAG + " TEXT," +
                Tables.DebugBoxLogTable.Field.MESSAGE + " TEXT," +
                Tables.DebugBoxLogTable.Field.STATE + " INTEGER" +
                ")";

        database.execSQL(net_traffics);
        database.execSQL(log);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
