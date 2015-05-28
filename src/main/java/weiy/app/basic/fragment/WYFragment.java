package weiy.app.basic.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class WYFragment extends Fragment implements OnClickListener {

	protected Handler           mHandler;
	protected View              mView;
	protected Activity          mActivity;
	protected Bundle            mBundle;
	protected String            mTitle;
	protected Resources         mRes;
	private   int               mContentView;
	private   int               mMenuItem;
	private   BroadcastReceiver mReceiver;

	public WYFragment(int view) {
		mContentView = view;
	}

	public WYFragment(int view, String title) {
		mContentView = view;
		mTitle = title;
	}

	public WYFragment(int view, int menuView) {
		mContentView = view;
		mMenuItem = menuView;
	}

	public WYFragment(int view, int menuItem, String title) {
		mContentView = view;
		mMenuItem = menuItem;
		mTitle = title;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mActivity = getActivity();
		mRes = getActivity().getApplicationContext().getResources();
		mBundle = getArguments() == null ? new Bundle() : getArguments();
		doCreate();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(mContentView, container, false);
		findViews();
		doCreateView();
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mTitle != null && !mTitle.equals("")) {
			mActivity.setTitle(mTitle);
		}
	}

	@Override
	public void onDestroy() {
		if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
		if (mReceiver != null) LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (mMenuItem != 0) {
			inflater.inflate(mMenuItem, menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return doItemSelected(item);
	}

	protected boolean doItemSelected(MenuItem item) {
		return false;
	}

	protected abstract void doCreateView();

	protected void doCreate() {}

	protected void findViews() {}

	@Override
	public void onClick(View v) {}

	protected void regReceiver() {
		IntentFilter filter = new IntentFilter();
		addRecAction(filter);
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				doReceive(intent);
			}
		};
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);
	}

	protected void addRecAction(IntentFilter filter) {}

	protected void doReceive(Intent intent) {}

	/** 增加一个内部handler类 */
	protected void addHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				doHandle(msg);
			}
		};
	}

	protected void doHandle(Message msg) {}

	protected void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void myLog(String log) {
		Log.e("mylog", log);
	}

}
