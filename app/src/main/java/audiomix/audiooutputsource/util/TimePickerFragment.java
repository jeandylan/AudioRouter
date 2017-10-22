package audiomix.audiooutputsource.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by dylan on 13-Aug-17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private OnUserSetTimeListener onUserSetTimeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog= new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        return timePickerDialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        try {
            onUserSetTimeListener = (OnUserSetTimeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        onUserSetTimeListener = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }

    public interface OnUserSetTimeListener {
        public void onUserSetTime(String tag,int h,int m);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
      if(onUserSetTimeListener !=null) onUserSetTimeListener.onUserSetTime(getTag(),hourOfDay,minute);

    }


}