package com.example.lucila.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.ResultReceiver;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.Fragmentos.OfertasFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private Toolbar toolbar;


    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    String navTitles[];
    TypedArray navIcons;
    ActionBarDrawerToggle drawerToggle;

    private GoogleApiClient clienteGoogle;
    private Location ultimaLocacionConocida;
    public static final int PERMISO_UBICACION = 1;
    private boolean permiso = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            //Si es la primera vez que se usa, se le pregunta con un dialogo y se esperar el resultado.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);
        else {
            permiso = true;
        }


        //Servicio--------------------------------------------
        if (clienteGoogle == null) {
            clienteGoogle = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (permiso) {
            clienteGoogle.connect();
        }
        //------------------------------------------
        //toolbar-------------
        setupToolbar();



        CheckEnableGPS();



        //Initialize Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerMainActivity);

        //Setup Titles and Icons of Navigation Drawer
        navTitles = getResources().getStringArray(R.array.navDrawerItems);
        navIcons = getResources().obtainTypedArray(R.array.navDrawerIcons);


        recyclerViewAdapter = new RecyclerViewAdapter(navTitles, navIcons, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Finally setup ActionBarDrawerToggle
        setupDrawerToggle();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Thin.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // borrar ultima ubic conocida

    public void onConnected(Bundle bundle) {
        Log.d("","on connected");
        if (clienteGoogle.isConnected()) {
            Log.d("","is connected");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);

            }
            else
                ultimaLocacionConocida = LocationServices.FusedLocationApi.getLastLocation(clienteGoogle);

            if (ultimaLocacionConocida == null) { // fallo , le seteo una loc por default
                //Si por alguna razón se almaceno mal

                Log.d("","Error al obtener la localizacion");
            }

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if ((lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){

                obtenerLocacizacion(); // solo esta activado el gps

            }

        } else Log.d("", "no connected");

    }

    private void obtenerLocacizacion() {
        try {   // String strAddress="Bahía Blanca";  // seteo ubicacion por default
            String strAddress="";
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            // acomodar permisos
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);

            }
            else {
                List<Address> addresses = geocoder.getFromLocation(ultimaLocacionConocida.getLatitude(), ultimaLocacionConocida.getLongitude(), 1);
                Log.d("", "addresses");
                Log.d("", String.valueOf(addresses.size()));
                Address fetchedAddress;

                if (addresses != null) {
                    if (addresses.size() == 0) {
                        //ACOMODAR NI ENTRARIA NUNCA
                        strAddress = "CORONEL SUAREZ";
                        Log.d("", "No se ha podido establecer la ubicacion");

                    } else {
                        fetchedAddress = addresses.get(0);
                        strAddress = fetchedAddress.getLocality();
                    }
                } else {
                    Log.d("", "No se ha podido establecer la ubicacion");
                    Toast.makeText(MainActivity.this, "No se ha podido establecer la ubicacion", Toast.LENGTH_SHORT).show();
                }

                Log.d("ubicacion: ", strAddress);
            }
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if ((lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)))
                Toast.makeText(MainActivity.this, "Se buscaran ofertas en: " + strAddress, Toast.LENGTH_SHORT).show();

            //establecemos la ubicacion en el usuario logueado
            Usuario logueado = ServicioUsuariosHttp.getInstance().getUsuarioLogueado();
            if (logueado != null) {
                logueado.setUbicacion(strAddress);
            }

            MostrarOfertas();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer la ubicacion", Toast.LENGTH_LONG).show();
        }
    }


    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("error", "Error en la conexion");
    }


    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolBar); //encontramos la instancia de la toolbar
        setSupportActionBar(toolbar);   //la setamos a la actividad
        getSupportActionBar().setTitle("Deportes");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //el boton de back


    }

    void setupDrawerToggle() {
        //icono
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        this.drawerToggle.syncState();
    }

    private void MostrarOfertas() {

        OfertasFragment fragmentoOfertas = new OfertasFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerView,fragmentoOfertas,null);
        fragmentTransaction.commit();


    }


    private void CheckEnableGPS() {

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||  !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Servicio de localización desactivado");
            builder.setMessage("Geolocalizacion no habilitada");
            builder.setPositiveButton("ACTIVAR", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("CONTINUAR", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialogInterface, int i) {

                    Usuario logueado = ServicioUsuariosHttp.getInstance().getUsuarioLogueado();
                    if (logueado != null) {
                        logueado.setUbicacion("Bahía Blanca");
                        MostrarOfertas();
                    }
                    Toast.makeText(MainActivity.this, "Se utilizará la ubicación default: Bahía Blanca", Toast.LENGTH_SHORT).show();

                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

    }

    public void setInvisibleToolbar(){
        getSupportActionBar().hide();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onRestart() {
        clienteGoogle.connect();
        super.onRestart();

    }
    @Override
    protected void onStop() {
        clienteGoogle.disconnect();
        super.onStop();
        Log.d("", "On stop");

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("", "On pause");
    }


    //  FragmentPagerAdapter

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class AddressResultReceiver extends ResultReceiver {
        //Constructor
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String ciudad = resultData.getString("resultado");
            //TODO implementacion
            Log.d("ciudad", ciudad);

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //Usa el requestCode pasado en el requestPermision()
        switch (requestCode) {
            case PERMISO_UBICACION: {
                //Si el usuario acepto
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permiso = true;
                } else {
                    permiso = false;
                }
            }

        }
    }

}



