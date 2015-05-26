package weiy.app.basic.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WYFile {

	/** 以完整的文件名打开文件, 不存就新建文件. */
	public static File openFile(String filePath) {

		File file = new File(filePath);
		File path = file.getParentFile();
		if (path == null) {
			return null;
		}
		if (!path.exists()) {
			path.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("===>>" + e.getMessage());
				return null;
			}
		}
		return file;
	}

	/** 以文件所在的路劲和文件名打开文件, 不存在就新建文件. */
	public static File openFile(String path, String fileName) {

		File file = new File(path, fileName);
		return openFile(file.getAbsolutePath());
	}

	/** 以文件所在的路劲和文件名打开文件, 不存在就新建文件. */
	public static File openFile(File path, String fileName) {

		File file = new File(path.getAbsolutePath(), fileName);
		return openFile(file.getAbsolutePath());
	}

	/** 保存图片. */
	public static File savePhoto(Bitmap bmp, String path) {

		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(openFile(path));
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (stream != null) stream.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return new File(path);
	}

	/** 从assets文件夹打开文件. */
	public static InputStream openFromAssets(String file, Context context) {

		try {
			return context.getAssets().open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 检查SD卡是否可写. */
	public static boolean isExternalStorageWritable() {

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public static final boolean checkExternalStorage(Context context) {

		if (isExternalStorageWritable()) {
			return true;
		} else {
			Toast.makeText(context, "SD卡不可用!", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/** 检查SD卡是否只读. */
	public static boolean isExternalStorageReadable() {

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

}
