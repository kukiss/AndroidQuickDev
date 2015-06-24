package weiy.app.basic.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/** Created by kukiss on 2015/1/23 0023. */
public abstract class WYDialogFragment extends DialogFragment implements View.OnClickListener {

	protected View   mView;
	protected Bundle mBundle;
	private   int    mContentView;
	private   int    mMenuRes;

	public WYDialogFragment(int contentView) {
		mContentView = contentView;
	}

	public WYDialogFragment(int contentView, int menuRes) {
		mContentView = contentView;
		mMenuRes = menuRes;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(mContentView, container, false);
		mBundle = getArguments() == null ? new Bundle() : getArguments();
		findViews();
		ButterKnife.inject(this, mView);
		doCreateView();
		return mView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (mMenuRes != 0) inflater.inflate(mMenuRes, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = getArguments();
		setStyle(STYLE_NO_TITLE, 0);
		doCreate();
	}

	@Override
	public void onDestroy() {
		if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {}

	protected void doCreate() {}

	protected void doCreateView() {}

	@Deprecated protected void findViews() {}

	protected void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void myLog(String log) {
		Log.e("mylog", log);
	}
}
