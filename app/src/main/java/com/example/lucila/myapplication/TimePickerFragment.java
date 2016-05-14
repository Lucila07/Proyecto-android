package com.example.lucila.myapplication;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    OnFechaElegidaListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFechaElegidaListener) {
            mListener = (OnFechaElegidaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFechaElegidaListener");
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(mListener != null) {
            String horaString= hourOfDay + ":" + minute;
            mListener.onFechaElegida(horaString, hourOfDay, minute);
        }
    }

    public interface OnFechaElegidaListener {
        public void onFechaElegida(String fecha, int hora, int min);
    }
}
