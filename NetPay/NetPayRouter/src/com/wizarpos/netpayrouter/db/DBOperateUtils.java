package com.wizarpos.netpayrouter.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wizarpos.netpay.util.MyLog;
import com.wizarpos.netpayrouter.bean.PayResultsBean;
import com.wizarpos.netpayrouter.bean.TerminalBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * @author xuchuanren
 * @date 2015年11月13日 上午10:46:53
 * @modify 修改人： 修改时间：
 */
public class DBOperateUtils {

	public static DBOperateUtils dbutil;
	public DBHelper dbHelper = null;
	private SQLiteDatabase db;

	private DBOperateUtils(Context context) {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	public static DBOperateUtils getInstance(Context context) {
		if (dbutil == null) {
			dbutil = new DBOperateUtils(context);
		}
		return dbutil;

	}

	/**
	 * 支付记录表
	 * 
	 * @param bean
	 * @return
	 */

	public int insertPayreulsts(PayResultsBean bean) {
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			// 实例化常量值
			ContentValues cValue = new ContentValues();
			// 添加sn
			cValue.put("uid", bean.getUid());
			cValue.put("sn", bean.getSn());
			// 添加终端名称
			cValue.put("terminal_name", bean.getTerminal_name());
			// 添加终端支付时间
			cValue.put("paytime", bean.getPaytime());
			// 添加流水号
			cValue.put("waterno", bean.getWaterno());
			// 添加支付金额
			cValue.put("paymoney", bean.getPaymoney());
			// 添加流水号
			cValue.put("paystatus", bean.getPaystatus());
			// 调用insert()方法插入数据
			db.insert(dbHelper.DB_TRANS_TABLE_NAME, null, cValue);
			db.close();
			return 1;
		} else {
			db.close();
			return 0;
		}
	}

