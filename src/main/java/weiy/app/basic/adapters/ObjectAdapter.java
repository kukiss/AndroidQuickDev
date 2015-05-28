package weiy.app.basic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectAdapter<T> extends BaseAdapter {
	protected List<T> mData;
	protected Context mContext;
	protected int     mView;

	public ObjectAdapter() {
	}

	public ObjectAdapter(List<T> data, Context context, int view) {
		mData = data == null ? new ArrayList<T>() : data;
		mContext = context;
		mView = view;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mView, null);
		}
		T obj = mData.get(position);
		doView(position, convertView, obj);
		return convertView;
	}

	protected abstract void doView(int position, View view, T obj);

	public void setView(int view) {
		mView = view;
	}

	public List<T> getData() {
		return mData;
	}

	public void setData(List<T> data) {
		mData = data == null ? new ArrayList<T>() : data;
		notifyDataSetChanged();
	}

	public void additem(T map) {
		mData.add(map);
		notifyDataSetChanged();
	}

	public void removeItem(int position) {
		mData.remove(position);
		notifyDataSetChanged();
	}

	public void clear() {
		mData.clear();
		notifyDataSetChanged();
	}
}
