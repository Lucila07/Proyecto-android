package com.example.lucila.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.lucila.myapplication.Datos.ServicioOfertasUsuario;
import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.Fragmentos.CrearPartidoFragment;
import com.example.lucila.myapplication.Fragmentos.FragmentPartidos;
import com.example.lucila.myapplication.Fragmentos.OfertasFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ServicioOfertasUsuario servicioOfertasUsuario;


    //lu-------------
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

        //--------------------

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //lu----------

        //Initialize Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerMainActivity);

        //Setup Titles and Icons of Navigation Drawer
        navTitles = getResources().getStringArray(R.array.navDrawerItems);
        navIcons = getResources().obtainTypedArray(R.array.navDrawerIcons);


        /**
         *Here , pass the titles and icons array to the adapter .
         *Additionally , pass the context of 'this' activity .
         *So that , later we can use the fragmentManager of this activity to add/replace fragments.
         */

        recyclerViewAdapter = new RecyclerViewAdapter(navTitles, navIcons, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        /**
         *It is must to set a Layout Manager For Recycler View
         *As per docs ,
         *RecyclerView allows client code to provide custom layout arrangements for child views.
         *These arrangements are controlled by the RecyclerView.LayoutManager.
         *A LayoutManager must be provided for RecyclerView to function.
         */

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Finally setup ActionBarDrawerToggle
        setupDrawerToggle();


    }


    public void onConnected(Bundle bundle) {
        if(clienteGoogle.isConnected()) {
            ultimaLocacionConocida= LocationServices.FusedLocationApi.getLastLocation(clienteGoogle);
            if (ultimaLocacionConocida == null) { // fallo , le seteo una loc por default
                //Si por alguna razón se almaceno mal
                ultimaLocacionConocida = new Location("Bahia Blanca");
                ultimaLocacionConocida.setLongitude(-38.7167 );
                ultimaLocacionConocida.setLatitude(-62.2833);
            }

            obtenerLocacizacion();
        }
        else  Log.d("","no connected");

    }

    private void obtenerLocacizacion() {
        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(ultimaLocacionConocida.getLatitude(), ultimaLocacionConocida.getLongitude(), 1);
            Log.d("", String.valueOf(addresses.size()));
            if (addresses != null) {
               Address fetchedAddress = addresses.get(0);
                String strAddress =addresses.get(0).getLocality();
                Log.d("ubicacion: ",strAddress);
                Toast.makeText(MainActivity.this, "Se buscaran ofertas en: "+strAddress, Toast.LENGTH_SHORT).show();
                //establecemos la ubicacion en el usuario logueado
                Usuario logueado=ServicioUsuariosHttp.getInstance().getUsuarioLogueado();
                if(logueado!=null)
                {
                    logueado.setUbicacion(strAddress);
                }
            } else {
                Log.d("", "No se ha podido establecer la ubicacion");
                Toast.makeText(MainActivity.this, "No se ha podido establecer la ubicacion", Toast.LENGTH_SHORT).show();
            }
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

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle() {
        //icono
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        this.drawerToggle.syncState();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        OfertasFragment fragmentoOfertas = new OfertasFragment();
       /*
        fragmentoOfertas.setServicioOfertas(servicio);*/
        adapter.addFragment(fragmentoOfertas, "Ofertas");

       // adapter.addFragment(new FragmentPartidos(), "Partidos");
      //  adapter.addFragment(new CrearPartidoFragment(), "Crear Partido");

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        clienteGoogle.disconnect();
        super.onStop();
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
            Log.d("ciudad",ciudad);

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



