package com.example.lucila.myapplication;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import com.example.lucila.myapplication.Datos.OfertasLista;
import com.example.lucila.myapplication.Datos.ServicioOfertasUsuario;
import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Fragmentos.CrearPartidoFragment;
import com.example.lucila.myapplication.Fragmentos.FragmentPartidos;
import com.example.lucila.myapplication.Fragmentos.OfertasFragment;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ServicioOfertasUsuario servicioOfertasUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar-------------
        toolbar = (Toolbar) findViewById(R.id.toolbar); //encontramos la instancia de la toolbar
        setSupportActionBar(toolbar);   //la setamos a la actividad
        getSupportActionBar().setTitle("Deportes");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //el boton de back
        //--------------------

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        OfertasFragment fragmentoOfertas= new OfertasFragment();
       /* ServicioOfertasUsuario servicio= new OfertasLista();
        fragmentoOfertas.setServicioOfertas(servicio);*/
        adapter.addFragment(fragmentoOfertas, "Ofertas");

        adapter.addFragment(new FragmentPartidos(), "Partidos");
        adapter.addFragment(new CrearPartidoFragment(),"Crear Partido");

        viewPager.setAdapter(adapter);
    }



  //  FragmentPagerAdapter

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        public Fragment getItem(int position) {
            return  mFragmentList.get(position);
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


}

