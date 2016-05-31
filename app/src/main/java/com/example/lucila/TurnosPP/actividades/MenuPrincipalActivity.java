package com.example.lucila.turnosPP.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Establecimiento;
import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.fragmentos.CrearOfertasFragment;
import com.example.lucila.turnosPP.fragmentos.EstablecerUbicacionFragment;
import com.example.lucila.turnosPP.fragmentos.MenuPrincipalFragment;
import com.example.lucila.turnosPP.fragmentos.OfertasFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MenuPrincipalActivity
        extends ToolbarActivity
        implements  MenuPrincipalFragment.OnBotonMenuClickListener,
                    CrearOfertasFragment.OnCrearOfertaListener,
                    EstablecerUbicacionFragment.OnEstablecerUbicacionListener,
                    GoogleApiClient.OnConnectionFailedListener {

    private Map<Integer, Class> mapBotonClase;
    private Map<Integer, Fragment> mapBotonFrag;

    private boolean esTablet;
    private boolean enPortaretrato;
    private TextView textoRecord;
    private Establecimiento establecimiento;

    private GoogleApiClient clienteAPI;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);


        if(savedInstanceState != null) {
            //Viene de otra actividad
            establecimiento = (Establecimiento) savedInstanceState.getSerializable("establecimiento");
        } else {
            //Viene de la actividad Login
            establecimiento = (Establecimiento) getIntent().getSerializableExtra("establecimiento");
        }
        textoRecord= (TextView) findViewById(R.id.textView_info_menu_ppal);
        setearRecordatorio();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mapBotonClase= new HashMap<Integer, Class>(7);
        mapBotonClase.put(R.id.boton_crear_oferta, CrearOfertasActivity.class);
        mapBotonClase.put(R.id.boton_comprar_ofertas, LoginActivity.class);
        mapBotonClase.put(R.id.boton_denunciar_usuario, LoginActivity.class);
        mapBotonClase.put(R.id.boton_establecer_ubicacion, EstablecerUbicacionActivity.class);
        mapBotonClase.put(R.id.boton_ver_creadas, OfertasActivity.class);

        esTablet= getResources().getBoolean(R.bool.esTablet);
        enPortaretrato= getResources().getBoolean(R.bool.enPortaretratos);

        if(esTablet) {
            mapBotonFrag = new HashMap<Integer, Fragment>(7);
            mapBotonFrag.put(R.id.boton_crear_oferta, new CrearOfertasFragment());
            mapBotonFrag.put(R.id.boton_comprar_ofertas, new CrearOfertasFragment());
            mapBotonFrag.put(R.id.boton_denunciar_usuario, new CrearOfertasFragment());
            mapBotonFrag.put(R.id.boton_establecer_ubicacion, new EstablecerUbicacionFragment());
            mapBotonFrag.put(R.id.boton_ver_creadas, new OfertasFragment());
        }

        //Google Sign-in
        //Para desconectarse de la app hay que conectarse a la api de google
        GoogleSignInOptions opcionesSignIn = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_id)).requestEmail().requestProfile()
                .build();

        //Google API
        clienteAPI = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, opcionesSignIn)
                .build();

        //TODO ver el tema del TextView con la cantidad de pack restantes

    }

    @Override
    public void onStart() {
        super.onStart();
        clienteAPI.connect();
        //Actualizo la cantidad de ofertas
        preferences= getPreferences(MODE_PRIVATE);
        int ofCreadas= preferences.getInt(getString(R.string.cant_ofertas_creadas),0);
        TextView cantOfertas= (TextView) findViewById(R.id.textview_n_ofertas_restantes);
        cantOfertas.setText(Integer.toString(establecimiento.getCantMaxOfertas() - ofCreadas));
    }

    @Override
    public void onStop() {
        clienteAPI.disconnect();
        super.onStop();
    }

    //Métodos onclick de los botones
    public void onClickBoton(View view) {
        int viewID= view.getId();

        if(!esTablet) {
            //Si no es una tablet creo el intent con una actividad.
            Class claseActivity = mapBotonClase.get(viewID);
            Intent sigActividad = new Intent(this, claseActivity);
            startActivity(sigActividad);
        }
        else {
            if(enPortaretrato) {
                //TODO tablet en portaretrato
            }
            //Cambio el fragment en el contenedor.
            FragmentTransaction fragTrans= getSupportFragmentManager().beginTransaction();
            fragTrans.replace(R.id.panel_info_menu_principal, mapBotonFrag.get(viewID));
            fragTrans.commit();
        }
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

    /*
    * Callbacks de los distintos fragments
    */
    /* CrearOfertaFragment */
    @Override
    public void onCrearOferta(Oferta oferta) {

    }

    @Override
    public void mostrarDialogoHora() {

    }

    @Override
    public void mostrarDialogoFecha() {

    }

    /* EstablecerUbicacionFragment */
    @Override
    public void onObtenerUbicacionListener() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Servicio de google caido", Toast.LENGTH_SHORT);
    }

    //region PRIVADOS
    private void setearRecordatorio() {
        //TODO deportes
        boolean seteoTelefono= establecimiento.getTelefono() != 0;
        boolean seteoUbicacion= !establecimiento.getUbicacion().isEmpty();
        if((!seteoTelefono) || (!seteoUbicacion))
            textoRecord.setVisibility(View.VISIBLE);
        else
            textoRecord.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void logout() {
        if(clienteAPI.isConnected()) {
            Auth.GoogleSignInApi.signOut(clienteAPI).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            mostrarLogin();
                        }
                    });
        }
    }

    private void mostrarLogin() {
        //TODO leer backstack por las dudas
        Intent loginAct= new Intent(this, LoginActivity.class);
        startActivity(loginAct);
    }

    @Override
    protected void perfil() {

    }

    @Override
    protected void opciones() {

    }
    //endregion
}
