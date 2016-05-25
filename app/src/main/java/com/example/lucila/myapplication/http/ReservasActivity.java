package com.example.lucila.myapplication.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.lucila.myapplication.AdapterReservas;
import com.example.lucila.myapplication.Datos.ItemData;
import com.example.lucila.myapplication.R;

public class ReservasActivity extends AppCompatActivity {


    private  Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.reservasView);

        // this is data fro recycler view

        ItemData itemsData[] = {
                new ItemData(0, "Club Universitario","21-3-2016","20:00"),
                new ItemData(1, "Liniers ","20-3-2016","21:30"),
                new ItemData(2, "Cancha2", "21-4-2016","20:30"),
                new ItemData(1, "Cancha3", "20-3-2016","22:00"),
                new ItemData(0, "Universitario","21-3-2016","20:00"),
                new ItemData(1, "Cancha1","20-3-2016","21:30"),
                new ItemData(2, "Cancha2", "21-4-2016","20:30"),
                new ItemData(1, "Cancha3", "20-3-2016","22:00"),};



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        AdapterReservas mAdapter = new AdapterReservas(itemsData);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setToolbar();

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
}
