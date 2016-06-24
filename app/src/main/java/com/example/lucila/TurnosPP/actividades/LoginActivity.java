package com.example.lucila.turnosPP.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lucila.turnosPP.beans.Establecimiento;
import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.beans.Pack;
import com.example.lucila.turnosPP.beans.VolleySingleton;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.myapplication.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity
        extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
                    View.OnClickListener {

    private static final String TAG= LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN= 1;
    private EditText user, pass;
    private CheckBox recordarmeChk;
    private TextView msjError;

    private boolean validacion;
    private boolean recordarme;
    private String[] Tdeportes;
    private Collection<Pack> packs;

    private SharedPreferences preferences;
    private GoogleApiClient clienteAPI;

    //region CICLO_VIDA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        preferences = getPreferences(MODE_PRIVATE);
        recordarme = preferences.getBoolean("recordarme", false);
        validacion = preferences.getBoolean("validacion", false);

        //Si el usuario ya validó antes y seteo "recordarme"
        if (validacion && recordarme) {
            //TODO sqllite traer el establecimiento
            // iniciciarMenuPPal(new Establecimiento(), null);
        } else {
            //Obtengo componentes a utilizar en esta aplicación
            user = (EditText) findViewById(R.id.edit_text_usuario);
            user.setText(preferences.getString("user", "")); //El segundo parametro es por si no existe tal preferencia guardada.
            pass = (EditText) findViewById(R.id.edit_text_contra);
            recordarmeChk = (CheckBox) findViewById(R.id.checkbox_recordame);
            recordarmeChk.setChecked(recordarme);
            msjError = (TextView) findViewById(R.id.edit_text_error);

            //Google Sign-in
            //Con el idToken ya se obtiene toda la info del user.
            GoogleSignInOptions opcionesSignIn = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.server_id)).requestEmail().requestProfile()
                    .build();

            //Google API
            clienteAPI = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, opcionesSignIn)
                    .addApi(AppIndex.API).build();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(clienteAPI);
            startActivityForResult(signInIntent, RC_SIGN_IN);

            findViewById(R.id.sign_in_button).setOnClickListener(this);

            traerDeportes();
            traerPacks();
        }
    }

    public void onPause() {
        super.onPause();
        if(recordarmeChk != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("recordarme",recordarmeChk.isChecked());
            editor.putBoolean("validacion",validacion);
            editor.putString("user", user.getText().toString());
            editor.commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        clienteAPI.connect();
    }

    @Override
    public void onStop() {
        clienteAPI.disconnect();
        super.onStop();
    }
     //endregion

    //region ON_CLICK
    public void login(View view) {
        //TODO Server verification
        String usuario, contra;
        usuario= user.getText().toString();
        contra= pass.getText().toString();
        if(usuario.isEmpty() || contra.isEmpty()) //Sanitización de entrada
            msjError.setVisibility(EditText.VISIBLE);
        else
            validar(usuario, contra);
    }

    public void registrarse(View view) {
        Intent registrarse= new Intent(this, RegistrarseActivity.class);
        registrarse.putExtra("Tdeportes",Tdeportes);
        startActivity(registrarse);
    }



    @Override
    public void onClick(View v) {
        //boton google
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(clienteAPI);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //endregion

    //region CALLBACKS
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "fallo la conexion con google", Toast.LENGTH_SHORT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    //endregion

    //region PRIVADOS
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String idToken= acct.getIdToken();
            if(esTokenValido(idToken))
                mandarTokenAlServer(idToken);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private boolean esTokenValido(String token) {
        //TODO validar token con google
        boolean toR= true;
        return toR;
    }

    private void mandarTokenAlServer(String token) {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("funcion", "login");
        map.put("token", token);
        crearPOSTJson(map);
    }

    /**
     * Usado cuando el usario usa nuestro servicio de log-in
     */
    private void validar(String usuario, String contra) {
        //TODO encriptar paramentro 'contra'
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("funcion", "login");
        map.put("user", usuario);
        map.put("pass", contra);
        crearPOSTJson(map);
    }

    /*
    * Prosesa los distintos json en la respuestas obtenidas del server
    */
    private void procesarRespuesta(JSONObject response) {
        try {
            int fallo= response.getInt("estado");
            validacion= fallo == 1;
            if (!validacion) {
                //Si falla la validación, muestro el mensaje de error
                msjError.setVisibility(EditText.VISIBLE);
                pass.setText("");
            } else {
                //El user se loguea correctamente
                msjError.setVisibility(EditText.INVISIBLE);

                //Creo el traductor de json
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                //Obtengo el usuario dentro del json
                JSONObject jsonUser = response.getJSONObject("usuario");
                JSONObject jsonEstablecimiento= jsonUser.getJSONObject("user");
                JSONArray jsonDeportes= jsonUser.getJSONArray("deportes");
                int cantOfertasMax= jsonUser.getInt("cantPack");
                //Traduzco
                Establecimiento user = gson.fromJson(jsonEstablecimiento.toString(), Establecimiento.class);
                String[] deportes= gson.fromJson(jsonDeportes.toString(), String[].class);
                user.setCantMaxOfertas(cantOfertasMax);
                user.setDeportes(new ArrayList<String>(Arrays.asList(deportes)));
                Type collectionType = new TypeToken<Collection<Oferta>>(){}.getType();
                Collection<Oferta> enums = gson.fromJson(jsonUser.getJSONArray("ofertas").toString(), collectionType);
                Oferta[] ofertas = enums.toArray(new Oferta[0]);
                for(Oferta o : ofertas) {
                    o.setNombreDeporte(Tdeportes[o.getIdDeporte()-1]);
                }

                validacion = true;

                //Inicio el menu principal
                iniciciarMenuPPal(user, ofertas);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * Dispara el intent del menu principal con el establecimiento pasado por parámetro
    */
    private void iniciciarMenuPPal(Establecimiento e, Oferta[] ofertas) {
        Intent menuPPal = new Intent(this, MenuPrincipalActivity.class);
        menuPPal.putExtra("establecimiento", e);
        menuPPal.putExtra("Tdeportes", Tdeportes);
        menuPPal.putExtra("ofertas",ofertas);
        menuPPal.putExtra("packs", (Serializable) packs);
        startActivity(menuPPal);
    }

    private void traerDeportes() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("funcion", "traerDeportes");
        JSONObject postJSON= new JSONObject(map);
        //Lo envio al server
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.UPDATE,
                        postJSON,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                settearDeportes(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
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

    private void traerPacks() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("funcion", "traerPacks");
        JSONObject postJSON= new JSONObject(map);
        //Lo envio al server
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.UPDATE,
                        postJSON,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                settearPacks(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
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

    private void settearDeportes(JSONObject response) {
        try {
            JSONArray jsonTDeportes= response.getJSONArray("todosD");
            Gson gson = new Gson();
            Tdeportes= gson.fromJson(jsonTDeportes.toString(), String[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void settearPacks(JSONObject response) {
        try {
            JSONArray jsonTPacks= response.getJSONArray("packs");
            Type collectionType = new TypeToken<Collection<Pack>>(){}.getType();
            Gson gson = new Gson();
            packs = gson.fromJson(jsonTPacks.toString(), collectionType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void crearPOSTJson(Map<String,String> map) {
        JSONObject postJSON= new JSONObject(map);
        Log.d("TAG", "json= "+postJSON.toString());
        //Lo envio al server
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.LOGIN,
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
                                Log.d(TAG, "Error Volley: " + error.getMessage());
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
    //endregion
}
