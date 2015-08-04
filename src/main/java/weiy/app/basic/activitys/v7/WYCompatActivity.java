package weiy.app.basic.activitys.v7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class WYCompatActivity extends AppCompatActivity implements OnClickListener {

	private int               mContentView;
	private int               mMenuView;
	private Handler           mHandler;
	private BroadcastReceiver mReceiver;
	private Resources         mRes;
	private Toolbar           mToolbar;

	/** 使用一个布局文件来初始化Activity */
	public WYCompatActivity(int contentView) {
		mContentView = contentView;
	}

	/** 指定一个布局文件和Menu文件来初始化Activity */
	public WYCompatActivity(int contentView, int menuView) {
		mContentView = contentView;
		mMenuView = menuView;
	}

	/** 获取Resource对象 */
	public Resources getRes() {
		return mRes;
	}

	/** 获取Handler对象 */
	public Handler getHandler() {
		return mHandler;
	}

	public Toolbar getToolbar() {
		return mToolbar;
	}

	public void setToolbar(int id) {
		mToolbar = (Toolbar) findViewById(id);
		if (mToolbar != null) setSupportActionBar(mToolbar);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doFirst();
		if (mContentView != 0) setContentView(mContentView);
		ButterKnife.inject(this);
		mRes = getApplicationContext().getResources();
		doCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		if (mReceiver != null) unregisterReceiver(mReceiver);
		if (mHandler != null) mHandler.removeCallbacksAndMessages(null); // prevent memory leak
		super.onDestroy();
	}

	/** 一些需要在setContentView方法之前调用的方法 */
	protected void doFirst() {}

	protected void setBackEnable(boolean bool) {
		ActionBar bar = getSupportActionBar();
		if (bar != null) bar.setDisplayHomeAsUpEnabled(bool);
	}

	protected abstract void doCreate(Bundle savedInstanceState);

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mMenuView != 0) {
			getMenuInflater().inflate(mMenuView, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return false;
		}
		return doItemSelected(item);
	}

	protected boolean doItemSelected(MenuItem item) {
		return false;
	}

	@Override
	public void onClick(View v) {}

	/** 注册广播接受器 */
	protected void regReceiver() {
		IntentFilter filter = new IntentFilter();
		addRecAction(filter);
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				doReceive(intent);
			}
		};
		registerReceiver(mReceiver, filter);
	}

	/** 添加广播过滤, 一般先调用regReceiver方法注册广播接收器 */
	protected void addRecAction(IntentFilter filter) {}

	/** 继承这个方法可以接受广播事件, 但务必先调用regReceiver方法注册广播接收器和addRecAction方法添加过滤 */
	protected void doReceive(Intent intent) {}

	/**
	 * 增加一个handler
	 */
	protected void addHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (!isFinishing()) doHandle(msg);
			}
		};
	}

	/** 继承这个方法可以实现handler的message事件, 但务必先调用addHandler方法 */
	protected void doHandle(Message msg) {}

	protected void showToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void showToast(int resId) {
		Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
	}

	protected void myLog(String msg) {
		Log.e("mylog", msg);
	}
}
