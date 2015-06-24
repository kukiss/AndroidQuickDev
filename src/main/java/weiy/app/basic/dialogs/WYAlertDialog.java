package weiy.app.basic.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by kukiss on 2014/10/4 0004.
 * 封装好的DialogFragment直接可用.
 */
public class WYAlertDialog extends DialogFragment {

	private AlertDialog     dialog;
	private OnClickListener mCallback;

	public WYAlertDialog() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		dialog = builder.create();
		return dialog;
	}

	public void setOnClickListener(OnClickListener callback) {
		mCallback = callback;
	}

	public WYAlertDialog setTitle(String title) {
		dialog.setTitle(title);
		return this;
	}

	public interface OnClickListener {
		void onButtonClick(DialogInterface dialogInterface, int button);
	}
}
