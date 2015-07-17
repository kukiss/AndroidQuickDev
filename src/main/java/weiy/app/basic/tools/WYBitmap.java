package weiy.app.basic.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class WYBitmap {

	/** 图片生成圆角. */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int   color   = 0xff424242;
		final Paint paint   = new Paint();
		final Rect  rect    = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF   = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/** 用指定的size(KB)压缩图片. */
	public static byte[] compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baOs = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baOs);
		int options = 90;
		while (baOs.toByteArray().length / 1024 > size && options > 0) {
			baOs.reset();
			image.compress(Bitmap.CompressFormat.JPEG, options, baOs);
			options -= 10;
		}
		return baOs.toByteArray();
	}

	/** 保存图片. */
	public static File savePhoto(Bitmap bmp, File file) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width  = bgimage.getWidth();
		float height = bgimage.getHeight();

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth  = ((float) (width > height ? newWidth : newHeight)) / width;
		float scaleHeight = ((float) (width > height ? newHeight : newWidth)) / height;
		float scale       = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
		// 缩放图片动作
		matrix.postScale(scale, scale);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}


	/** 设置bitmap大小. */
	public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if (w > h) {
			int t = w;
			w = h;
			h = t;
		}
		// 计算缩放比例
		float scaleWidth  = ((float) width) / w;
		float scaleHeight = ((float) height) / h;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	/** 从Resources以指定大小载入图片. */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/** 计算缩放比例. */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height       = options.outHeight;
		final int width        = options.outWidth;
		int       inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/** 从文件以指定大小载入图片. */
	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap decodeSampledBitmapFromUrl(URL url, int reqWidth, int reqHeight) throws Exception {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(url.openStream(), null, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(url.openStream(), null, options);
	}
}
