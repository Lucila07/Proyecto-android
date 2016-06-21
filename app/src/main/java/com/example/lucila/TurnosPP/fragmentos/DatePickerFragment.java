package com.example.lucila.turnosPP.fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment
        extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    OnFechaElegidaListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(mListener != null) {
            mListener.onFechaElegida(dayOfMonth,monthOfYear,year);
        }
    }

    /**
     * Interfaz que deben implementar las actividades que quieran usar este diálogo.
     */
    public interface OnFechaElegidaListener {
        void onFechaElegida(int dia, int mes, int anio);
    }
}
