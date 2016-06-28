package com.example.lucila.turnosPP.actividades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.turnosPP.fragmentos.CrearOfertasFragment;
import com.example.lucila.turnosPP.fragmentos.OfertasFragment;
import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.VolleySingleton;
import com.example.lucila.turnosPP.servicios.VolleyRequestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;

public class OfertasActivity
        extends ToolbarActivity
        implements OfertasFragment.OnListFragmentInteractionListener {

    private Oferta[] ofertas;
    private String[] deportes;
    private int idEstablecimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);

        idEstablecimiento= getIntent().getIntExtra("id", 0);
        deportes= (String[]) getIntent().getSerializableExtra("Tdeportes");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        cargarAdaptador();
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

    @Override
    protected void onResume() {
        super.onResume();
        cargarAdaptador();
    }

    /**
     * Carga el adaptador con las metas obtenidas
     * en la respuesta
     */
    public void cargarAdaptador() {
        String url= Constantes.GET_OFERTAS_ESTABLECIMIENTO + "&idEstablecimiento="+ idEstablecimiento;
        VolleyRequestService.startActionGetRequest(this, url);
    }

    @Override
    protected void procesarRespuestaExitosa(JSONObject respuesta) {
        try {
            JSONArray mensaje = respuesta.getJSONArray("ofertas");
            Type collectionType = new TypeToken<Collection<Oferta>>(){}.getType();
            Collection<Oferta> enums = gson.fromJson(mensaje.toString(), collectionType);
            ofertas = enums.toArray(new Oferta[0]);
            for(Oferta o : ofertas) {
                o.setNombreDeporte(deportes[o.getIdDeporte()-1]);
            }
            settearFragmentListaOfertas();
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
        if(item.getEstado().equals("disponible")) {
            Intent i = new Intent(this, EditarOfertaActivity.class);
            i.putExtra(Constantes.ARREGLO_DEPORTES_ESTABLECIMIENTO, deportes);
            i.putExtra(Constantes.OFERTA_EDITAR, item);
            startActivity(i);
        }
        else Toast.makeText(this,"La oferta ya fue reservada", Toast.LENGTH_SHORT).show();
    }
}
