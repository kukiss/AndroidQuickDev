package weiy.app.basic.tools;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weiy.app.basic.dialogs.DatePickDialog;
import weiy.app.basic.dialogs.TimePickDialog;
import weiy.app.basic.myclass.Size;

public class WYCommon {

	public static void showToast(String msg, Context context) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(String msg, Context context, int duration) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setDuration(duration);
		toast.show();
	}

	public static boolean checkNetwork(Context context) {
		if (isNetworkAvailable(context)) {
			return true;
		} else {
			Toast.makeText(context, "网络连接失败, 请检查网络设置!", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm      = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo         network = cm.getActiveNetworkInfo();
		return network != null && network.isConnected();
	}

	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	public static String getDeviceId(Context context) {
		//TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return getMacAddress(context);
	}

	/** 获取mac地址 */
	public static String getMacAddress(Context context) {
		WifiManager wm  = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String      mac = wm.getConnectionInfo().getMacAddress();
		return mac;
	}

	public static String getMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest   digest   = null;
		FileInputStream in       = null;
		byte            buffer[] = new byte[1024];
		int             len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	public static PackageInfo getApkInfo(String apkFile, Context context) {
		PackageManager packageManager = context.getPackageManager();
		return packageManager.getPackageArchiveInfo(apkFile, PackageManager.GET_ACTIVITIES);
	}

	/** dip px互转 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/** 检查输入框是否为空 */
	public static boolean checkInputIsNull(EditText[] edits) {
		for (EditText et : edits) {
			if (et.getText().toString().equals("")) {
				return true;
			}
		}
		return false;
	}

	/** 注册点击事件 */
	public static void setClickListener(View[] views, View.OnClickListener listener) {
		for (View v : views) {
			v.setOnClickListener(listener);
		}
	}

	/**
	 * help of time
	 * 返回当前时间
	 */
	public static String getNowTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String getNowTime(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	public static String getNowDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static String getDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static String getTime(long mills, String format) {
		return new SimpleDateFormat(format).format(new Date(mills));
	}

	public static Date subDate(Date date, int day) {
		return new Date(date.getTime() - day * 24 * 60 * 60 * 1000);
	}

	public static Date addDate(Date date, int day) {
		return new Date(date.getTime() + day * 24 * 60 * 60 * 1000);
	}

	public static Date parseDate(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}


	/** 为ListView添加一个空数据时候的view */
	public static void setEmptyView(Context context, ListView lv, int viewId) {
		View v = LayoutInflater.from(context).inflate(viewId, null);
		v.setVisibility(View.GONE);
		((ViewGroup) lv.getParent()).addView(v);
		lv.setEmptyView(v);
	}

	/** 显示日期选择的dialog */
	public static void showDatePickDialog(FragmentManager manager, DatePickDialog.OnDatePickListener listener) {
		DatePickDialog dialog = new DatePickDialog();
		dialog.setOnDatePickListener(listener);
		WYFrag.showDialog(manager, dialog);
	}

	/** 显示时间选择的dialog */
	public static void showTimePickDialog(FragmentManager manager, TimePickDialog.OnTimePickListener listener) {
		TimePickDialog dialog = new TimePickDialog();
		dialog.setOnTimePickListener(listener);
		WYFrag.showDialog(manager, dialog);
	}

	/** 显示软键盘, view为焦点view */
	public static void showKeyBoard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/** 隐藏软键盘 */
	public static void hideKeyBoard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/** 获取屏幕分辨率 */
	public static int[] getScreenSize(Context context) {
		int[] size = new int[2];
		size[0] = context.getResources().getDisplayMetrics().widthPixels;
		size[1] = context.getResources().getDisplayMetrics().heightPixels;
		return size;
	}

	/** 检查意图是否有相应的app可以处理 */
	public static boolean hasAppHandle(Context context, Intent intent) {
		return context.getPackageManager().queryIntentActivities(intent, 0).size() > 0;
	}

	/** 跳转GPS设置界面 */
	public static void enableLocationSettings(Context context) {
		Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		context.startActivity(settingsIntent);
	}

	public static boolean isNum(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isFloat(String str) {
		try {
			Float.parseFloat(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** 获取mac地址 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo    info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/** 触发home键 */
	public static void pressHome(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(intent);
	}

	/** open system's camera */
	public static void openSystemCamera(Context context, File file) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		context.startActivity(intent);
	}

	/** get the version of this app */
	public static String getVersion(Context context) {
		String version = "";
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	public static void openUrl(Context context, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) context.startActivity(intent);
	}

	public static void toggleMobileData(Context context, boolean state) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			Method method = connectivityManager.getClass().getMethod("setMobileDataEnabled", boolean.class);
			method.invoke(connectivityManager, state);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** find resource id by name */
	public static int getResId(Context context, String name, String type) {
		try {
			return context.getResources().getIdentifier(name, type, context.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/** get refreshed location */
	public static Location getLastLocation(LocationManager manager) {
		Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null && Math.abs(location.getTime() - System.currentTimeMillis()) < 20000) {
			return location;
		} else {
			return null;
		}
	}

	private Size measureSize(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		int height = view.getMeasuredHeight();
		int width  = view.getMeasuredWidth();
		return new Size(width, height);
	}

}
