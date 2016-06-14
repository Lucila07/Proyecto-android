package com.example.lucila.turnosPP.actividades;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.fragmentos.CrearOfertasFragment;
import com.example.lucila.turnosPP.fragmentos.TimePickerFragment;
import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.fragmentos.DatePickerFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Actividad que implementa la creación de ofertas.
 */
public class CrearOfertasActivity
        extends AppCompatActivity
        implements CrearOfertasFragment.OnCrearOfertaListener,
        TimePickerFragment.OnHoraElegidaListener,
                   DatePickerFragment.OnFechaElegidaListener {
    private static String TAG= "FRAGMENT_CREAR_OFERTA";

    private Oferta ofertaACrear;
    private String[] deportes;
    private Calendar fecha;
    private int idEst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ofertas);

        fecha= Calendar.getInstance();
        idEst= getIntent().getIntExtra("id",0);
        if(savedInstanceState != null) {
            deportes= savedInstanceState.getStringArray("deportes");
            fecha= (Calendar) savedInstanceState.getSerializable("fecha");
            idEst= savedInstanceState.getInt("id");
        }
        else
            deportes= (String[]) getIntent().getSerializableExtra("deportes");

        getSupportFragmentManager().beginTransaction().add(
                R.id.panel_fragment_crearOfertas,
                CrearOfertasFragment.newInstance(deportes),
                TAG
        ).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("deportes", deportes);
        savedInstanceState.putSerializable("fecha", fecha);
        savedInstanceState.putInt("id",idEst);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onCrearOferta(Oferta oferta) {
        //TODO Crear oferta en el server.
        crearRequestAlServer(oferta);
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
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        fragmentCO.setHora(stringHora, hora, min);
        fecha.set(
                fecha.get(Calendar.YEAR),
                fecha.get(Calendar.MONTH),
                fecha.get(Calendar.DAY_OF_MONTH),
                hora,
                min
        );
    }

    @Override
    public void onFechaElegida(String stringFecha, int dia, int mes, int anio) {
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        fragmentCO.setFecha(stringFecha, dia, mes, anio);
        fecha.set(
                anio,
                mes,
                dia,
                fecha.get(Calendar.HOUR),
                fecha.get(Calendar.MINUTE)
        );
    }

    private void crearRequestAlServer(Oferta oferta) {
        Map<String,String> mapeoJSON= new HashMap<>();
        mapeoJSON.put("funcion","crearOferta");
        String fecha= this.fecha.get(Calendar.YEAR) + "-" +
                 this.fecha.get(Calendar.MONTH) + "-" +
                 this.fecha.get(Calendar.DAY_OF_MONTH) + " " +
                 this.fecha.get(Calendar.HOUR) + ":" +
                 this.fecha.get(Calendar.MINUTE) + ":00";

        mapeoJSON.put("fecha",fecha);
        mapeoJSON.put("idEstablecimiento",Integer.toString(idEst));/*
        mapeoJSON.put("deporte",);
        mapeoJSON.put("precioHab",);
        mapeoJSON.put("precioAct",);
        mapeoJSON.put("fecha",);
        mapeoJSON.put("hora",);*/
    }
}
