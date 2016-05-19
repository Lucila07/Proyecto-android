package com.example.lucila.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lucila.myapplication.servicios.ObtenerDireccionService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class EstablecerUbicacionActivity
        extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    //Codigo usado para distiguir en el pedido de permisos
    public static final int PERMISO_UBICACION= 1;

    //API para el mapa de google
    private GoogleApiClient clienteGoogle;

    //Locacion
    private Location ultimaLocacionConocida;

    //Fragmentos
    private EstablecerUbicacionFragment fragmentoUbicacion;
    private SupportMapFragment fragmentoMapa;

    //Instancia del mapa de google
    private GoogleMap mapa;
    private Marker marcas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_establecer_ubicacion);

        fragmentoUbicacion= (EstablecerUbicacionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_estab_ubicacion);
        fragmentoMapa= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragmentoMapa.getMapAsync(this);

        if (clienteGoogle == null) {
            clienteGoogle = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        clienteGoogle.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        clienteGoogle.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //Usa el requestCode pasado en el requestPermision()
        switch (requestCode) {
            case PERMISO_UBICACION: {
                //Si el usuario acepto
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    obtenerUbicacion();
                }
                else {
                    //TODO Permisos - mirar mas adelante
                }
            }
        }
    }


    /**
     * Se llama cuando se toca el boton gps en la actividad
     */
    public void calcularUbicacion() {
        //Permiso peligroso: ahora android pregunta al usuario dentro de la aplicación.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            //Si es la primera vez que se usa se le pregunta con un dialogo y se esperar el resultado en el listener
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);
        else
            //Si ya tengo el permiso, obtengo la ubicacion
            obtenerUbicacion();
    }

    //Si llego aca es porque ya cheque los permisos
    @SuppressWarnings("MissingPermission")
    private void obtenerUbicacion() {
        if(clienteGoogle.isConnected()) {
            if(ultimaLocacionConocida != null) {
                ultimaLocacionConocida = LocationServices.FusedLocationApi.getLastLocation(clienteGoogle);
                ObtenerDireccionService.startService(this, new AddressResultReceiver(new Handler()), ultimaLocacionConocida);
            }
        }
        else {
            Toast.makeText(this, "Conexión no establecida", Toast.LENGTH_SHORT).show();
        }
    }

    private void setearDireccion(String direccion) {
        fragmentoUbicacion.setDireccion(direccion);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(ultimaLocacionConocida != null) {
            double longitud;
            double latitud;
            longitud= ultimaLocacionConocida.getLongitude();
            latitud= ultimaLocacionConocida.getLatitude();
            marcas= googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitud,longitud))
                                    .title("Ubicación"));
        }
        mapa= googleMap;
    }

    @SuppressLint("ParcelCreator")
    public class AddressResultReceiver extends ResultReceiver {
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
        }
    }
}
