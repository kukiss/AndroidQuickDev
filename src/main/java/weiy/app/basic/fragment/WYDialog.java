package weiy.app.basic.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by kukiss on 2014/10/4 0004.
 * 封装好的DialogFragment直接可用。
 */
public class WYDialog extends DialogFragment {

	private OnClickListener mCallback;

	/** 获得一个只显示标题，信息和确定按钮的Dialog */
	public static WYDialog getInstance(String title, String msg, boolean cancelable) {
		WYDialog dialog = new WYDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("msg", msg);
		args.putBoolean("cancelable", cancelable);
		dialog.setArguments(args);
		return dialog;
	}

	/** 获得一个显示标题，信息以及按钮的Dialog，传空字符串将不设置按钮 */
	public static WYDialog getInstance(String title, String msg, String positionBtn, String negativeBtn, boolean cancelable) {
		WYDialog dialog = new WYDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("msg", msg);
		args.putString("positionBtn", positionBtn);
		args.putString("negativeBtn", negativeBtn);
		args.putBoolean("cancelable", cancelable);
		dialog.setArguments(args);

		return dialog;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnClickListener) getActivity();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final AlertDialog dialog = builder.create();

		// 根据参数设置dialog
		Bundle args = getArguments();
		dialog.setTitle(args.getString("title"));
		dialog.setMessage(args.getString("msg"));
		setCancelable(args.getBoolean("cancelable"));

		String positionBtn = args.getString("positionBtn");
		if (positionBtn != null) {
			dialog.setButton(AlertDialog.BUTTON_POSITIVE, positionBtn, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					if (mCallback != null) mCallback.onButtonClick(dialogInterface, i);
				}
			});
		}

		String negativeBtn = args.getString("negativeBtn");
		if (negativeBtn != null) {
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeBtn, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					if (mCallback != null) mCallback.onButtonClick(dialogInterface, i);
				}
			});
		}

		return dialog;
	}

	public void setOnClickListener(OnClickListener callback) {
		mCallback = callback;
	}

	public interface OnClickListener {
		void onButtonClick(DialogInterface dialogInterface, int button);
	}
}
