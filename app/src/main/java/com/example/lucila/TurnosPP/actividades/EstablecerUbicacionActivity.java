package com.example.lucila.turnosPP.actividades;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.fragmentos.EstablecerUbicacionFragment;
import com.example.lucila.turnosPP.servicios.ObtenerDireccionService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class EstablecerUbicacionActivity
        extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
        EstablecerUbicacionFragment.OnEstablecerUbicacionListener {

    //Codigo usado para determinar la respuesta del usuario sobre los permisos de ubicacion
    public static final int PERMISO_UBICACION= 1;

    //API para el mapa de google
    private GoogleApiClient clienteGoogle;

    //Locacion
    private Location ultimaLocacionConocida;

    //Fragmentos
    private EstablecerUbicacionFragment fragmentoUbicacion;

    //Instancia del mapa de google
    private GoogleMap mapa;
    private Marker marcas;
    //Asumo que no tengo permiso
    private boolean permiso= false;
    private boolean registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_establecer_ubicacion);

        fragmentoUbicacion= (EstablecerUbicacionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_estab_ubicacion);
        SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragmentoMapa.getMapAsync(this);

        //Permiso peligroso: ahora android pregunta al usuario dentro de la aplicación.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            //Si es la primera vez que se usa, se le pregunta con un dialogo y se esperar el resultado.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);
        else {
            permiso= true;
        }
        if(savedInstanceState != null) {
            //Si guarde info
            ultimaLocacionConocida= savedInstanceState.getParcelable("locacion");
            if(ultimaLocacionConocida == null) {
                //Si por alguna razón se almaceno mal
                ultimaLocacionConocida= new Location("Bahñia Blanca");
                ultimaLocacionConocida.setLongitude(-39);
                ultimaLocacionConocida.setLatitude(-62);
            }
        }
        if (clienteGoogle == null) {
            clienteGoogle = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        registro= getIntent().getBooleanExtra("registro", false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("locacion", ultimaLocacionConocida);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        if(permiso)
            clienteGoogle.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        clienteGoogle.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    /**
     * Se llama cuando se toca el boton gps en la actividad
     */
    public void onObtenerUbicacionListener() {
            obtenerUbicacion();
    }

    @SuppressWarnings("MissingPermission")
    private void obtenerUbicacion() {
        if(clienteGoogle.isConnected()) {
            if(ultimaLocacionConocida == null)
                ultimaLocacionConocida= LocationServices.FusedLocationApi.getLastLocation(clienteGoogle);
            ObtenerDireccionService.startService(this, new AddressResultReceiver(new Handler()), ultimaLocacionConocida);
        }
        else {
            Toast.makeText(this, "Sin servicio GPS", Toast.LENGTH_SHORT).show();
        }
    }

    private void setearDireccion(String direccion) {
        fragmentoUbicacion.setDireccion(direccion);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double longitud= -39;
        double latitud= -62;
        if(ultimaLocacionConocida != null) {
            longitud= ultimaLocacionConocida.getLongitude();
            latitud= ultimaLocacionConocida.getLatitude();
            marcas= googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitud,longitud))
                                    .title(ultimaLocacionConocida.getProvider()));
        }
        mapa= googleMap;
        LatLng locacion= new LatLng(longitud,latitud);
        CameraUpdate camara= CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(locacion, 7));
        googleMap.moveCamera(camara);
    }

    @SuppressLint("ParcelCreator")
    public class AddressResultReceiver extends ResultReceiver {
        //Constructor
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String direccion = resultData.getString("resultado");
            setearDireccion(direccion);
            double longitud= ultimaLocacionConocida.getLongitude();
            double latitud= ultimaLocacionConocida.getLatitude();
            if(marcas != null) {
                marcas.setPosition(new LatLng(latitud, longitud));
            }
            else if (mapa != null) {
                    marcas = mapa.addMarker(new MarkerOptions()
                            .position(new LatLng(latitud, longitud))
                            .title("Ubicación"));
                    }
            if(registro) {
                crearIntentResultado(direccion);
            }
        }
    }

    private void crearIntentResultado(String direccion) {
        Intent resultado= new Intent();
        resultado.putExtra("ubicacion", direccion);
        setResult(RESULT_OK, resultado);
        finish();
    }
}