	/**
	 * 通过支付时间和sn，更新支付状态
	 * 
	 * @param paytime
	 */
	public void updatePayStatus(PayResultsBean bean) {
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			ContentValues cValue = new ContentValues();
			cValue.put("waterno", bean.getWaterno());
			cValue.put("paytime", bean.getPaytime());
			cValue.put("paymoney", bean.getPaymoney());
			cValue.put("terminal_name", bean.getTerminal_name());
			cValue.put("sn", bean.getSn());
			cValue.put("paystatus", bean.getPaystatus());
			db.update(DBHelper.DB_TRANS_TABLE_NAME, cValue, " uid = ? ", new String[] { bean.getUid() });
			db.close();
		}
	}

	public long getPayResultCount(String startTime, String endTime, int paystatus, String sn, int pageNumber,
			int pageSize) {
		long count = 0;
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			String selection = null;
			String[] selectionArgs = null;

			List<String> selections = new ArrayList<String>();
			List<String> Args = new ArrayList<String>();
			if (!TextUtils.isEmpty(startTime)) {
				selections.add("paytime >= ? ");
				Args.add(startTime);
			}
			if (!TextUtils.isEmpty(endTime)) {
				selections.add("paytime <=? ");
				Args.add(endTime);
			}
			if (!TextUtils.isEmpty(sn)) {
				selections.add("sn =? ");
				Args.add(sn);
			}
			if (paystatus != 0) {
				selections.add("paystatus =? ");
				Args.add(paystatus + "");
			}
			String sql = "select count(*) from " + DBHelper.DB_TRANS_TABLE_NAME;
			if (selections.size() > 0) {
				selectionArgs = new String[selections.size()];
				selection = "";
				for (int i = 0; i < selections.size(); i++) {
					selection = selection + selections.get(i) + " and ";
					selectionArgs[i] = Args.get(i);
				}
				selection += " 1=1 ";
				sql += " where " + selection;
			}
			MyLog.d("sql=" + sql);
			MyLog.d("selectionArgs=" + Arrays.toString(selectionArgs));
			Cursor cursor = db.rawQuery(sql, selectionArgs);
			cursor.moveToFirst();
			count = cursor.getLong(0);
		}
		return count;
	}

	/**
	 * 支付记录查询
	 * 
	 * @param startTime
	 * @param endTime
	 * @param paystatus
	 * @param sn
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public ArrayList<PayResultsBean> getPayResultList(String startTime, String endTime, int paystatus, String sn,
			int pageNumber, int pageSize) {
		ArrayList<PayResultsBean> list = new ArrayList<PayResultsBean>();
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			String selection = null;
			String[] selectionArgs = null;

			List<String> selections = new ArrayList<String>();
			List<String> Args = new ArrayList<String>();
			if (!TextUtils.isEmpty(startTime)) {
				selections.add("paytime >= ? ");
				Args.add(startTime);
			}
			if (!TextUtils.isEmpty(endTime)) {
				selections.add("paytime <=? ");
				Args.add(endTime);
			}
			if (!TextUtils.isEmpty(sn)) {
				selections.add("sn =? ");
				Args.add(sn);
			}
			if (paystatus != 0) {
				selections.add("paystatus =? ");
				Args.add(paystatus + "");
			}

			if (selections.size() > 0) {
				selectionArgs = new String[selections.size()];
				selection = "";
				for (int i = 0; i < selections.size(); i++) {
					selection = selection + selections.get(i) + " and ";
					selectionArgs[i] = Args.get(i);
				}
				selection += " 1=1 ";
			}
			String orderBy = " paytime DESC";
			String limit = " " + (pageNumber - 1) * pageSize + " , " + pageSize;
			Cursor cursor = db.query(true, dbHelper.DB_TRANS_TABLE_NAME, null, selection, selectionArgs, null, null,
					orderBy, limit);
			if (cursor != null && cursor.getCount() > 0) {
				if (cursor.moveToFirst()) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						PayResultsBean bean = new PayResultsBean();
						bean.setUid(cursor.getString(cursor.getColumnIndex("uid")));
						bean.setPaymoney(cursor.getString(cursor.getColumnIndex("paymoney")));
						bean.setPaystatus(cursor.getInt(cursor.getColumnIndex("paystatus")));
						bean.setPaytime(cursor.getLong(cursor.getColumnIndex("paytime")));
						bean.setSn(cursor.getString(cursor.getColumnIndex("sn")));
						bean.setWaterno(cursor.getString(cursor.getColumnIndex("waterno")));
						bean.setTerminal_name(cursor.getString(cursor.getColumnIndex("terminal_name")));
						list.add(bean);
					}
				}
				cursor.close();
				db.close();
			}

		}

		return list;
	}

	/**
	 * 终端数据表 数据库添加操作
	 * 
	 * @param terminal
	 * @return 成功1 非成功-1,0表示已存在
	 */

	public int insertDB(TerminalBean terminal) {
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			Cursor cursor = db.query(dbHelper.DB_TABLE_TERMINAL_NAME, null, "sn =?",
					new String[] { terminal.getTerminal_sn() }, null, null, null);
			if (cursor == null || cursor.getCount() == 0) {
				// 实例化常量值
				ContentValues cValue = new ContentValues();
				// 添加sn
				cValue.put("sn", terminal.getTerminal_sn());
				// 添加ip地址
				cValue.put("ip", terminal.getTerminal_ip());
				// 添加终端名称
				cValue.put("terminal_name", terminal.getTerminal_name());
				// 添加终端创建时间
				cValue.put("createdate", terminal.getCreatedate());
				// 调用insert()方法插入数据
				db.insert(dbHelper.DB_TABLE_TERMINAL_NAME, null, cValue);
				cursor.close();
				db.close();
				return 1;
			} else {
				cursor.close();
				db.close();
				return 0;
			}

		}

		return -1;
	}

	public ArrayList<TerminalBean> getTerminalList() {
		ArrayList<TerminalBean> terminal = new ArrayList<TerminalBean>();
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			Cursor cursor = db.query(dbHelper.DB_TABLE_TERMINAL_NAME, null, null, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				if (cursor.moveToFirst()) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						TerminalBean bean = new TerminalBean();
						bean.setCreatedate(cursor.getString(cursor.getColumnIndex("createdate")));
						bean.setTerminal_ip(cursor.getString(cursor.getColumnIndex("ip")));
						bean.setTerminal_name(cursor.getString(cursor.getColumnIndex("terminal_name")));
						bean.setTerminal_sn(cursor.getString(cursor.getColumnIndex("sn")));
						bean.setTerminal_status("0");
						terminal.add(bean);
					}
				}

			}
			cursor.close();
			db.close();
		}

		return terminal;
	}

	/**
	 * 获取分页数
	 * 
	 * @param maxResult
	 * @return
	 */

	public Long getTotalTerminalsPage(int maxResult) {
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}

			Cursor cursor = db.rawQuery("select count(*)from " + dbHelper.DB_TABLE_TERMINAL_NAME, null);
			// 游标移到第一条记录准备获取数据
			if (cursor.moveToFirst()) {
				// 获取数据中的LONG类型数据
				Long count = cursor.getLong(0);
				return count%maxResult==0 ?  count/maxResult:(count/maxResult+1);

			}
		}

		return 0l;
	}

	/**
	 * 分页查询
	 * 
	 * @param pageNo
	 * @param maxResult
	 * @return
	 */

	public ArrayList<TerminalBean> getTerminalListByPage(int pageNo, int maxResult) {
		ArrayList<TerminalBean> terminal = new ArrayList<TerminalBean>();
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			String sql = "select * from " + dbHelper.DB_TABLE_TERMINAL_NAME + " order by createdate DESC limit ?,?";
			int firstResult = 0;
			if (pageNo != 1) {
				firstResult = (pageNo - 1) * maxResult;
			}
			Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(firstResult), String.valueOf(maxResult) });
			if (cursor != null && cursor.getCount() > 0) {
				if (cursor.moveToFirst()) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						TerminalBean bean = new TerminalBean();
						bean.setCreatedate(cursor.getString(cursor.getColumnIndex("createdate")));
						bean.setTerminal_ip(cursor.getString(cursor.getColumnIndex("ip")));
						bean.setTerminal_name(cursor.getString(cursor.getColumnIndex("terminal_name")));
						bean.setTerminal_sn(cursor.getString(cursor.getColumnIndex("sn")));
						bean.setTerminal_status("0");
						terminal.add(bean);
					}
				}

			}
			cursor.close();
			db.close();
		}

		return terminal;
	}

	/**
	 * 通过SN号删除该条记录
	 * 
	 * @param sn
	 */
	public int deleteTerminalBySN(String sn) {
		int a = -1;
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}

			a = db.delete(DBHelper.DB_TABLE_TERMINAL_NAME, "sn=?", new String[] { sn });

			db.close();
		}
		return a;
	}

	/**
	 * 通过SN号删除该条记录
	 * 
	 * @param sn
	 */
	public int updateTerminalBySN(String sn, TerminalBean terminal) {
		int a = -1;
		if (db != null) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}

			// a = db.delete(DBHelper.DB_TABLE_TERMINAL_NAME, "sn=?", new
			// String[] { sn
			// });
			ContentValues cValue = new ContentValues();
			// 添加sn
			// cValue.put("sn", terminal.getTerminal_sn());
			// 添加ip地址
			cValue.put("ip", terminal.getTerminal_ip());
			// 添加终端名称
			cValue.put("terminal_name", terminal.getTerminal_name());
			// 添加终端创建时间
			cValue.put("createdate", terminal.getCreatedate());
			a = db.update(DBHelper.DB_TABLE_TERMINAL_NAME, cValue, "sn=?", new String[] { sn });
			db.close();
		}
		return a;
	}

}
