package weiy.app.basic.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by kukiss on 2014/10/4 0004.
 * 封装好的DialogFragment直接可用.
 */
public class WYDialog extends DialogFragment {

	private String          title;
	private String          msg;
	private String          positionBtn;
	private String          negativeBtn;
	private boolean         cancelable;
	private OnClickListener mCallback;

	public static WYDialog getInstance(String title, String msg) {
		return getInstance(title, msg, null, null, true, null);
	}

	public static WYDialog getInstance(String title, String msg, boolean cancelable) {
		return getInstance(title, msg, null, null, cancelable, null);
	}

	/** 获得一个只显示标题，信息和确定按钮的Dialog */
	public static WYDialog getInstance(String title, String msg, boolean cancelable, OnClickListener listener) {
		return getInstance(title, msg, null, null, cancelable, listener);
	}

	/** 获得一个显示标题，信息以及按钮的Dialog，传空字符串将不设置按钮 */
	public static WYDialog getInstance(String title, String msg, String positionBtn, String negativeBtn, boolean cancelable) {
		return getInstance(title, msg, positionBtn, negativeBtn, cancelable, null);
	}

	/** get a dialog, if you don't want to set button, just pass null */
	public static WYDialog getInstance(String title, String msg, String positionBtn, String negativeBtn, boolean cancelable, OnClickListener listener) {
		WYDialog dialog = new WYDialog();
		dialog.init(title, msg, positionBtn, negativeBtn, cancelable, listener);
		return dialog;
	}

	public void init(String title, String msg, String positionBtn, String negativeBtn, boolean cancelable, OnClickListener listener) {
		this.title = title;
		this.msg = msg;
		this.positionBtn = positionBtn;
		this.negativeBtn = negativeBtn;
		this.cancelable = cancelable;
		mCallback = listener;
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
		final AlertDialog   dialog  = builder.create();

		// 根据参数设置dialog
		dialog.setTitle(title);
		dialog.setMessage(msg);
		setCancelable(cancelable);

		if (positionBtn != null) {
			dialog.setButton(AlertDialog.BUTTON_POSITIVE, positionBtn, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					if (mCallback != null) mCallback.onButtonClick(dialogInterface, i);
				}
			});
		}

		if (negativeBtn != null) {
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeBtn, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					if (mCallback != null) mCallback.onButtonClick(dialogInterface, i);
				}
			});
		}

		if (positionBtn == null && negativeBtn == null) {
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "确定", new DialogInterface.OnClickListener() {
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
