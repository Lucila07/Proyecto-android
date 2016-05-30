package com.example.lucila.turnosPP.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.lucila.turnosPP.beans.VolleySingleton;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.myapplication.R;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity
        extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
                    View.OnClickListener {

    private static final String TAG= OfertasActivity.class.getSimpleName();
    private static final int RC_SIGN_IN= 1;

    private EditText user, pass;
    private CheckBox recordarmeChk;
    private TextView msjError;

    private boolean validacion;
    private boolean recordarme;

    private SharedPreferences preferences;
    private GoogleApiClient clienteAPI;

    //region CICLO_VIDA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        preferences= getPreferences(MODE_PRIVATE);
        recordarme= preferences.getBoolean("recordarme", false);
        validacion= preferences.getBoolean("validacion", false);

        //Si el usuario ya validó antes y seteo "recordarme"
        if(validacion && recordarme) {
            //TODO sqllite traer el establecimiento
            iniciciarMenuPPal(new Establecimiento());
        }
        else {
            //Obtengo componentes a utilizar en esta aplicación
            user= (EditText) findViewById(R.id.edit_text_usuario);
            user.setText(preferences.getString("user", "")); //El segundo parametro es por si no existe tal preferencia guardada.
            pass= (EditText) findViewById(R.id.edit_text_contra);
            recordarmeChk= (CheckBox) findViewById(R.id.checkbox_recordame);
            recordarmeChk.setChecked(recordarme);
            msjError= (TextView) findViewById(R.id.edit_text_error);

            //Google Sign-in
            //Con el idToken ya se obtiene toda la info del user.
            GoogleSignInOptions opcionesSignIn = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.server_id)).requestEmail().requestProfile()
                    .build();

            //Google API
            clienteAPI = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, opcionesSignIn)
                    .build();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(clienteAPI);
            startActivityForResult(signInIntent, RC_SIGN_IN);

            findViewById(R.id.sign_in_button).setOnClickListener(this);
        }
    }

    public void onPause() {
        super.onPause();
        //Si el usuario quiere ser recordado
        /*
        if(recordarmeChk.isChecked() && validacion) {
            //Almaceno su informacion en el celular
            //TODO SQLLite con info del user y checkbox.
        }*/
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
        Intent registrarse= new Intent(this,RegistrarseActivity.class);
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
        map.put("token", token);
        //Creo el objeto json con el token que me da google
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

    /**
     * Usado cuando el usario usa nuestro servicio de log-in
     */
    private void validar(String usuario, String contra) {
        //TODO encriptar paramentro 'contra'
        String urlLogin= Constantes.LOGIN +
                "?user=" + usuario +
                "&pass=" + contra;

        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        urlLogin,
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
                Gson gson = new Gson();

                //Obtengo el usuario dentro del json
                JSONObject json = response.getJSONObject("usuario");

                //Traduzco
                Establecimiento user = gson.fromJson(json.toString(), Establecimiento.class);
                validacion = true;

                //Inicio el menu principal
                iniciciarMenuPPal(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
    * Dispara el intent del menu principal con el establecimiento pasado por parámetro
    */
    private void iniciciarMenuPPal(Establecimiento e) {
        Intent menuPPal = new Intent(this, MenuPrincipalActivity.class);
        menuPPal.putExtra("establecimiento", e);
        startActivity(menuPPal);
    }
    //endregion
}
