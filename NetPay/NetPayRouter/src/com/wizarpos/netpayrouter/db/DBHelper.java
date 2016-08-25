package com.wizarpos.netpayrouter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author xuchuanren
 * @date 2015年11月13日 上午10:29:49
 * @modify 修改人： 修改时间：
 */
public class DBHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "terminal.db";
	public static String DB_TABLE_TERMINAL_NAME = "terminal";
	public static String DB_TRANS_TABLE_NAME = "trans_result";
	private final static int version = 5;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, version);

	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建表terminal SQL语句
		String stu_table = "create table "
				+ DB_TABLE_TERMINAL_NAME
				+ "( uid text primary key ,terminal_name text,sn text, ip text,createdate text)";
		// 执行SQL语句
		db.execSQL(stu_table);
		
		// 创建表支付记录 SQL语句
		String trans_table = "create table "
				+ DB_TRANS_TABLE_NAME
				+ "( uid text primary key ,waterno text,paytime INTEGER, paymoney text,terminal_name text,sn text, paystatus INT)";
		// 执行SQL语句
		db.execSQL(trans_table);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("drop table if exists "+DB_TABLE_TERMINAL_NAME+";");
		db.execSQL("drop table if exists "+DB_TRANS_TABLE_NAME+";");
		onCreate(db);
	}

}
