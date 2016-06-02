package com.example.lucila.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lucila.myapplication.AdapterReservas;
import com.example.lucila.myapplication.Datos.ItemData;
import com.example.lucila.myapplication.Datos.ServicioOfertasHttp;
import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.R;

public class ReservasUsuarioActivity extends AppCompatActivity implements ServicioOfertasHttp.ReservasUsuarioCallback, ServicioUsuariosHttp.AccesoUsuarios {


    private  Toolbar toolbar;
    private ItemData[] itemsData;
    private  RecyclerView recyclerView;
    private ServicioOfertasHttp servicioOfertasHttp;
    private ServicioUsuariosHttp serviciousario;
    private   RecyclerView.LayoutManager mLayoutManager;
    private TextView ninguna_oferta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.reservasView);

        servicioOfertasHttp= ServicioOfertasHttp.getInstanciaServicioReservas(this,this);
        serviciousario=ServicioUsuariosHttp.getInstance(this,this);
        Usuario user= serviciousario.getUsuarioLogueado();
        Log.d("reservas hechas","pido las reservas ");
        servicioOfertasHttp.establecerOfertasUsuarioLogueado(user);

        ninguna_oferta=(TextView)findViewById(R.id.ofertas_usuario_ninguna);



    }


    public void setToolbar(){
        //toolbar-------------
        toolbar = (Toolbar) findViewById(R.id.toolbar_reservasHechas); //encontramos la instancia de la toolbar
        setSupportActionBar(toolbar);   //la setamos a la actividad


//        if (getSupportActionBar() != null) { // Habilitar up button para volcver atras

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //este muestra el boton de volver
        getSupportActionBar().setDisplayShowHomeEnabled(true);//para volver atras
        //}


        getSupportActionBar().setTitle("Mis Reservas");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //--------------------

    }

    @Override
    public void exito(Oferta[] ofertaArray) {

       if(ofertaArray.length>0) {
           //  create an adapter
           Log.d("reservas hechas", "creo el adapter cant ofe" + ofertaArray.length);
           mLayoutManager = new LinearLayoutManager(this);
           recyclerView.setLayoutManager(mLayoutManager);

           AdapterReservas mAdapter = new AdapterReservas(ofertaArray);

           //  set adapter
           recyclerView.setAdapter(mAdapter);
           // set item animator to DefaultAnimator
           recyclerView.setItemAnimator(new DefaultItemAnimator());
       }
        else {
           ninguna_oferta.setVisibility(View.VISIBLE);
       }
    }

    @Override
    public void fallo() {
        //TODO:hacer el fallo
    }

    @Override
    public void cargarMain() {

    }

    @Override
    public void cargarTelefono() {

    }
}


