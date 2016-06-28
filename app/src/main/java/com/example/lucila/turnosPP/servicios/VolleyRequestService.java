package com.example.lucila.turnosPP.servicios;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lucila.turnosPP.beans.VolleySingleton;
import com.example.lucila.turnosPP.constantes.Constantes;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VolleyRequestService extends IntentService {

    private static final String ACTION_GET_REQUEST = "com.example.lucila.turnosPP.servicios.action.FOO";
    private static final String ACTION_POST_REQUEST = "com.example.lucila.turnosPP.servicios.action.BAZ";

    private static final String URL = "com.example.lucila.turnosPP.servicios.extra.URL";
    private static final String MAP = "com.example.lucila.turnosPP.servicios.extra.MAP";

    public VolleyRequestService() {
        super("VolleyRequestService");
    }

    /**
     * Crea un servicio para manejar el metodo Get a una URL usando Volley
     */
    public static void startActionGetRequest(Context context, String url) {
        Intent intent = new Intent(context, VolleyRequestService.class);
        intent.setAction(ACTION_GET_REQUEST);
        intent.putExtra(URL, url);
        context.startService(intent);
    }

    /**
     * Crea un servicio para manejar el metodo Pos a una URL usando Volley
     */
    public static void startActionPostRequest(Context context, String url, Map<String, String> map) {
        Intent intent = new Intent(context, VolleyRequestService.class);
        intent.setAction(ACTION_POST_REQUEST);
        intent.putExtra(URL, url);
        intent.putExtra(MAP, (Serializable) map);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String url = intent.getStringExtra(URL);
            if (ACTION_GET_REQUEST.equals(action)) {
                handleActionGetRequest(url);
            } else if (ACTION_POST_REQUEST.equals(action)) {
                final Map<String,String> map= (Map<String,String>) intent.getSerializableExtra(MAP);
                handleActionPostRequest(url, map);
            }
        }
    }

    private void handleActionGetRequest(String url) {
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
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
                                Log.d("Volley", "Error Volley: " + error.getMessage());
                            }
                        }
                )
        );
    }


    private void handleActionPostRequest(String url, Map<String, String> map) {
        JSONObject postJSON= new JSONObject(map);
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        url,
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

    private void procesarRespuesta(JSONObject response) {
        Intent intentLocal= new Intent(Constantes.SERVICIO_VOLLEY);
        intentLocal.putExtra(Constantes.JSON_RESPUESTA, response.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentLocal);
    }
}
