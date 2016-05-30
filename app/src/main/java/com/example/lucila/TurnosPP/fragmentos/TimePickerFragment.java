package com.example.lucila.turnosPP.fragmentos;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Este fragmento muestra un picker de fecha u hora. Las actividades que deseen usarlo
 * tienen que implementar la interfaz OnFechaElegidaListener.
 */
public class TimePickerFragment
        extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    OnHoraElegidaListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Usa el tiempo actual para el picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),
                                        this,
                                        hour,
                                        minute,
                                        DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHoraElegidaListener) {
            mListener = (OnHoraElegidaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHoraElegidaListener");
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Se ejecuta cuando se acepta el dialog.
        if(mListener != null) {
            //Genero un string que representa la hora elegida
            String horaString;
            String hora, minuto;
            if(minute < 10)
               minuto=  "0" + minute;
            else
               minuto= "" + minute;
            if(hourOfDay < 10)
                hora= "0" + hourOfDay;
            else
                hora= "" + hourOfDay;
            horaString= hora + ":" + minuto;
            //Hago el callback a la actividad
            mListener.onHoraElegida(horaString, hourOfDay, minute);
        }
    }

    /**
     * Interfaz que deben implementar las actividades que quieran usar este diálogo.
     */
    public interface OnHoraElegidaListener {
        public void onHoraElegida(String stringHora, int hora, int min);
    }
}
