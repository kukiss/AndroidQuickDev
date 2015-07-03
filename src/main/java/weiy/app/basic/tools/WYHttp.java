package weiy.app.basic.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

public class WYHttp {

	@Deprecated
	public static String send(String path, Map<String, String> params, Map<String, String> files) {

		if (files == null) {
			return post(path, files);
		} else {
			return uploadFile(path, params, files);
		}
	}

	/** 以指定路径和参数发送post请求, 参数可以为null. */
	public static String get(String path) {

		ArrayList<NameValuePair> list = null;

		HttpGet get = new HttpGet(path);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // 设置网络超时

		// 设置post请求的参数.
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e("mylog", e.getMessage());
		}
		return "";
	}


	/** 以指定路径和参数发送post请求, 参数可以为null. */
	public static String post(String path, Map<String, String> params) {

		ArrayList<NameValuePair> list = null;
		if (params != null) {
			list = new ArrayList<>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}

		HttpPost post = new HttpPost(path);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // 设置网络超时

		// 设置post请求的参数.
		try {
			if (list != null) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
				post.setEntity(entity);
			}
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e("mylog", e.getMessage());
		}
		return "";
	}

	/** 以指定的路径和参数以及文件上传文件, 参数可以为null. */
	public static String uploadBytes(String path, Map<String, String> params, Map<String, byte[]> files) {
		HttpPost httpPost = new HttpPost(path);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // 设置网络超时.
		MultipartEntity entity = new MultipartEntity();
		try {
			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					entity.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName("UTF-8")));
				}
			}
			if (files !=null) {
				for (Map.Entry<String, byte[]> entry : files.entrySet()) {
					entity.addPart(entry.getKey(), new ByteArrayBody(entry.getValue(), ""));
				}
			}
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e("mylog", e.getMessage());
		}
		return "";
	}

	/** 以指定的路径和参数以及文件上传文件, 参数可以为null. */
	public static String uploadFile(String path, Map<String, String> params, Map<String, String> files) {
		HttpPost httpPost = new HttpPost(path);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // 设置网络超时.
		MultipartEntity entity = new MultipartEntity();
		try {
			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					entity.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName("UTF-8")));
				}
			}
			for (Map.Entry<String, String> entry : files.entrySet()) {
				entity.addPart(entry.getKey(), new FileBody(new File(entry.getValue())));
			}
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e("mylog", e.getMessage());
		}
		return "";
	}

	/** 从网络下载图片. */
	public static File uploadImage(String url, String path, int width, int height) {

		File file = null;
		try {
			URL imgUrl = new URL(url);
			file = WYFile.savePhoto(BitmapFactory.decodeStream(imgUrl.openStream()), path);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	/** 从网络下载图片. */
	public static Bitmap loadImage(String url, int width, int height) {
		Bitmap bmp = null;
		try {
			URL imgUrl = new URL(url);
			bmp = WYBitmap.decodeSampledBitmapFromUrl(imgUrl, width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public static Bitmap loadImage(String url) {
		Bitmap bmp = null;
		try {
			URL imgUrl = new URL(url);
			bmp = BitmapFactory.decodeStream(imgUrl.openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}
}
