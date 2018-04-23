package com.ljy.ljyutils.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ljy.util.LjyDbOpenHelper;
import com.ljy.util.LjyLogUtil;

/**
 * Created by LJY on 2018/4/23.
 */

public class BookProvider extends ContentProvider {

    public static final String AUTHORITY = "com.ljy.ljyutils.provider.Book.Provider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        uriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private Context mContext;
    private LjyDbOpenHelper mDbHelper;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        LjyLogUtil.i("BookProvider.onCreate(), currentThread: " + Thread.currentThread().getName());
        mContext = getContext();
        //初始化数据库,这里是为了方便演示,实际使用中不推荐在主线程中进行耗时的数据库操作
        initProviderData();
        return true;
    }


    private void initProviderData() {
        //创建数据库对象
        mDbHelper = new LjyDbOpenHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        //创建表
//        LjyDbOpenHelper.createTable(mDb,BOOK_TABLE_NAME,"name TEXT");
//        LjyDbOpenHelper.createTable(mDb,USER_TABLE_NAME,"name TEXT,age INT");
        mDbHelper.createTable(BOOK_TABLE_NAME, "name TEXT");
        mDbHelper.createTable(USER_TABLE_NAME, "name TEXT, age INT");
        //初始化数据
        mDbHelper.deleteFromTable(BOOK_TABLE_NAME);
        mDbHelper.deleteFromTable(USER_TABLE_NAME);
        mDbHelper.insertIntoTable(BOOK_TABLE_NAME, "1,'Android'");
        mDbHelper.insertIntoTable(BOOK_TABLE_NAME, "2,'iOS'");
        mDbHelper.insertIntoTable(BOOK_TABLE_NAME, "3,'Html 5'");
        mDbHelper.insertIntoTable(USER_TABLE_NAME, "1,'兰息',18");
        mDbHelper.insertIntoTable(USER_TABLE_NAME, "2,'梦泪',19");
        mDbHelper.insertIntoTable(USER_TABLE_NAME, "3,'杰斯',17");


    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        LjyLogUtil.i("BookProvider.query(), currentThread: " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table))
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        return mDb.query(table, projection, selection, selectionArgs,
                null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        LjyLogUtil.i("BookProvider.getType(), currentThread: " + Thread.currentThread().getName());
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        LjyLogUtil.i("BookProvider.insert(), currentThread: " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table))
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        mDb.insert(table,null,values);
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        LjyLogUtil.i("BookProvider.delete(), currentThread: " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table))
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        int count=mDb.delete(table,selection,selectionArgs);
        if (count>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        LjyLogUtil.i("BookProvider.update(), currentThread: " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table))
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        int row=mDb.update(table,values,selection,selectionArgs);
        if (row>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
