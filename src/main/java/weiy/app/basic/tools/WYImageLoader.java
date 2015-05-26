package weiy.app.basic.tools;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Created by kukiss on 2015/1/16 0016. */
public class WYImageLoader {

	WYImageCache    mCache;
	ExecutorService mThreads;

	public WYImageLoader(String dir) {

		mCache = new WYImageCache(dir);
		mThreads = Executors.newFixedThreadPool(2);
	}

	public void loadImage(final String url, final int width, final int height, final ImageView view) {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				if (msg.obj != null) view.setImageBitmap((Bitmap) msg.obj);
			}
		};

		mThreads.execute(new Runnable() {
			@Override
			public void run() {

				Bitmap bmp = mCache.loadFromUrl(url, width, height);
				handler.obtainMessage(100, bmp).sendToTarget();
			}
		});
	}
}