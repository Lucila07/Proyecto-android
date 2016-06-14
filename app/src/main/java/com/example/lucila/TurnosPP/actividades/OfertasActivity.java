package com.example.lucila.turnosPP.actividades;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.turnosPP.fragmentos.OfertasFragment;
import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;

public class OfertasActivity extends AppCompatActivity implements OfertasFragment.OnListFragmentInteractionListener {

    private static final String TAG= OfertasActivity.class.getSimpleName();
    private static final String GSON= "gson";

    private Gson gson;
    private Oferta[] ofertas;
    private String[] deportes;
    private int idEstablecimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);

        idEstablecimiento= getIntent().getIntExtra("id", 0);
        deportes= (String[]) getIntent().getSerializableExtra("Tdeportes");
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        cargarAdaptador();
    }

    /**
     * Carga el adaptador con las metas obtenidas
     * en la respuesta
     */
    public void cargarAdaptador() {
        // Petición GET
        VolleySingleton.getInstance(this).
                        addToRequestQueue(
                            new JsonObjectRequest(
                                Request.Method.GET,
                                Constantes.GET_OFERTAS_ESTABLECIMIENTO + "&idEstablecimiento="+ idEstablecimiento,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuesta(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.getMessage());
                                    }
                                }

                        )
                );
    }
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");
            switch (estado) {
                case "1": {
                    // EXITO
                    // Obtener array Json
                    JSONArray mensaje = response.getJSONArray("ofertas");
                    // Parsear con Gson
                    Type collectionType = new TypeToken<Collection<Oferta>>(){}.getType();
                    Collection<Oferta> enums = gson.fromJson(mensaje.toString(), collectionType);
                    ofertas = enums.toArray(new Oferta[0]);
                    for(Oferta o : ofertas) {
                        o.setNombreDeporte(deportes[o.getIdDeporte()-1]);
                    }
                    settearFragmentListaOfertas();
                    break;
                    }
                case "2": {
                    // El Usuario no tiene ofertas creadas
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void settearFragmentListaOfertas() {
        Fragment listaOfertasFragment= OfertasFragment.newInstance(ofertas);
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ver_ofertas_container, listaOfertasFragment);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Oferta item) {

    }
}
