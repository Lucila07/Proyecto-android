package com.example.lucila.myapplication.Fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lucila.myapplication.R;

/**
 * Created by Lucila on 30/04/2016.
 */
public class PerfilFragment  extends android.support.v4.app.Fragment {




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=  inflater.inflate(R.layout.perfil_layout,null);


        TextView nombre, telefono, localidad, denuncias, reservas;
        nombre=(TextView)v.findViewById(R.id.nombreUsuario);
        telefono=(TextView)v.findViewById(R.id.nroTel);
        localidad=(TextView)v.findViewById(R.id.nombreLocalidad);
        denuncias=(TextView)v.findViewById(R.id.nroDenuncias);
        reservas =(TextView)v.findViewById(R.id.nroRes);

        nombre.setText("Pepito Perez");
        telefono.setText("2926455554");
        localidad.setText("Coronel Suárez");
        reservas.setText("1");
        denuncias.setText("1");

        return v;
    }




}
