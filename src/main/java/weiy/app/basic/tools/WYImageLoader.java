package weiy.app.basic.tools;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.badoo.mobile.util.WeakHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Created by kukiss on 2015/1/16 0016. */
public class WYImageLoader {

	private WYImageCache    mCache;
	private ExecutorService mThreads;

	public WYImageLoader(int threadCount) {

		mCache = new WYImageCache();
		mThreads = Executors.newFixedThreadPool(threadCount);
	}

	public WYImageLoader(String dir) {

		mCache = new WYImageCache(dir);
		mThreads = Executors.newFixedThreadPool(2);
	}

	public void loadImage(final String url, final int width, final int height, final ImageView view) {

		final WeakHandler handler = new WeakHandler();

		mThreads.execute(new Runnable() {
			@Override
			public void run() {

				final Bitmap bmp = mCache.loadFromUrl(url, width, height);
				if (bmp != null) {
					handler.post(new Runnable() {
						@Override public void run() {
							view.setImageBitmap(bmp);
						}
					});
				}
			}
		});
	}

	public void loadImage(final String url) {
		mCache.loadFromUrl(url);
	}
}