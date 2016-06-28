package com.example.lucila.turnosPP.actividades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ToolbarActivity extends AppCompatActivity {

    protected ResponseReceiver receiver;
    protected Gson gson;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    protected void onResume() {
        super.onResume();
        if(receiver == null)
            receiver= new ResponseReceiver();
        IntentFilter filtro= new IntentFilter(Constantes.SERVICIO_VOLLEY);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filtro);
    }

    protected void onPause() {
        if(receiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.menu_perfil:
                perfil();
                return true;
            case R.id.menu_settings:
                opciones();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //region TOOLBAR_OPTION
    protected abstract void logout();

    protected abstract void perfil();

    protected abstract void opciones();
    //endregion

    //region SERVICIO_VOLLEY
    private class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonString= intent.getStringExtra(Constantes.JSON_RESPUESTA);
            JSONObject respuesta= null;
            try {
                respuesta = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            procesarRespuesta(respuesta);
        }
    }

    protected void procesarRespuesta(JSONObject respuesta) {
        try {
            // Obtener atributo "estado"
            String estado = respuesta.getString("estado");
            switch (estado) {
                case "1": {
                    procesarRespuestaExitosa(respuesta);
                    break;
                }
                case "2": {

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected abstract void procesarRespuestaExitosa(JSONObject respuesta);
    //endregion
}
