package com.wizarpos.netpayrouter.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Tools {

	/** 播放多媒体文件 */
	public static void play(Context ctx, String mediaFile) {
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(mediaFile);
			player.prepare();
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			player.release();
		}
	}

	/** 页面跳转 */
	public static void to(Activity fromActivity,
			Class<? extends Context> toContext) {
		Intent intent = new Intent();
		intent.setClass(fromActivity, toContext);
		fromActivity.startActivity(intent);
	}

	/** 页面跳转 */
	public static void toAndFinish(Activity fromActivity,
			Class<? extends Context> toContext) {
		to(fromActivity, toContext);
		fromActivity.finish();
	}

	public static final String INT_PARAM_KEY = "__int_param_key";

	public static void toWithParam(Activity fromActivity,
			Class<? extends Context> toContext, int param) {
		Intent intent = new Intent();
		intent.putExtra(INT_PARAM_KEY, param);
		intent.setClass(fromActivity, toContext);
		fromActivity.startActivity(intent);
	}

	public static void to(Activity fromActivity,
			Class<? extends Context> toContext, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(fromActivity, toContext);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	public static void toAndFinish(Activity fromActivity,
			Class<? extends Context> toContext, int requestCode) {
		to(fromActivity, toContext, requestCode);
		fromActivity.finish();
	}

	public static void toWithParam(Activity fromActivity,
			Class<? extends Context> toContext, int requestCode, int param) {
		Intent intent = new Intent();
		intent.putExtra(INT_PARAM_KEY, param);
		intent.setClass(fromActivity, toContext);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/** 左补字符串 */
	public static String leftPad(String src, String pad, int pos) {
		int length = src.length();
		if (length >= pos) {
			return src;
		}
		do {
			src = pad + src;
		} while (src.length() < pos);
		return src;
	}

	/** 从左边删除指定字符 */
	public static String leftRemove(String src, String s) {
		while (src.startsWith(s)) {
			src = src.substring(s.length());
		}
		return src;
	}

	/** 从左边插入 */
	public static String insert(String src, String s, int index) {
		if (src.length() < index) {
			return src;
		}
		return src.substring(0, index) + s + src.substring(index);
	}

	/** 从右边指定位置插入 */
	public static String insertFromRight(String src, String s, int index) {
		if (src.length() < index) {
			return src;
		}
		return src.substring(0, src.length() - index) + s
				+ src.substring(src.length() - index);
	}

	/** 替换指定位置的字符 */
	public static String replace(String src, char c, int startIndex, int length) {
		if (src == null) {
			return null;
		}
		if (src.length() < startIndex + length) {
			return src;
		}
		if (length < 0) {
			throw new IllegalArgumentException("length should be >= 0");
		}
		char[] cs = new char[length];
		Arrays.fill(cs, c);
		return src.substring(0, startIndex) + String.valueOf(cs)
				+ src.substring(startIndex + length);
	}

	public static String reverse(String s) {
		return new StringBuilder(s).reverse().toString();
	}

	/**
	 * 格式化人民币。元 格式化为 保留2位小数
	 * 
	 * @param i
	 * @return
	 */
	public static String formatYuan(long i) {
		DecimalFormat format = new DecimalFormat("0.00");
		return format.format(i);
	}

	/**
	 * 格式化人民币。分 格式化为 元
	 * 
	 * @param i
	 *            分
	 * @return 精确到分的人民币表示
	 */
	public static String formatFen(long i) {
		BigDecimal money = new BigDecimal(i);
		return money.divide(new BigDecimal(100), 2,
				BigDecimal.ROUND_UNNECESSARY).toString();
	}

	/**
	 * 格式换人民币。元 --> 分
	 * 
	 * @param s
	 *            元
	 * @return 分
	 */
	public static int toIntMoney(String s) {
		BigDecimal money = new BigDecimal(s);
		return money.multiply(new BigDecimal(100)).intValue();
	}

	/**
	 * 人民币求余额
	 * 
	 * @param num1
	 *            减数 元
	 * @param num2
	 *            被减数 分
	 * @return 元
	 */
	public static BigDecimal substract(String num1, int num2) {
		return new BigDecimal(num1).subtract(new BigDecimal(num2)
				.divide(new BigDecimal(100)));
	}

	/**
	 * 人民币求和
	 * 
	 * @param num1
	 *            加数 元
	 * @param num2
	 *            加数 分
	 * @return 元
	 */
	public static BigDecimal add(String num1, int num2) {
		return new BigDecimal(num1).add(new BigDecimal(num2)
				.divide(new BigDecimal(100)));
	}

	/**
	 * 人民币比较
	 * 
	 * @param num1
	 *            元
	 * @param num2
	 *            分
	 * @return 元 > 分 ? (> 0) : (<= 0)
	 */
	public static int compare(String num1, int num2) {
		return new BigDecimal(num1).multiply(new BigDecimal(100)).compareTo(
				new BigDecimal(num2));
	}

	private static final String SERIAL_PARAM_KEY = "__serial_param_key";

	/** ip是否规范有效 */
	public static boolean validIpv4(String ip) {
		String regexp = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	/** 端口号是否规范有效 */
	public static boolean validPort(int port) {
		return (port > 1 && port < 65535);
	}

	/**
	 * 从Map中取值
	 * 
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValue(Map<String, Object> map, String key,
			Object defaultValue) {
		Object obj = map.get(key);
		if (obj == null) {
			if (defaultValue == null) {
				return null;
			}
			return (T) defaultValue;
		}
		return (T) obj;
	}

	public static String getValue(boolean v, String trueValue, String falseValue) {
		return v ? trueValue : falseValue;
	}

	public static String byte2String(byte[] bs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bs.length; i++) {
			sb.append(String.format("%02X", bs[i]));
		}
		return sb.toString();
	}

	// public static Bitmap genQRCodeTow(String content) {
	// // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
	// try {
	// BitMatrix matrix = new MultiFormatWriter().encode(content,
	// BarcodeFormat.QR_CODE, 280, 280);
	// int width = matrix.getWidth();
	// int height = matrix.getHeight();
	// // 二维矩阵转为一维像素数组,也就是一直横着排了，注意添加白色背景，否则打印会是黑色。
	// int[] pixels = new int[width * height];
	// for (int y = 0; y < height; y++) {
	// for (int x = 0; x < width; x++) {
	// if (matrix.get(x, y)) {
	// pixels[y * width + x] = 0xFF000000;
	// } else {// 二维码背景白色
	// pixels[y * width + x] = 0xFFFFFFFF;
	// }
	// }
	// }
	// Bitmap bitmap = Bitmap.createBitmap(width, height,
	// Bitmap.Config.ARGB_8888);
	// // 通过像素数组生成bitmap,具体参考api
	// bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	//
	// return bitmap;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	// public static Bitmap genQRCode(String content) {
	// // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
	// try {
	// BitMatrix matrix = new MultiFormatWriter().encode(content,
	// BarcodeFormat.QR_CODE, 200, 200);
	// int width = matrix.getWidth();
	// int height = matrix.getHeight();
	// // 二维矩阵转为一维像素数组,也就是一直横着排了，注意添加白色背景，否则打印会是黑色。
	// int[] pixels = new int[width * height];
	// for (int y = 0; y < height; y++) {
	// for (int x = 0; x < width; x++) {
	// if (matrix.get(x, y)) {
	// pixels[y * width + x] = 0xFF000000;
	// } else {// 二维码背景白色
	// pixels[y * width + x] = 0xFFFFFFFF;
	// }
	// }
	// }
	// Bitmap bitmap = Bitmap.createBitmap(width, height,
	// Bitmap.Config.ARGB_8888);
	// // 通过像素数组生成bitmap,具体参考api
	// bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	//
	// return bitmap;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	/**
	 * resize bitmap size
	 * 
	 * @param b
	 *            orignal bitmap
	 * @param width
	 *            new width
	 * @param height
	 *            new height
	 * @return new Bitmap
	 */
	public static Bitmap resizeBitmap(Bitmap b, int width, int height) {
		return Bitmap.createScaledBitmap(b, width, height, false);
	}

	public static void writePng(Bitmap b, File destFile) {
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(destFile);
			b.compress(Bitmap.CompressFormat.PNG, 100, fout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fout != null) {
					fout.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public static Bitmap readPng(String path) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			BitmapFactory.decodeFile(path, options);
			WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
					BitmapFactory.decodeFile(path, options));
			return Bitmap.createBitmap(weak.get());
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * create http url, which start with http://
	 * 
	 * @param host
	 *            host address
	 * @param port
	 *            default 80
	 * @param suffixUrl
	 *            resource relative address
	 * @return
	 */
	public static String getHttpUrl(String host, String port, String suffixUrl) {
		if (host == null) {
			throw new IllegalArgumentException("host can not be null");
		}
		String url = "http://" + host;
		if (port != null && !"".equals(port.trim())
				&& !"80".equals(port.trim())) {
			url += ":" + port;
		}
		url += suffixUrl;
		return url;
	}

	/** 输入金额 */
	public static String inputMoney(String stext, String appendValue) {
		String text = stext;
		text = text.replace(".", "");
		text += appendValue;
		text = Tools.leftRemove(text, "0");
		text = Tools.leftPad(text, "0", 3);
		text = Tools.insertFromRight(text, ".", 2);
		if (text.length() > 10) {
			return stext;
		}
		return text;
	}

	/** 删除金额 */
	public static String deleteMoney(String stext) {
		String text = stext;
		text = text.replace(".", "");
		text = text.substring(0, text.length() - 1);
		text = Tools.leftRemove(text, "0");
		text = Tools.leftPad(text, "0", 3);
		text = Tools.insertFromRight(text, ".", 2);
		return text;
	}

	/** 输入数字 */
	public static String inputNumber(String stext, String appendValue) {
		String text = stext;
		text += appendValue;
		return text;
	}

	/** 删除数字 */
	public static String deleteNumber(String stext) {
		String text = stext;
		if (text == null || "".equals(text)) {
			return stext;
		}
		text = text.substring(0, text.length() - 1);
		return text;
	}

	public static String formatCardNo(String cardNo) {
		char[] cs = cardNo.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cs.length; i++) {
			if (i % 4 == 0 && i != 0) {
				sb.append(" ");
			}
			sb.append(cs[i]);
		}
		return sb.toString();
	}

	/**
	 * 计算总页数
	 * 
	 * @param count
	 *            总记录数
	 * @param perNo
	 *            每页记录数
	 * @return 总页数，最少 1 页
	 */
	public static long getTotalPage(long count, int perNo) {
		if (count == 0) {
			return 1;
		}
		if (count % perNo == 0) {
			return count / perNo;
		}
		return (count / perNo) + 1;
	}

	/**
	 * 计算分页记录开始索引位置
	 * 
	 * @param pageNo
	 *            页码
	 * @param perNo
	 *            每页记录数
	 * @return 开始索引。从 0 开始
	 */
	public static long getPageStartIndex(long pageNo, int perNo) {
		return (pageNo - 1) * perNo;
	}

	/**
	 * 计算分页记录结束索引位置
	 * 
	 * @param pageNo
	 *            页码
	 * @param perNo
	 *            每页记录数
	 * @param count
	 *            总记录数
	 * @return 结束索引位置。
	 */
	public static long getPageEndIndex(long pageNo, int perNo, long count) {
		return count - getPageStartIndex(pageNo, perNo);
	}

	public static String md5(String s) {
		try {
			byte[] bs = s.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] d = md.digest(bs);
			return byte2String(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		try {
			if (!file.exists()) {
				return;
			}
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] fs = file.listFiles();
				for (File f : fs) {
					deleteFile(f);
				}
			}
			file.delete();
		} catch (Exception e) {
		}
	}

	/**
	 * url 编码
	 * 
	 * @param s
	 */
	public static String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * url 解码
	 * 
	 * @param s
	 */
	public static String urlDecode(String s) {
		try {
			return URLDecoder.decode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static Map<String, String> httpParamToMap(String httpParam) {
		Map<String, String> m = new TreeMap<String, String>();
		if (httpParam == null) {
			return m;
		}
		String[] ss = httpParam.split("&");
		for (String s : ss) {
			String[] kv = s.split("=");
			if (kv == null || "".equals(kv) || kv.length == 0) {
				continue;
			} else if (kv.length == 1) {
				m.put(kv[0], null);
			} else {
				m.put(kv[0], kv[1]);
			}
		}
		return m;
	}

	public static String mapToHttpParam(Map<String, Object> m,
			boolean ignoreNullOrEmpty) {
		if (m == null || m.size() < 1) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Object> entry : m.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (ignoreNullOrEmpty == true
					&& (value == null || ""
							.equals(String.valueOf(value).trim()))) {
				continue;
			}
			sb.append(key).append("=").append(String.valueOf(value))
					.append("&");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String mapToHttpParam(Map<String, Object> m) {
		return mapToHttpParam(m, false);
	}

	/**
	 * 服务是否启动。现在用法有待验证
	 * 
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean hasService(Context context, String className) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (serviceList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;
			Log.d("SERVICE", serviceName.getClassName());
			if (serviceName.getClassName().equals(className)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 * @param editText
	 */
	public static void hideSoftInput(final Context context,
			final EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(),
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 强制显示软键盘
	 * 
	 * @param context
	 * @param editText
	 */
	public static void showSoftInput(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(),
				InputMethodManager.SHOW_FORCED);
	}

	public static LayoutInflater getInflater(Context context) {
		return (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public static String dealGridPicUrl(String url, int weight, int height) {
		if (url == null) {
			return "";
		}
		String dex = "";
		if (url.contains("jpg")) {
			dex = ".jpg";
		} else if (url.contains("jpeg")) {
			dex = ".jpeg";
		} else if (url.contains("webp")) {
			dex = ".webp";
		} else if (url.contains("png")) {
			dex = ".png";
		} else if (url.contains("bmp")) {
			dex = ".bmp";
		} else {
			return "";
		}
		return url + "@" + weight + "w_" + height + "h_90Q" + dex;
	}

	public static String deleteMidTranLog(String tranLog, String mid) {
		if (tranLog == null) {
			return "";
		}
		String[] strarray2 = tranLog.split(mid);
		String printTranLog = "";
		for (int i = 0; i < strarray2.length; i++) {
			printTranLog += strarray2[i];
		}
		return printTranLog;
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}

	public static boolean isRightMoney(String money) {
		String reg = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(money);
		if (!matcher.find()) {
			return false;
		}
		return true;
	}
}