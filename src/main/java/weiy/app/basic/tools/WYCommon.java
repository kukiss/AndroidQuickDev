package weiy.app.basic.tools;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weiy.app.basic.dialogs.DatePickDialog;
import weiy.app.basic.dialogs.TimePickDialog;

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
			Toast.makeText(context, "网络无法连接, 请检查网络设置.", Toast.LENGTH_SHORT).show();
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

	public static Map<String, Object> getApkInfo(String apkFile, Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo    packageInfo    = packageManager.getPackageArchiveInfo(apkFile, PackageManager.GET_ACTIVITIES);
		if (packageInfo != null) {
			HashMap<String, Object> map = new HashMap<String, Object>(4);
			map.put("name", packageInfo.packageName);
			map.put("uid", packageInfo.sharedUserId);
			map.put("vname", packageInfo.versionName);
			map.put("vcode", packageInfo.versionCode);
			return map;
		}
		return null;
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

	/** 返回当前时间 */
	public static String getNowTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String getNowTime(String format) {
		return new SimpleDateFormat(format).format(new Date());
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
}
