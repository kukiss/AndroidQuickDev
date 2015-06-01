package weiy.app.basic.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public abstract class WYCompatActivity extends AppCompatActivity implements OnClickListener {

	protected int               mContentView;
	protected int               mMenuView; // set a menu
	protected Handler           mHandler;
	protected BroadcastReceiver mReceiver;
	protected Resources         mRes; // recources

	public WYCompatActivity(int contentView) {
		mContentView = contentView;
	}

	public WYCompatActivity(int contentView, int menuView) {
		mContentView = contentView;
		mMenuView = menuView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doFirst();
		setContentView(mContentView);
		findViews();
		mRes = getApplicationContext().getResources();
		doCreate();
	}

	@Override
	protected void onDestroy() {
		if (mReceiver != null) unregisterReceiver(mReceiver);

		if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);

		if (mHandler != null) mHandler.removeCallbacksAndMessages(null); // prevent memory leak

		super.onDestroy();
	}

	protected void doFirst() {}

	protected abstract void doCreate();

	protected void findViews() {}

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

	protected void addRecAction(IntentFilter filter) {}

	protected void doReceive(Intent intent) {}

	/** 增加一个handler */
	protected void addHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (!isFinishing()) doHandle(msg);
			}
		};
	}

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
