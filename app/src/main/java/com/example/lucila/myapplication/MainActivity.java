package com.example.lucila.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.Fragmentos.OfertasFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private String navTitles[];
    private TypedArray navIcons;
    private ActionBarDrawerToggle drawerToggle;
    private GoogleApiClient clienteGoogle;
    private Location ultimaLocacionConocida;
    public static final int PERMISO_UBICACION = 1;
    private boolean permiso = false;
     private GoogleApiClient googleApiClient;

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


        //Servicio
        if (clienteGoogle == null) {
            clienteGoogle = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        Toast.makeText(MainActivity.this, "Error al conectarse ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        CheckEnableGPS();


        if (permiso)
            {
                clienteGoogle.connect();
            }

        setupToolbar();





        //Initialize Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerMainActivity);

        //Setup titulos e iconos del  Navigation Drawer
        navTitles = getResources().getStringArray(R.array.navDrawerItems);
        navIcons = getResources().obtainTypedArray(R.array.navDrawerIcons);


        recyclerViewAdapter = new RecyclerViewAdapter(navTitles, navIcons, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // setup ActionBarDrawerToggle
        setupDrawerToggle();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void onConnected(Bundle bundle) {
        if (clienteGoogle.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);

            }
            else
                ultimaLocacionConocida = LocationServices.FusedLocationApi.getLastLocation(clienteGoogle);

            if (ultimaLocacionConocida == null) {
                //Si por alguna razón se almaceno mal

                Log.d("locaclizacion","Error al obtener la localizacion");
            }

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if ((lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){

                obtenerLocacizacion(); // solo si  esta activado el gps

            }

        }

    }

    private void obtenerLocacizacion() {
        try {
            String strAddress="";
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);
            }
            else {
                List<Address> addresses=null;
                if(ultimaLocacionConocida==null) {

                    Log.d("locacion", "es null");


                }
                else
                     addresses = geocoder.getFromLocation(ultimaLocacionConocida.getLatitude(), ultimaLocacionConocida.getLongitude(), 1);

                    Address fetchedAddress;

                if (addresses != null) {
                    if (addresses.size() == 0) {

                        Log.d("", "No se ha podido establecer la ubicacion");
                        Toast.makeText(MainActivity.this, "No se ha podido establecer la ubicacion", Toast.LENGTH_SHORT).show();
                    } else {
                        fetchedAddress = addresses.get(0);
                        strAddress = fetchedAddress.getLocality();
                    }
                } else {
                    Log.d("", "No se ha podido establecer la ubicacion");
                    Toast.makeText(MainActivity.this, "No se ha podido establecer la ubicacion", Toast.LENGTH_SHORT).show();
                }

              //  Log.d("ubicacion: ", strAddress);
            }
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if ((lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))&&ultimaLocacionConocida!=null)
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
        getSupportActionBar().setTitle("Juga y ahorra");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //el boton de back


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                return true;

        }
        return true;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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
        final Activity actividad=this;
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
                        //Se setea la ubicacion por default
                        logueado.setUbicacion("nula");
                        MostrarOfertas();
                    }
                    Toast.makeText(MainActivity.this, "Se buscaran ofertas en Argentina", Toast.LENGTH_SHORT).show();

                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

    }
    protected void logout() {
        if(googleApiClient.isConnected()) {

            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                           Intent intent= new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)&&lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)&&permiso)
        {
            Log.d("restart","conecte locacion");
            clienteGoogle.connect();

        }

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



