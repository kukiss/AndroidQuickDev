package weiy.app.basic.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/** Created by kukiss on 2014/11/9 0009. */
public class TimePickDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private OnTimePickListener mTimePickListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Calendar calendar = Calendar.getInstance();
		MyTimePickerDialog dialog = new MyTimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
		return dialog;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if (mTimePickListener != null) {
			mTimePickListener.onTimePick(hourOfDay + ":" + minute);
		}
	}

	public interface OnTimePickListener {
		void onTimePick(String time);
	}

	public void setOnTimePickListener(OnTimePickListener listener) {
		mTimePickListener = listener;
	}
}
