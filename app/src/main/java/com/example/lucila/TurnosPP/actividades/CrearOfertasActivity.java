package com.example.lucila.turnosPP.actividades;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.beans.VolleySingleton;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.turnosPP.fragmentos.CrearOfertasFragment;
import com.example.lucila.turnosPP.fragmentos.TimePickerFragment;
import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.fragmentos.DatePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

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
    private int idEst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ofertas);

        ofertaACrear= new Oferta();
        idEst= getIntent().getIntExtra("id",0);
        if(savedInstanceState != null) {
            deportes= savedInstanceState.getStringArray("deportes");
            ofertaACrear= (Oferta) savedInstanceState.getSerializable("oferta");
            idEst= savedInstanceState.getInt("id");
        }
        else
            deportes= (String[]) getIntent().getSerializableExtra("Tdeportes");

        getSupportFragmentManager().beginTransaction().add(
                R.id.panel_fragment_crearOfertas,
                CrearOfertasFragment.newInstance(deportes),
                TAG
        ).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("deportes", deportes);
        savedInstanceState.putSerializable("oferta", ofertaACrear);
        savedInstanceState.putInt("id",idEst);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onCrearOferta(Oferta oferta) {
        //TODO Crear oferta en el server.
        ofertaACrear= oferta;
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

    }

    @Override
    public void onFechaElegida(String stringFecha, int dia, int mes, int anio) {
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        fragmentCO.setFecha(stringFecha, dia, mes, anio);
    }

    private void crearRequestAlServer(Oferta oferta) {
        Map<String,String> mapeoJSON= new HashMap<>();
        mapeoJSON.put("funcion","crearOferta");
        Calendar fechaElegida= oferta.getFecha();
        String fecha= fechaElegida.get(Calendar.YEAR) + "-" +
                fechaElegida.get(Calendar.MONTH) + "-" +
                fechaElegida.get(Calendar.DAY_OF_MONTH) + " " +
                fechaElegida.get(Calendar.HOUR) + ":" +
                fechaElegida.get(Calendar.MINUTE) + ":00";

        mapeoJSON.put("fecha", fecha);
        mapeoJSON.put("idEstablecimiento", Integer.toString(idEst));
        mapeoJSON.put("deporte", Integer.toString(oferta.getIdDeporte()));
        mapeoJSON.put("nombreDeporte", oferta.getNombreDeporte());
        mapeoJSON.put("precioHab", Float.toString(oferta.getPrecioHabitual()));
        mapeoJSON.put("precioAct", Float.toString(oferta.getPrecioFinal()));
        crearPOSTJson(mapeoJSON);
    }

    private void crearPOSTJson(Map<String,String> map) {
        JSONObject postJSON= new JSONObject(map);
        Log.d("TAG", "json= "+postJSON.toString());
        //Lo envio al server
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.LOGIN,
                        postJSON,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                procesarRespuesta(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    private void procesarRespuesta(JSONObject respuesta) {
        try {
            boolean fallo= respuesta.getInt("estado") == 2;
            if(!fallo)
                finish();
            else
                Toast.makeText(this,"Se produjo un error",Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
