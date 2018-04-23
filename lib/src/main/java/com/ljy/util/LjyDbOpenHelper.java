package com.ljy.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by LJY on 2018/4/23.
 */

public class LjyDbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ljy.db";

    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;


    public LjyDbOpenHelper(Context context) {
        this(context, DB_NAME);
    }

    public LjyDbOpenHelper(Context context, String dbName) {
        this(context, dbName, DB_VERSION);
    }

    public LjyDbOpenHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(String tableName) {
        createTable(tableName, null);
    }

    public void createTable(String tableName, String params) {
        createTable(db, tableName, params);
    }

    public void createTable(SQLiteDatabase db, String tableName, String params) {
        if (TextUtils.isEmpty(params)) {
            String sqlStr = "CREATE TABLE IF NOT EXISTS " + tableName +
                    "(_id INTEGER PRIMARY KEY)";
            db.execSQL(sqlStr);
        } else {
            String sqlStr = "CREATE TABLE IF NOT EXISTS " + tableName +
                    "(_id INTEGER PRIMARY KEY," + params + ")";
            db.execSQL(sqlStr);
        }
    }

    public void deleteFromTable(String tableName) {
        deleteFromTable(tableName, null);
    }

    public void deleteFromTable(String tableName, String params) {
        deleteFromTable(db, tableName, params);
    }

    public void deleteFromTable(SQLiteDatabase db, String tableName, String params) {
        if (TextUtils.isEmpty(params))
            db.execSQL("delete from " + tableName);
        else {
            db.execSQL("delete from " + tableName + " where " + params);
        }
    }

    public void insertIntoTable(String tableName,String data ){
        insertIntoTable(db,tableName,data);
    }
    public void insertIntoTable(SQLiteDatabase db,String tableName,String data ){
        db.execSQL("insert into "+tableName+" values("+data+");");
    }
}
