package com.example.lucila.turnosPP.actividades;

import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
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
    private boolean editar, eliminar, crearOferta;
    private int cantOfertasCreadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ofertas);

        idEst= getIntent().getIntExtra("id",0);
        if(savedInstanceState != null) {
            deportes= savedInstanceState.getStringArray("deportes");
            ofertaACrear= (Oferta) savedInstanceState.getSerializable("oferta");
            idEst= savedInstanceState.getInt("id");
        }
        else
            deportes= (String[]) getIntent().getSerializableExtra("Tdeportes");

        editar= getIntent().getBooleanExtra("editar", false);
        if(editar)
            ofertaACrear= (Oferta) getIntent().getSerializableExtra("ofertaEditar");
        else
            ofertaACrear= new Oferta();
        crearOferta= false;
        eliminar= false;
        getSupportFragmentManager().beginTransaction().add(
                R.id.panel_fragment_crearOfertas,
                CrearOfertasFragment.newInstance(deportes, editar, ofertaACrear),
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

    protected void onStart() {
        super.onStart();
        SharedPreferences sp=  getSharedPreferences("ofertasCreadas", MODE_PRIVATE);
        cantOfertasCreadas= sp.getInt(getString(R.string.cant_ofertas_creadas), 0);
    }

    protected void onStop() {
        if(crearOferta) {
            SharedPreferences sp=  getSharedPreferences("ofertasCreadas", MODE_PRIVATE);
            SharedPreferences.Editor editor= sp.edit();
            cantOfertasCreadas= sp.getInt(getString(R.string.cant_ofertas_creadas), 0);
            cantOfertasCreadas++;
            editor.putInt(getString(R.string.cant_ofertas_creadas), cantOfertasCreadas);
            editor.commit();
        }
        super.onStop();
    }

    @Override
    public void onCrearOferta(Oferta oferta) {
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
    public void onHoraElegida(int hora, int min) {
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        fragmentCO.setHora(hora, min);

    }

    @Override
    public void onFechaElegida(int dia, int mes, int anio) {
        CrearOfertasFragment fragmentCO= (CrearOfertasFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        fragmentCO.setFecha(dia, mes, anio);
    }

    public void eliminarOferta(int codigo) {
        Map<String,String> mapeoJSON= new HashMap<>();
        mapeoJSON.put("funcion", "eliminarOferta");
        mapeoJSON.put("codigo", Integer.toString(codigo));
        eliminar= true;
        crearPOSTJson(mapeoJSON);
    }

    private void crearRequestAlServer(Oferta oferta) {
        Map<String,String> mapeoJSON= new HashMap<>();
        if(editar) {
            mapeoJSON.put("funcion", "actualizarOferta");
            mapeoJSON.put("idOferta", Integer.toString(oferta.getCodigo()));
        }
        else
            mapeoJSON.put("funcion","crearOferta");
        Calendar fechaElegida= oferta.getFecha();
        fechaElegida.set(Calendar.SECOND,0);
        SimpleDateFormat formatoFecha= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String fecha= formatoFecha.format(fechaElegida.getTime());

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
            if(!fallo) {
                if(!editar) //Cree una oferta
                    crearOferta= true;
                else if(eliminar) //Elimine una oferta
                    cantOfertasCreadas--;
                finish();
            }
            else
                Toast.makeText(this,"Se produjo un error",Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
