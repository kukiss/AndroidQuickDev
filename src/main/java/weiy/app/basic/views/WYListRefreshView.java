package weiy.app.basic.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import weiy.app.basic.R;


/** Created by weiy on 15-6-1. */
public class WYListRefreshView extends RelativeLayout {

	public static final int FLAG_LOAD_FAIL    = 1;
	public static final int FLAG_LOAD_SUCCESS = 2;

	private ListView vList;
	private Button   vRefresh;

	private Context  mCon;
	private Callback mCallback;

	public WYListRefreshView(Context context) {
		super(context);
		mCon = context;
		init();
	}

	public WYListRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCon = context;
		init();
	}

	private void init() {
		LayoutInflater.from(mCon).inflate(R.layout.wy_view_list_refresh, this);
		vList = (ListView) findViewById(R.id.listview);

		vRefresh = (Button) findViewById(R.id.refresh);
		vRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCallback != null) {
					mCallback.onResult(FLAG_LOAD_FAIL);
					vRefresh.setVisibility(GONE);
				}
			}
		});
	}

	public void setAdapter(BaseAdapter adapter) {
		if (adapter == null || adapter.getCount() == 0) {
			vRefresh.setVisibility(VISIBLE);
		} else {
			vList.setAdapter(adapter);
			vRefresh.setVisibility(GONE);
			if (mCallback != null) mCallback.onResult(FLAG_LOAD_SUCCESS);
		}
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		vList.setOnItemClickListener(listener);
	}
	public void setOnScrollListener(AbsListView.OnScrollListener listener) {
		vList.setOnScrollListener(listener);
	}

	public interface Callback {
		void onResult(int flag);
	}

	public void setCallback(Callback callback) {
		mCallback = callback;
	}
}
