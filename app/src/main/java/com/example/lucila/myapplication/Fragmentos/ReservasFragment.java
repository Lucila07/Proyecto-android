package com.example.lucila.myapplication.Fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucila.myapplication.AdapterReservas;
import com.example.lucila.myapplication.Datos.ItemData;
import com.example.lucila.myapplication.MyAdapter;
import com.example.lucila.myapplication.R;

/**
 * Created by Ratan on 7/9/2015.
 */
public class ReservasFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.reservas_layout,null);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.reservasView);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        // 3. create an adapter
        AdapterReservas mAdapter = new AdapterReservas(itemsData);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        return v;

    }
}
