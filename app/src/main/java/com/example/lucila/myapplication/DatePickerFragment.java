package com.example.lucila.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

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
            String fechaElegida;
            String mes, dia;
            if(monthOfYear < 10)
                mes= "/0" + monthOfYear;
            else
                mes= "/" + monthOfYear;
            if(dayOfMonth < 10)
                dia= "0" + dayOfMonth;
            else
                dia= "" + dayOfMonth;
            fechaElegida= dia + mes;
            mListener.onFechaElegida(fechaElegida,dayOfMonth,monthOfYear,year);
        }
    }

    /**
     * Interfaz que deben implementar las actividades que quieran usar este diálogo.
     */
    public interface OnFechaElegidaListener {
        public void onFechaElegida(String stringFecha, int dia, int mes, int anio);
    }
}
