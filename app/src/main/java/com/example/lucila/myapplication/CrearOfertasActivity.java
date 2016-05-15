package com.example.lucila.myapplication;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lucila.beans.Oferta;

import java.sql.Time;
import java.util.Calendar;

/**
 * Actividad que implementa la creación de ofertas.
 */
public class CrearOfertasActivity
        extends AppCompatActivity
        implements CrearOfertasFragment.OnCrearOfertaListener,
                   TimePickerFragment.OnHoraElegidaListener,
                   DatePickerFragment.OnFechaElegidaListener {

    private Oferta ofertaACrear;
    private Calendar fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ofertas);
    }


    @Override
    public void onCrearOferta(Oferta oferta) {
        //TODO Crear oferta en el server.
    }

    @Override
    public void mostrarDialogoHora() {
        DialogFragment hora= new TimePickerFragment();
        hora.show(getSupportFragmentManager(),"timePicker");
    }

    @Override
    public void mostrarDialogoFecha() {
        DialogFragment fecha= new DatePickerFragment();
        fecha.show(getSupportFragmentManager(),"datePicker");
    }

    @Override
    public void onHoraElegida(String stringHora, int hora, int min) {
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_crear_oferta);
        fragmentCO.setHora(stringHora);
    }

    @Override
    public void onFechaElegida(String stringFecha, int dia, int mes, int anio) {
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_crear_oferta);
        fragmentCO.setFecha(stringFecha);
    }
}
