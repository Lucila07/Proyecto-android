package com.example.lucila.turnosPP.actividades;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Establecimiento;
import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.beans.Pack;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.turnosPP.fragmentos.CrearOfertasFragment;
import com.example.lucila.turnosPP.fragmentos.DatePickerFragment;
import com.example.lucila.turnosPP.fragmentos.MenuPrincipalFragment;
import com.example.lucila.turnosPP.fragmentos.OfertasFragment;
import com.example.lucila.turnosPP.fragmentos.PackFragment;
import com.example.lucila.turnosPP.fragmentos.TimePickerFragment;
import com.example.lucila.turnosPP.servicios.VolleyRequestService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MenuPrincipalActivity
        extends ToolbarActivity
        implements  MenuPrincipalFragment.OnBotonMenuClickListener,
                    CrearOfertasFragment.OnCrearOfertaListener,
                    DatePickerFragment.OnFechaElegidaListener,
                    TimePickerFragment.OnHoraElegidaListener,
                    GoogleApiClient.OnConnectionFailedListener,
                    OfertasFragment.OnListFragmentInteractionListener,
                    PackFragment.OnEleccionPackListener {

    public static final int COMPRAR_PACK_ACTIVITY = 5;
    public static final int ESTABLECER_UBICACION = 6;

    private Map<Integer, Class> mapBotonClase;
    private Map<String, Boolean> mapDeportes;

    private boolean esTablet;
    private boolean enPortaretrato;

    private Establecimiento establecimiento;
    private String[] deportes;
    private Oferta[] ofertas;
    private Collection<Pack> packs;
    private String fragmentTag;

    private GoogleApiClient clienteAPI;

    //region CICLO_VIDA_ACTIVIDAD
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
            packs= (Collection<Pack>) savedInstanceState.getSerializable("packs");
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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.panel_botones_menu_principal,
                        MenuPrincipalFragment.newInstance(establecimiento.getCantMaxOfertas()-ofertas.length),
                        "fragment_menu_ppal")
                .commit();
        if(esTablet && !enPortaretrato) {
            //Tengo que instanciar el fragment de los botones del menú
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.panel_info_menu_principal,
                            OfertasFragment.newInstance(ofertas))
                    .commit();
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("establecimiento", establecimiento);
        savedInstanceState.putStringArray("Tdeportes", deportes);
        savedInstanceState.putSerializable("ofertas", ofertas);
        savedInstanceState.putSerializable("mapeoDeportes", (Serializable) mapDeportes);
        savedInstanceState.putSerializable("packs", (Serializable) packs);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        clienteAPI.connect();
    }

    public void onResume() {
        super.onResume();
        //setOfertasRestantes();
    }

    @Override
    public void onStop() {
        clienteAPI.disconnect();
        super.onStop();
    }
    //endregion

    /*
    * Callbacks de los distintos fragments
    */
    //region MENU_PRINCIPAL_FRAGMENT_CALLBACK
    public void onClickBoton(View view) {
        int viewID= view.getId();
        if(!esTablet || enPortaretrato) {
            //Si no es una tablet creo el intent con una actividad.
            Class claseActivity = mapBotonClase.get(viewID);
            Intent sigActividad = new Intent(this, claseActivity);
            switch(viewID) {
                case R.id.boton_crear_oferta: {
                    boolean puedeCrearOferta= ofertas.length < establecimiento.getCantMaxOfertas();
                    if(puedeCrearOferta) {
                        sigActividad.putExtra("deportesEst", establecimiento.getDeportes().toArray(new String[0]));
                        sigActividad.putExtra("id", establecimiento.getId());
                        startActivity(sigActividad);
                    }
                    break;
                }
                case R.id.boton_comprar_ofertas: {
                    sigActividad.putExtra(Constantes.PACKS_DISPONIBLES,(Serializable) packs);
                    sigActividad.putExtra(Constantes.ID_PACK_COMPRADO, establecimiento.getIdPack());
                    startActivityForResult(sigActividad, COMPRAR_PACK_ACTIVITY);
                    break;
                }
                case R.id.boton_establecer_ubicacion: {
                    startActivity(sigActividad);
                    break;
                }
                case R.id.boton_ver_creadas: {
                    sigActividad.putExtra("id", establecimiento.getId());
                    sigActividad.putExtra("Tdeportes", deportes);
                    startActivity(sigActividad);
                    break;
                }
                case R.id.boton_denunciar_usuario: {
                    break;
                }
            }
        }
        else {
            //Cambio el fragment en el contenedor
            //Por defecto muestra las ofertas creadas
            Fragment fragNuevo= OfertasFragment.newInstance(ofertas);
            boolean comprarOfertas= false;
            switch (viewID) {
                case R.id.boton_ver_creadas: {
                    //ya cree el fragment por defecto
                    break;
                }
                case R.id.boton_comprar_ofertas: {
                    comprarOfertas= true;
                    fragmentTag= "comprar_pack_fragment";
                    break;
                }
                case R.id.boton_establecer_ubicacion: {
                    Intent sigActividad= new Intent(this, EstablecerUbicacionActivity.class);
                    startActivityForResult(sigActividad, ESTABLECER_UBICACION);
                    break;
                }
                case R.id.boton_crear_oferta: {
                    int size= establecimiento.getDeportes().size();
                    String[] deportesEst= establecimiento.getDeportes().toArray(new String[size]);
                    fragNuevo= CrearOfertasFragment.newInstance(deportesEst, false, null);
                    fragmentTag= Constantes.CREAR_OFERTA_FRAG;
                }
            }
            FragmentTransaction fragTrans= getSupportFragmentManager().beginTransaction();
            if(comprarOfertas) {
                int i= 0;
                for(Pack p: packs) {
                    if (p.getId() != 1) {
                        fragNuevo= PackFragment.newInstance(p, p.getId() == establecimiento.getIdPack());
                        if(i == 0) {
                            fragTrans= fragTrans.replace(R.id.panel_info_menu_principal,
                                    fragNuevo,
                                    String.valueOf(p.getId()));
                            i++;
                        }
                        else {
                            fragTrans= fragTrans.add(R.id.panel_info_menu_principal,
                                    fragNuevo,
                                    String.valueOf(p.getId()));
                        }
                    }
                }
            } else
                fragTrans= fragTrans.replace(R.id.panel_info_menu_principal, fragNuevo, fragmentTag);
            fragTrans= fragTrans.addToBackStack(null);
            fragTrans.commit();
        }
    }
    //endregion

    //region CREAR_OFERTA_FRAGMENT_CALLBACKS
    @Override
    public void onCrearOferta(Oferta oferta, boolean editar) {
        Map<String, String> mapeoJSON= new HashMap<>();

        //Funcion a realizar por el web service
        if(!editar)
            mapeoJSON.put("funcion","crearOferta");
        else {
            mapeoJSON.put("funcion","actualizarOferta");
            mapeoJSON.put("idOferta",String.valueOf(oferta.getCodigo()));
        }

        //Formateo la fecha para que se corresponda con la almacenada en la base
        Calendar fechaElegida= oferta.getFecha();
        fechaElegida.set(Calendar.SECOND,0);
        SimpleDateFormat formatoFecha= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String fecha= formatoFecha.format(fechaElegida.getTime());
        mapeoJSON.put("fecha", fecha);

        //Termino de crear el mapeo de claves-valor
        mapeoJSON.put("idEstablecimiento", Integer.toString(establecimiento.getId()));
        mapeoJSON.put("nombreDeporte", oferta.getNombreDeporte());
        mapeoJSON.put("precioHab", Float.toString(oferta.getPrecioHabitual()));
        mapeoJSON.put("precioAct", Float.toString(oferta.getPrecioFinal()));

        //Delego el requerimiento al IntentService
        VolleyRequestService.startActionPostRequest(this, Constantes.UPDATE, mapeoJSON);
    }

    @Override
    public void eliminarOferta(int codigo) {
        Map<String,String> mapeoJSON= new HashMap<>();
        mapeoJSON.put("funcion", "eliminarOferta");
        mapeoJSON.put("codigo", Integer.toString(codigo));
        VolleyRequestService.startActionPostRequest(this, Constantes.UPDATE, mapeoJSON);
    }

    @Override
    public void mostrarDialogoHora() {
        (new TimePickerFragment()).show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void mostrarDialogoFecha() {
        (new DatePickerFragment()).show(getSupportFragmentManager(), "datePicker");
    }
    //endregion

    //region DATE_PICKER_CALLBACK
    @Override
    public void onFechaElegida(int dia, int mes, int anio) {
        ((CrearOfertasFragment) getSupportFragmentManager().findFragmentByTag(Constantes.CREAR_OFERTA_FRAG))
                .setFecha(dia, mes, anio);
    }
    //endregion

    //region TIME_PICKER_CALLBACK
    @Override
    public void onHoraElegida(int hora, int min) {
        ((CrearOfertasFragment)getSupportFragmentManager().findFragmentByTag(Constantes.CREAR_OFERTA_FRAG))
                .setHora(hora, min);
    }
    //endregion

    //region OFERTAS_FRAGMENT_CALLBACK
    @Override
    public void onListFragmentInteraction(Oferta item) {
        fragmentTag= Constantes.CREAR_OFERTA_FRAG;
        int size= establecimiento.getDeportes().size();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.panel_info_menu_principal,
                        CrearOfertasFragment.newInstance(
                                establecimiento.getDeportes().toArray(new String[size]),
                                true,
                                item),
                        fragmentTag)
                .addToBackStack(null)
                .commit();
    }
    //endregion

    //region PACK_CALLBACK
    @Override
    public void onEleccionPack(Pack pack) {
        setearPack(pack);
    }
    //endregion

    //region GOOGLE_API
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Servicio de google caido", Toast.LENGTH_SHORT).show();
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(clienteAPI).setResultCallback(
                new ResultCallback<Status>() {
                    public void onResult(Status status) {
                        finish();
                    }
                });
    }
    //endregion

    //region TOOLBAR
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
        Intent i= new Intent(this, RegistrarseActivity.class);
        i.putExtra("establecimiento", establecimiento);
        i.putExtra("editar", true);
        i.putExtra("Tdeportes", deportes);
        startActivity(i);
    }

    @Override
    protected void opciones() {

    }
    //endregion

    @Override
    protected void procesarRespuestaExitosa(JSONObject respuesta) {
        if(esTablet) {
            //Si invoque al servicio para crear una oferta
            if (fragmentTag.equals(Constantes.CREAR_OFERTA_FRAG)) {
                //Obtengo las ofertas para mostrarlas
                fragmentTag = "ver_ofertas_fragment";
                String url = Constantes.GET_OFERTAS_ESTABLECIMIENTO +
                        "&idEstablecimiento=" + establecimiento.getId();
                VolleyRequestService.startActionGetRequest(this, url);
            } else if (fragmentTag.equals("ver_ofertas_fragment")) {
                //Si llame para ver las ofertas
                try {
                    Type collectionType = new TypeToken<Collection<Oferta>>() {
                    }.getType();
                    Collection<Oferta> enums = gson.fromJson(respuesta.getJSONArray("ofertas").toString(), collectionType);
                    ofertas = enums.toArray(new Oferta[enums.size()]);
                    for (Oferta oferta : ofertas) {
                        oferta.setNombreDeporte(
                                deportes[oferta.getIdDeporte() - 1]
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Cambio cualquier fragment por el de mostrar las ofertas
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.panel_info_menu_principal,
                                OfertasFragment.newInstance(ofertas),
                                fragmentTag)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.panel_info_menu_principal,
                                OfertasFragment.newInstance(ofertas),
                                fragmentTag)
                        .commit();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Si el resultado es correcto
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                //Llame por los packs
                case COMPRAR_PACK_ACTIVITY: {
                    Pack packElegido= (Pack) data.getSerializableExtra(Constantes.PACK_ELEGIDO);
                    setearPack(packElegido);
                }
                case ESTABLECER_UBICACION: {
                    String direccion= data.getStringExtra("direccion");
                    establecimiento.setUbicacion(direccion);
                    if(direccion != null) {
                        Map<String, String> mapa = new HashMap<>();
                        mapa.put("funcion", "actualizarUsuario");
                        String[] aux= direccion.split(";");
                        if(aux.length < 2)
                            mapa.put("ubicacion",aux[0]);
                        else {
                            mapa.put("direccion", aux[0]);
                            mapa.put("ubicacion", aux[1]);
                        }
                        VolleyRequestService.startActionPostRequest(this, Constantes.UPDATE, mapa);
                    }

                }
            }
        }
    }


    //region METODOS_PRIVADOS
    private void setearRecordatorio() {
        boolean seteoTelefono= establecimiento.getTelefono() != 0;
        boolean seteoUbicacion= establecimiento.getUbicacion() != null;
        boolean seteoDeportes= (establecimiento.getDeportes() != null && establecimiento.getDeportes().size() != 0);
        boolean mostrar= (!seteoTelefono) || (!seteoUbicacion) || (!seteoDeportes);
        ((MenuPrincipalFragment)getSupportFragmentManager()
                .findFragmentByTag("botones_menu_principal"))
                .setRecordatorio(mostrar);
    }

    private void setOfertasRestantes() {
        int ofertasRestantes= establecimiento.getCantMaxOfertas() - ofertas.length;
        ((MenuPrincipalFragment)getSupportFragmentManager()
                .findFragmentByTag("fragment_menu_ppal")).setCantidadDeOfertas(ofertasRestantes);
    }

    private void setearPack(Pack packElegido) {
        establecimiento.setIdPack(packElegido.getId());
        establecimiento.setCantMaxOfertas(packElegido.getCantidadOfertas());
        setOfertasRestantes();
        Map<String, String> mapa= new HashMap<>();
        mapa.put("funcion","setUserPack");
        mapa.put("id",String.valueOf(establecimiento.getId()));
        mapa.put("idPack",String.valueOf(packElegido.getId()));
        VolleyRequestService.startActionPostRequest(this, Constantes.UPDATE, mapa);
    }
    //endregion
}
