package weiy.app.basic.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
public class CrashHandler implements UncaughtExceptionHandler {

	// CrashHandler实例
	private static CrashHandler             instance;
	// 系统默认的UncaughtException处理类
	private        UncaughtExceptionHandler mDefaultHandler;
	// 程序的Context对象
	private        Context                  mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

	/** 保证只有一个CrashHandler实例. */
	private CrashHandler() {

	}

	public static CrashHandler getInstance() {

		if (instance == null) instance = new CrashHandler();
		return instance;
	}

	public void init(Context context) {

		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/** 当UncaughtException发生时会转入该函数来处理. */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息发送错误报告等操作均在此完成.
	 *
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {

		if (ex == null) {
			return false;
		}
		// 收集设备参数信息
		collectDeviceInfo(mContext);

		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {

				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		// 保存日志文件
		saveCatchInfo2File(ex);
		uploadError(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 */
	public void collectDeviceInfo(Context ctx) {

		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 保存错误信息到文件中. */
	private String saveCatchInfo2File(Throwable ex) {

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key).append("=").append(value).append("\n");
		}

		Writer      writer      = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);

		// show error log in terminal
		Log.e("mylog", sb.toString());

		//upload to server
		new Thread() {
			@Override public void run() {

			}
		}.start();

		/* save to file
		try {
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory() + "/crash/" + getApplicationName(mContext) + "/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				// 发送给开发人员
				// sendCrashLog2PM(getApplicationName(mContext), fileName, path + fileName);
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		return null;
	}

	private void uploadError(Throwable ex) {

	}

	public String getApplicationName(Context c) {

		PackageManager  packageManager = null;
		ApplicationInfo applicationInfo;
		try {
			packageManager = c.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(c.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			applicationInfo = null;
		}
		return (String) packageManager.getApplicationLabel(applicationInfo);
	}

	/**
	 * 将捕获的导致崩溃的错误信息发送给开发人员
	 * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
	 */
	private void sendCrashLog2PM(String appName, String fileName, String dir) {

		if (!new File(dir).exists()) {
			Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
			return;
		}
		FileInputStream fis    = null;
		BufferedReader  reader = null;
		String          s;
		try {
			fis = new FileInputStream(dir);
			reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			while (true) {
				s = reader.readLine();
				if (s == null) break;
				// 由于目前尚未确定以何种方式发送，所以先打出log日志。
				Log.e("mylog", s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // 关闭流
			try {
				assert reader != null;
				reader.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
