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
import com.example.lucila.turnosPP.beans.Pack;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
    private Map<String, Boolean> mapDeportes;

    private boolean esTablet;
    private boolean enPortaretrato;
    private TextView textoRecord;
    private Establecimiento establecimiento;
    private String[] deportes;
    private Oferta[] ofertas;
    private int cantidadOfertas;
    private Collection<Pack> packs;

    private GoogleApiClient clienteAPI;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        mapDeportes= new HashMap<>();

        if(savedInstanceState != null) {
            //Viene de otra actividad
            establecimiento = (Establecimiento) savedInstanceState.getSerializable("establecimiento");
            deportes= savedInstanceState.getStringArray("Tdeportes");
            ofertas= (Oferta[]) savedInstanceState.getSerializable("ofertas");
            mapDeportes= (Map<String, Boolean>) savedInstanceState.getSerializable("mapeoDeportes");
        } else {
            //Viene de la actividad Login
            establecimiento = (Establecimiento) getIntent().getSerializableExtra("establecimiento");
            deportes= (String[]) getIntent().getSerializableExtra("Tdeportes");
            ofertas= (Oferta[]) getIntent().getSerializableExtra("ofertas");
            for(String d : deportes) {
                boolean tiene= establecimiento.getDeportes().contains(d);
                mapDeportes.put(d, tiene);
            }
            packs= (Collection<Pack>) getIntent().getSerializableExtra("packs");
        }
        textoRecord= (TextView) findViewById(R.id.textView_info_menu_ppal);
        setearRecordatorio();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mapBotonClase= new HashMap<Integer, Class>(7);
        mapBotonClase.put(R.id.boton_crear_oferta, CrearOfertasActivity.class);
        mapBotonClase.put(R.id.boton_comprar_ofertas, ComprarPackActivity.class);
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
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("establecimiento", establecimiento);
        savedInstanceState.putStringArray("Tdeportes", deportes);
        savedInstanceState.putSerializable("ofertas", ofertas);
        savedInstanceState.putSerializable("mapeoDeportes", (Serializable) mapDeportes);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        clienteAPI.connect();
    }

    public void onResume() {
        super.onResume();
        setOfertasRestantes();
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
            boolean esCrearOferta= claseActivity == CrearOfertasActivity.class;
            boolean puedeCrearOferta= cantidadOfertas < establecimiento.getCantMaxOfertas();
            if(!esCrearOferta || (esCrearOferta && puedeCrearOferta)) {
                Intent sigActividad = new Intent(this, claseActivity);
                sigActividad.putExtra("id", establecimiento.getId());
                sigActividad.putExtra("Tdeportes", deportes);
                sigActividad.putExtra("deportesEst", establecimiento.getDeportes().toArray());
                sigActividad.putExtra("ofertas", ofertas);
                sigActividad.putExtra("packs_disponibles",(Serializable) packs);
                if(claseActivity == ComprarPackActivity.class)
                    startActivityForResult(sigActividad, 5);
                else
                    startActivity(sigActividad);

            } else {

            }
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

    public void eliminarOferta(int codigo) {
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
        boolean seteoTelefono= establecimiento.getTelefono() != 0;
        boolean seteoUbicacion= establecimiento.getUbicacion() != null;
        boolean seteoDeportes= (establecimiento.getDeportes() != null && establecimiento.getDeportes().size() != 0);
        if((!seteoTelefono) || (!seteoUbicacion) || (!seteoDeportes))
            textoRecord.setVisibility(View.VISIBLE);
        else
            textoRecord.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void logout() {
        if(clienteAPI.isConnected()) {
            Auth.GoogleSignInApi.revokeAccess(clienteAPI).setResultCallback(
                    new ResultCallback<Status>() {
                        public void onResult(Status status) {
                            finish();
                        }
                    });
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
        Intent i= new Intent(this, DeportesCheckerActivity.class);
        i.putExtra("mapeoDeportes",  (Serializable) mapDeportes);
        i.putExtra("id", establecimiento.getId());
        startActivity(i);
    }

    @Override
    protected void opciones() {

    }
    //endregion

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(clienteAPI).setResultCallback(
                new ResultCallback<Status>() {
                    public void onResult(Status status) {
                        finish();
                    }
                });
    }

    private void setOfertasRestantes() {
        TextView cantOfertas= (TextView) findViewById(R.id.textview_n_ofertas_restantes);
        cantOfertas.setText(Integer.toString(establecimiento.getCantMaxOfertas() - ofertas.length));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Si el resultado es correcto
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                //Llame por los packs
                case 5: {
                    Pack packElegido= (Pack) data.getSerializableExtra("pack_elegido");
                    establecimiento.setCantMaxOfertas(packElegido.getCantidadOfertas());
                    setOfertasRestantes();
                }
            }
        }
    }
}
