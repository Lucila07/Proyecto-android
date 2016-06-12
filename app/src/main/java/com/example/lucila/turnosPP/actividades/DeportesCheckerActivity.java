package com.example.lucila.turnosPP.actividades;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.VolleySingleton;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.turnosPP.fragmentos.DeportesCheckerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeportesCheckerActivity
        extends AppCompatActivity
        implements DeportesCheckerFragment.OnGuardarCambiosListener{

    //Mantiene la informacion de los checkers al momento de creacion de la actividad
    //Es usado para crear el fragmento con los cheques y para comparar los
    //deportes que agrega o quita el usuario
    private Map<String, Boolean> deportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deportes_checker);

        //Recupero el mapeo
        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey("mapeo"))
                //Ya fue creada la actividad
                deportes = (Map<String, Boolean>) savedInstanceState.getSerializable("mapeo");
            else
                deportes= (Map<String,Boolean>) getIntent().getSerializableExtra("mapeoDeportes");
        } else
            //O fue llamada desde un intent
            deportes= (Map<String,Boolean>) getIntent().getSerializableExtra("mapeoDeportes");

        //Creao el fragmento de los checkers
        Fragment f= DeportesCheckerFragment.newInstance(new HashMap<>(deportes));
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_deportes_checker,f);
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("mapeo", (Serializable) deportes);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onGuardarCambios(Map<String, Boolean> deportesActualizados)  {
        //Analizo que deportes hizo un check y cuales quito
        List<List<String>> deportes= analizarCambios(deportesActualizados);
        //Actualizo el mapeo por si se vuelve a entrar en la actividad
        this.deportes= deportesActualizados;

        Intent resultado= new Intent();
        resultado.putExtra("deportesNuevos",(Serializable) deportes.get(0));
        resultado.putExtra("deportesEliminar",(Serializable) deportes.get(1));
        resultado.putExtra("mapeo", (Serializable) deportesActualizados);
        setResult(RESULT_OK, resultado);
        finish();
    }

    //Analiza los cambios realizados por el usuario y crea 2 listas para saber que deportes
    //hay que actualizar en la base y cuales hay que eliminar
    private List<List<String>> analizarCambios(Map<String, Boolean> deportesAct) {
        List<List<String>> toR= new ArrayList<List<String>>();
        toR.add(new ArrayList<String>());//Lista de deportes nuevos
        toR.add(new ArrayList<String>());//Lista de deportes a eliminar

        //Checkeo todos los cambios
        for(String deporte : deportes.keySet()) {
            Boolean b1, b2;
            b1= deportes.get(deporte);
            b2= deportesAct.get(deporte);
            if(!b1.booleanValue() && b2.booleanValue()) {
                //Antes no lo tenia, pero ahora lo agregó
                toR.get(0).add(deporte);
            }
            else if(b1.booleanValue() && !b2.booleanValue()) {
                //Antes lo tenia pero lo sacó
                toR.get(1).add(deporte);
            }
            //Si ya lo tiene y no lo saco, o si no lo tiene y no lo puso no tengo que hacer nada
        }
        return toR;
    }
}
