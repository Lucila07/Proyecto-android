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
import com.example.lucila.myapplication.http.deporteDeserializer;
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

    private List<Deporte>deportes;

    private MapeoIdDeporte mapeo;
    /**
     * primero se deben obtener los deportes, para luego hacer el mapeo de id deporte a deporte en las ofertas
     * */
    public  ServicioOfertasHttp(Activity activity)
    {
        this.activity=activity;

        mapeo= new MapeoIdDeporte();
        ofertas= new ArrayList<Oferta>();
        deportes= new ArrayList<Deporte>();
        //obetenemos los deportes del servidor y los guardamos en la lista deportes
        realizarPeticionDeportes("get_deportes");
        //obtenermos las ofertas del servidor y las guardamos en la lista ofertas
        realizarPeticionOfertas("get_ofertas", null);


    }
    @Override
    public boolean reservarOferta(Oferta oferta, Usuario user) {
        return false;
    }

    @Override
    public List<Oferta> getOfertas() {

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
    public List<Deporte> getDeportes()
    {

       return deportes;
    }

    @Override
    public int cantidadDeportes() {
        return 0;
    }

    @Override
    public Deporte getDeporte(String nombre) {

        for(int i=0;i<deportes.size();i++){
            if (deportes.get(i).getNombre().equals(nombre))
                return deportes.get(i);

        }
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

                                      procesarRespuestaOfertas(response);
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
    private  void procesarRespuestaOfertas(JSONObject response) {

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
                    gBuilder.registerTypeAdapter(Oferta.class,new ofertaDeserializer(mapeo));
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

    /**
     get representa la funcion que se quiere llamar cuya url se saca de constantes oferta
     param representa el parametro especifico para la funcion, en caso que se quieran todas las ofertas estara null.
     */
    private void realizarPeticionDeportes(String get){


        // Petición GET
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        ConstantesAcceso.getURL(get,null),
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta Json
                                String respuesta=response.toString();
                                //  Log.d(activity.getClass().getSimpleName(), "Recibido del serv: " + respuesta);
                            procesarRespuestaDeportes(response);

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
 * realizar las operaciones correspondientes a la obtencion de deportes
 *
 * @param response Objeto Json con la respuesta
 */
private  void  procesarRespuestaDeportes(JSONObject response) {
    Deporte[]deportesArray= null;
    try {


        String estado = response.getString("estado");

        switch (estado) {
            case "1": // EXITO
                // Obtener array "deportes" Json
                JSONArray mensaje = response.getJSONArray("deportes");
                String cadenaRecibida=mensaje.toString();

               // Log.d(activity.getClass().getSimpleName(), "Deportes " + cadenaRecibida);

                GsonBuilder gBuilder = new GsonBuilder();
                gBuilder.registerTypeAdapter(Deporte.class,new deporteDeserializer());
                gson = gBuilder.create();
                deportesArray= gson.fromJson(cadenaRecibida, Deporte[].class);


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

    for ( int i=0;i<deportesArray.length;i++)
          {
            Deporte d=  deportesArray[i];
            deportes.add(d);
            mapeo.insert(d.getIdDeporte(),d);  //establecemos el mapeo

          }


    }
}

