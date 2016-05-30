package com.example.lucila.myapplication.Datos;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.MainActivity;
import com.example.lucila.myapplication.http.ConstantesAcceso;
import com.example.lucila.myapplication.http.VolleySingleton;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.json.JSONObject;
/**
 * Created by tino on 25/05/2016.
 */
public class ServicioUsuariosHttp implements  ServicioUsuarios{

    private static AccesoUsuarios accesoUsuarios;

    private static Usuario usuarioLogueado;

    private static Activity activity;

    private static  ServicioUsuariosHttp instancia;

    private  ServicioUsuariosHttp(AccesoUsuarios a, Activity activity){
       this.accesoUsuarios=a;
        this.activity=activity;
    }

    public  static ServicioUsuariosHttp getInstance(AccesoUsuarios acceso, Activity activity){
        if(instancia==null)
            instancia= new ServicioUsuariosHttp(acceso,activity);
        else {

            instancia.accesoUsuarios=acceso;
            instancia.activity=activity;
        }
        return instancia;
    }

    @Override
    public  Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    @Override
    public  void verificarExistencia(Usuario user)
    {
        usuarioLogueado=user;
        peticionExistenciaUsuario();


    }


    @Override
    public void crearUsuario(String nombreUsuario,String mail,String id,String telefono){

        HashMap<String,String>mapa= new HashMap<>();
        mapa.put("idUser",id);
        mapa.put("nombreApellido",nombreUsuario);
        mapa.put("telefono",telefono);
        mapa.put("mail",mail);

        JSONObject json= new JSONObject(mapa);
        Log.d("nuevo usuario","json a mandar "+json.toString());
        peticionCrearUsuario(json);

    }

    private static void peticionCrearUsuario( JSONObject json){

        String url = ConstantesAcceso.getURL("guardar_usuario",null);
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("nuevo usuario", "se envio el requerimiento");
                                procesarRespuestaCrearUsuario(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }));
    }

    private static void procesarRespuestaCrearUsuario(JSONObject response){

        try {
            String estado = response.getString("estado");
            switch (estado) {
                case "1": // EXITO
                    Log.d("nuevo usuario", "se creocorrectamente estado"+estado);
                    accesoUsuarios.cargarMain();
                    break;
                case "2": // FALLIDO
                    accesoUsuarios.cargarTelefono();
                    break;
            }
        }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }



    private static void peticionExistenciaUsuario( ){

        String url= ConstantesAcceso.getURL("verificar_usuario",usuarioLogueado.getIdUsuario());
        Log.d("verificar usuario","url "+url);
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta Json
                                String respuesta=response.toString();

                                procesarRespuestaVerificar(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(activity.getClass().getSimpleName(), "Error Volley al verificar usuario: " + error.getMessage());
                            }
                        }

                )
        );
    }
    /**
     * si el usuario existe entonces:
     *  y carga la pantalla principal
     * */
    private static void procesarRespuestaVerificar(JSONObject response){


        try {
            String existe= response.getString("existe");
            Log.d("existe usuario ", "json "+response.toString());
            switch (existe) {
                case "true": // EXITO
                    String telefono= response.getString("telefono");
                    usuarioLogueado.setTelefono(telefono);
                    accesoUsuarios.cargarMain();
                    break;
                case "false": // FALLIDO

                    accesoUsuarios.cargarTelefono();
                    break;
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }



/**
 * interfaz que representa los callback de login
 * */
public interface  AccesoUsuarios{

    /**
     * caso que el usuario existe en la bd
     * representa el chequeo asyncrono con el servidor
     * the user.
     */
    public void cargarMain();
    /**
     * caso que el usuario NO existe en la bd
     * entonces carga la pantalla para que el usuario ingrese el telefono
     * */
    public void cargarTelefono();


}
}
