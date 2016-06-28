package com.example.lucila.turnosPP.actividades;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Establecimiento;
import com.example.lucila.turnosPP.beans.VolleySingleton;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.turnosPP.fragmentos.DeportesCheckerFragment;
import com.example.lucila.turnosPP.fragmentos.EstablecerUbicacionFragment;
import com.example.lucila.turnosPP.fragmentos.InfoUsuarioFragment;
import com.example.lucila.turnosPP.servicios.VolleyRequestService;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrarseActivity
        extends ToolbarActivity
        implements InfoUsuarioFragment.OnFragmentInteractionListener {

    private static final String TAG= RegistrarseActivity.class.getSimpleName();

    private final static int COD_DEP_CHECKER= 1;
    private final static int COD_UBICACION= 2;

    private Establecimiento establecimiento;
    private boolean editar;

    //Usado por la actividad DeportesChecker
    private Map<String, Boolean> mapeoDeportes;

    //Deportes ingresados por el usuario
    private List<String> deportesNuevos;
    private List<String> deportesQuitados;

    private InfoUsuarioFragment fragmentoInfoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse_activity);

        establecimiento= new Establecimiento();
        if(savedInstanceState != null) {
            mapeoDeportes= (Map<String, Boolean>) savedInstanceState.getSerializable("mapeo");
            deportesNuevos= (List<String>) savedInstanceState.getSerializable("depNuevos");
            deportesQuitados= (List<String>) savedInstanceState.getSerializable("depEliminados");
            establecimiento= (Establecimiento) savedInstanceState.getSerializable("establecimiento");
            editar= savedInstanceState.getBoolean("editar");
        } else {
            //Creo el mapeo para las actividades que lo usan
            mapeoDeportes= new HashMap<>();
            String[] dep= (String[]) getIntent().getSerializableExtra("Tdeportes");
            editar= getIntent().getBooleanExtra("editar", false);
            if(editar)
                establecimiento = (Establecimiento) getIntent().getSerializableExtra("establecimiento");
            if(dep != null)
                for(String d : dep)
                    mapeoDeportes.put(d, false);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if(myToolbar != null) {
            myToolbar.getMenu().clear();
            if(editar)
                myToolbar.setTitle(R.string.title_editar_perfil);
        }
        setSupportActionBar(myToolbar);


        fragmentoInfoUsuario= InfoUsuarioFragment.newInstance(establecimiento);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.panel_fragment_registrarAct,
                        fragmentoInfoUsuario)
                .commit();
    }

    @Override
    protected void logout() {}

    @Override
    protected void perfil() {}

    @Override
    protected void opciones() {}

    @Override
    public void onSaveInstanceState(Bundle savedInstaceState) {
        savedInstaceState.putSerializable("mapeo",(Serializable) mapeoDeportes);
        savedInstaceState.putBoolean("editar", editar);
        savedInstaceState.putSerializable("depNuevos", (Serializable) deportesNuevos);
        savedInstaceState.putSerializable("depEliminados", (Serializable) deportesQuitados);
        savedInstaceState.putSerializable("establecimiento", (Serializable) establecimiento);
        super.onSaveInstanceState(savedInstaceState);
    }

    @Override
    public void onGuardarCambios(Establecimiento establecimiento, String pass) {
        this.establecimiento= establecimiento;
        crearUser(pass);
    }

    @Override
    public void onEstablecerUbicacion() {
        Intent i = new Intent(this, EstablecerUbicacionActivity.class);
        i.putExtra("registro", true); //El user se esta registrando
        startActivityForResult(i, COD_UBICACION);
    }

    @Override
    public void onEstablecerDeportes() {
        Intent i= new Intent(this, DeportesCheckerActivity.class);
        i.putExtra("mapeoDeportes",(Serializable) mapeoDeportes);
        startActivityForResult(i, COD_DEP_CHECKER);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Si el resultado es correcto
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                //Llame por los deportes
                case COD_DEP_CHECKER: {
                    //Mapeo de los deportes elegidos
                    mapeoDeportes= (Map<String, Boolean>) data.getSerializableExtra("mapeo");

                    //Lista de las diferencias actualizadas
                    deportesNuevos = (List<String>) data.getSerializableExtra("deportesNuevos");
                    if(editar)
                            deportesQuitados= (List<String>) data.getSerializableExtra("deportesEliminar");

                    //Lista auxiliar para actualizar la UI
                    List<String> aux= new ArrayList<>(mapeoDeportes.keySet().size());
                    for (String dep : mapeoDeportes.keySet())
                        if (mapeoDeportes.get(dep))
                            aux.add(dep);
                    establecimiento.setDeportes((ArrayList<String>) aux);
                    fragmentoInfoUsuario.actualizarDeportesUsuario(aux);
                    break;
                }
                case COD_UBICACION: {
                    String ubicacion= data.getStringExtra("ubicacion");
                    establecimiento.setUbicacion(ubicacion);
                    fragmentoInfoUsuario.actualizarUbicacion(ubicacion);
                }
            }
        }
    }

    private void crearUser(String pass) {
        //Creo el mapeo para el web service
        Map<String, String> mapa= crearMapa(pass);
        JSONObject postJSON= new JSONObject(mapa);
        try {
            postJSON.put("deportesEliminar", new JSONArray(deportesQuitados));
            postJSON.put("deportesNuevos", new JSONArray(deportesNuevos));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.UPDATE,
                        postJSON,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                procesarRespuestaExitosa(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Volley", "Error Volley: " + error.getMessage());
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

    @Override
    protected void procesarRespuestaExitosa(JSONObject respuesta) {
        finish();
    }

    private Map<String, String> crearMapa(String pass) {
        Map<String, String> mapa= new HashMap<>();
        if(editar) {
            mapa.put("funcion", "actualizarUsuario");
            mapa.put("idUser",String.valueOf(establecimiento.getId()));
        }
        else
            mapa.put("funcion","crearUsuario");
        mapa.put("user", establecimiento.getNombre());
        mapa.put("email", establecimiento.getEmail());
        mapa.put("pass", pass);
        if(!establecimiento.getUbicacion().isEmpty()) {
            String[] u= establecimiento.getUbicacion().split(";");
            if(u.length == 2) {
                mapa.put("ubicacion", u[1]);
                mapa.put("direccion", u[0]);
            }
            else mapa.put("ubicacion", u[0]);
        }
        if(establecimiento.getTelefono() != 0)
            mapa.put("telefono", String.valueOf(establecimiento.getTelefono()));
        return mapa;
    }
}
