package com.yugy.v2ex.daily.dao.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.yugy.v2ex.daily.dao.DataProvider;
import com.yugy.v2ex.daily.dao.dbinfo.BaseNodesDBInfo;

/**
 * Created by yugy on 14-3-7.
 */
public abstract class BaseDataHelper {

    private Context mContext;

    public BaseDataHelper(Context context){
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    protected abstract Uri getContentUri();

    protected abstract String getTableName();

    public void notifyChange(){
        mContext.getContentResolver().notifyChange(getContentUri(), null);
    }

    protected final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        return mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    protected final Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder){
        return mContext.getContentResolver().query(getContentUri(), projection, selection, selectionArgs, sortOrder);
    }

    protected final Uri insert(ContentValues values){
        return mContext.getContentResolver().insert(getContentUri(), values);
    }

    protected final int bulkInsert(ContentValues[] values){
        return mContext.getContentResolver().bulkInsert(getContentUri(), values);
    }

    protected final int update(ContentValues values, String where, String[] whereArgs){
        return mContext.getContentResolver().update(getContentUri(), values, where, whereArgs);
    }

    protected final int delete(String where, String[] selectionArgs){
        return mContext.getContentResolver().delete(getContentUri(), where, selectionArgs);
    }

    public CursorLoader getCursorLoader(Context context){
        return getCursorLoader(context, null, null, null, null);
    }

    public CursorLoader getCursorLoader(Context context, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        return new CursorLoader(context, getContentUri(), projection, selection, selectionArgs, sortOrder);
    }

    public int deleteAll(){
        synchronized (DataProvider.obj){
            DataProvider.DBHelper dbHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            return db.delete(getTableName(), null, null);
        }
    }

    public CursorLoader getCursorLoader(){
        return new CursorLoader(getContext(), getContentUri(), null, null, null, BaseNodesDBInfo._ID);
    }

}
