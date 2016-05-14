package com.example.lucila.myapplication;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lucila.beans.Oferta;

import java.util.Calendar;

/**
 * Actividad que implementa la creación de ofertas.
 */
public class CrearOfertasActivity
        extends AppCompatActivity
        implements CrearOfertasFragment.OnCrearOfertaListener,
                   TimePickerFragment.OnFechaElegidaListener {

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
    public void mostrarDialogo() {
        DialogFragment fecha= new TimePickerFragment();
        fecha.show(getSupportFragmentManager(),"timePicker");
    }

    @Override
    public void onFechaElegida(String fecha, int hora, int min) {
        CrearOfertasFragment f= (CrearOfertasFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_crear_oferta);
        f.setFecha(fecha);
    }
}
