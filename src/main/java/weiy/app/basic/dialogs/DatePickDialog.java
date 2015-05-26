package weiy.app.basic.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/** Created by kukiss on 2014/11/9 0009. */
public class DatePickDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private OnDatePickListener mCallback;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Calendar calendar = Calendar.getInstance();
		MyDatePickerDialog dialog = new MyDatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		return dialog;
	}

	@Override
	public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
		if (mCallback != null) {
			mCallback.onDatePick(i + "-" + (i2+1) + "-" + i3);
		}
	}

	public interface OnDatePickListener {
		void onDatePick(String date);
	}

	public void setOnDatePickListener(OnDatePickListener callback) {
		mCallback = callback;
	}
}
