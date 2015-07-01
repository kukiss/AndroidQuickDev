package weiy.app.basic.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.net.URL;
import java.util.HashMap;

public class WYImageCache {

	private String                   mCacheDir;
	private LruCache<String, Bitmap> mCache;
	private HashMap<String, String>  mFiles;

	public WYImageCache() {
		mFiles = new HashMap<>();

		mCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 6)) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public WYImageCache(String dir) {
		mCacheDir = dir;
		mFiles = new HashMap<>();

		mCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 6)) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	private Bitmap get(String key) {
		return mCache.get(key);
	}

	public Bitmap loadFromFile(String path, int width, int height) {
		Bitmap bmp = mCache.get(path);
		if (bmp == null) {
			bmp = WYBitmap.decodeSampledBitmapFromFile(path, width, height);
			put(path, bmp);
		}
		return bmp;
	}

	private void put(String key, Bitmap bitmap) {
		if (bitmap != null) {
			mCache.put(key, bitmap);
		}
	}

	public Bitmap loadFromResource(Resources res, int resId, int width, int height) {
		Bitmap bmp = mCache.get(String.valueOf(resId));
		if (bmp != null) {
			return bmp;
		}
		bmp = WYBitmap.decodeSampledBitmapFromResource(res, resId, width, height);
		put(String.valueOf(resId), bmp);
		return bmp;
	}

	/*
	public Bitmap loadFromUrl(String url, int width, int height) {

		Bitmap bmp = mCache.get(url);
		if (bmp == null) {
			if (mFiles.containsKey(url)) {
				bmp = BitmapUtils.decodeSampledBitmapFromFile(mFiles.get(url), width, height);
				mCache.put(url, bmp);
				return bmp;
			} else {
				bmp = HttpUtils.loadImage(url, width, height);

				if (bmp != null) {
					String path = mCacheDir + System.currentTimeMillis() + ".png";
					File file = FileUtils.savePhoto(bmp, path);
					mFiles.put(url, file.getAbsolutePath());

					put(url, bmp);
				}
			}
		}
		return bmp;
	}
	*/

	/** load image from url */
	public Bitmap loadFromUrl(String url) {
		Bitmap bmp = mCache.get(url);

		if (bmp == null) {
			try {
				bmp = BitmapFactory.decodeStream(new URL(url).openStream());
			} catch (Exception e) {
				Log.e("mylog", "ImageCache->loadFromUrlNoFile: error -> " + e.getMessage());
				return null;
			}
			put(url, bmp);
		}
		return bmp;
	}

	/** load image from url and set to a given size */
	public Bitmap loadFromUrl(String url, int width, int height) {
		Bitmap bmp = mCache.get(url);

		if (bmp == null) {
			try {
				bmp = WYBitmap.decodeSampledBitmapFromUrl(new URL(url), width, height);
			} catch (Exception e) {
				Log.e("mylog", "ImageCache->loadFromUrlNoFile: error -> " + e.getMessage());
				return null;
			}
			put(url, bmp);
		}
		return bmp;
	}
}
