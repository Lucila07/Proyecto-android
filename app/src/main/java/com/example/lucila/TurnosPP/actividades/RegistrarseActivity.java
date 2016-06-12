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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrarseActivity
        extends AppCompatActivity
        implements InfoUsuarioFragment.OnFragmentInteractionListener {

    private static final String TAG= RegistrarseActivity.class.getSimpleName();

    private final static int COD_DEP_CHECKER= 1;
    private final static int COD_UBICACION= 2;

    private Establecimiento establecimiento;
    private Map<String, Boolean> mapeoDeportes; //Usado por la actividad DeportesChecker

    //Deportes ingresados por el usuario
    private List<String> deportesNuevos= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        establecimiento= new Establecimiento();
        //Creo el mapeo para las actividades que lo usan
        mapeoDeportes= new HashMap<>();
        String[] dep= (String[]) getIntent().getSerializableExtra("Tdeportes");
        for(String d : dep) {
            mapeoDeportes.put(d, false);
        }
        establecimiento= new Establecimiento();
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
                case COD_DEP_CHECKER: { //Llame por los deportes
                    deportesNuevos= (List<String>) data.getSerializableExtra("deportesNuevos");
                    Map<String,Boolean> m= (Map<String, Boolean>) data.getSerializableExtra("mapeo");
                    break;
                }
                case COD_UBICACION: {
                    establecimiento.setUbicacion(data.getStringExtra("ubicacion"));
                }
            }
        }
    }

    private void crearUser(String pass) {
        Map<String, String> mapa= new HashMap<>();
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
        JSONObject jsonObject= new JSONObject(mapa);
        JSONArray array1 = new JSONArray(deportesNuevos);
        try {
            jsonObject= jsonObject.put("deportesNuevos", array1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, jsonObject.toString());
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.UPDATE,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mensajeError(error.getMessage());
                            }
                        }
                ));
    }

    private void actualizarUser() {

    }

    private void procesarRespuesta(JSONObject response) {
        try {
            int resultado= response.getInt("estado");
            if(resultado == 1) {
                finish();
            }
            else {
                mensajeError("Hubo un error al procesar los datos");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mensajeError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
