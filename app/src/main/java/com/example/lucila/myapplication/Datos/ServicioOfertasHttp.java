package com.example.lucila.myapplication.Datos;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.http.ConstantesAcceso;
import com.example.lucila.myapplication.http.VolleySingleton;
import com.example.lucila.myapplication.http.ofertaDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
//jason  para las consultas al web service
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by tino on 21/05/2016.
 */
public class ServicioOfertasHttp implements ServicioOfertasUsuario {

    private  Activity activity;

    private Gson gson;

    private List<Oferta>ofertas;

    final String FORMATO_FECHA = "yyyy-mm-dd";
    public  ServicioOfertasHttp(Activity activity)
    {
        this.activity=activity;
        ofertas=new ArrayList<Oferta>();

    }
    @Override
    public boolean reservarOferta(Oferta oferta, Usuario user) {
        return false;
    }

    @Override
    public List<Oferta> getOfertas() {
         realizarPeticionOfertas("get_ofertas", null);
        return ofertas;
    }

    @Override
    public List<Oferta> getOfertasUbicacion(String ubicacion) {
        return null;
    }

    @Override
    public List<Oferta> getOfertasDeporte(Deporte deporte) {
        return null;
    }

    @Override
    public Oferta getOfertaCodigo(Long codigo) {
        return null;
    }

    @Override
    public List<Deporte> getDeportes() {
        return null;
    }

    @Override
    public int cantidadDeportes() {
        return 0;
    }

    @Override
    public Deporte getDeporte(String nombre) {
        return null;
    }

    /**
    get representa la funcion que se quiere llamar cuya url se saca de constantes oferta
     param representa el parametro especifico para la funcion, en caso que se quieran todas las ofertas estara null.
     */
    private void realizarPeticionOfertas(String get, String param){


        // Petición GET
        VolleySingleton.getInstance(activity).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConstantesAcceso.getURL(get,param),
                                null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                       String respuesta=response.toString();
                                      //  Log.d(activity.getClass().getSimpleName(), "Recibido del serv: " + respuesta);
                                      procesarRespuesta(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(activity.getClass().getSimpleName(), "Error Volley: " + error.getMessage());
                                    }
                                }

                        )
                );

    }

    /**
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    private  void procesarRespuesta(JSONObject response) {

        try {

            // Obtener atributo "estado"
            String estado = response.getString("estado");
          //  Log.d(activity.getClass().getSimpleName(), "estado " + estado);
            switch (estado) {
                case "1": // EXITO
                    // Obtener array "ofertas" Json
                    JSONArray mensaje = response.getJSONArray("ofertas");
                    String cadenaRecibida=mensaje.toString();

                    GsonBuilder gBuilder = new GsonBuilder();
                    gBuilder.registerTypeAdapter(Oferta.class,new ofertaDeserializer());
                    gson = gBuilder.create();
                    Oferta[] ofertaArray= gson.fromJson(cadenaRecibida, Oferta[].class);

                    for(int i=0;i<ofertaArray.length;i++){

                        ofertas.add(ofertaArray[i]);

                    }
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");

                    Toast.makeText(
                            activity,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
