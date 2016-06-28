package com.example.lucila.turnosPP.actividades;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.turnosPP.fragmentos.CrearOfertasFragment;
import com.example.lucila.turnosPP.fragmentos.DatePickerFragment;
import com.example.lucila.turnosPP.fragmentos.TimePickerFragment;
import com.example.lucila.turnosPP.servicios.VolleyRequestService;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditarOfertaActivity
        extends ToolbarActivity
        implements CrearOfertasFragment.OnCrearOfertaListener,
                TimePickerFragment.OnHoraElegidaListener,
                DatePickerFragment.OnFechaElegidaListener {

    private static String TAG= "FRAGMENT_CREAR_OFERTA";

    private String[] deportes;
    private Oferta ofertaAEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_oferta);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if(savedInstanceState != null) {
            deportes= savedInstanceState.getStringArray(Constantes.ARREGLO_DEPORTES_ESTABLECIMIENTO);
            ofertaAEditar= (Oferta) savedInstanceState.getSerializable(Constantes.OFERTA_EDITAR);
        } else {
            deportes= getIntent().getStringArrayExtra(Constantes.ARREGLO_DEPORTES_ESTABLECIMIENTO);
            ofertaAEditar= (Oferta) getIntent().getSerializableExtra(Constantes.OFERTA_EDITAR);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.panel_editar_oferta,
                        CrearOfertasFragment.newInstance(deportes, true /*Editar*/, ofertaAEditar),
                        TAG)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constantes.OFERTA_EDITAR, ofertaAEditar);
        outState.putStringArray(Constantes.ARREGLO_DEPORTES_ESTABLECIMIENTO, deportes);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void logout() {

    }

    @Override
    protected void perfil() {

    }

    @Override
    protected void opciones() {

    }

    //region FRAGMENT_METHODS
    @Override
    public void onCrearOferta(Oferta oferta, boolean editar) {
        //Se editó la oferta y hay que actualizarla
        Map<String, String> mapeoJSON= new HashMap<>();
        mapeoJSON.put("funcion","actualizarOferta");
        mapeoJSON.put("idOferta", Integer.toString(oferta.getCodigo()));
        Calendar fechaElegida= oferta.getFecha();
        fechaElegida.set(Calendar.SECOND, 0);
        SimpleDateFormat formatoFecha= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String fecha= formatoFecha.format(fechaElegida.getTime());
        mapeoJSON.put("fecha", fecha);
        mapeoJSON.put("nombreDeporte", oferta.getNombreDeporte());
        mapeoJSON.put("precioHab", Float.toString(oferta.getPrecioHabitual()));
        mapeoJSON.put("precioAct", Float.toString(oferta.getPrecioFinal()));
        //Inicio el request al servicio
        VolleyRequestService.startActionPostRequest(this, Constantes.UPDATE, mapeoJSON);
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
    public void eliminarOferta(int codigo) {
        Map<String,String> mapeoJSON= new HashMap<>();
        mapeoJSON.put("funcion", "eliminarOferta");
        mapeoJSON.put("codigo", Integer.toString(codigo));
        VolleyRequestService.startActionPostRequest(this, Constantes.UPDATE, mapeoJSON);
    }

    @Override
    public void onHoraElegida(int hora, int min) {
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        fragmentCO.setHora(hora, min);
    }

    @Override
    public void onFechaElegida(int dia, int mes, int anio) {
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        fragmentCO.setFecha(dia, mes, anio);
    }
    //endregion

    @Override
    protected void procesarRespuestaExitosa(JSONObject respuesta) {
        finish();
    }
}
