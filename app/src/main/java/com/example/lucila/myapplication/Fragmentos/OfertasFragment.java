package com.example.lucila.myapplication.Fragmentos;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucila.myapplication.Datos.*;
import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.MyAdapter;
import com.example.lucila.myapplication.R;
import com.example.lucila.myapplication.ReservaOfertaActivity;

import java.util.ArrayList;
import java.util.List;


public class OfertasFragment extends Fragment {

    private ListView lista;
    private Context contexto;
    private RecyclerView mRecyclerView;
    private  MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private   List<Oferta>ofertas;

    private  ServicioOfertasUsuario servicioOfertasUsuario;

    public OfertasFragment() {
        // Required empty public constructor
    }

    /**
     * inyecta la dependencia del servicio de ofertas.
     * @parametro  ServicioOfertasUsuario de ofertas
     * **/
    public  void setServicioOfertas(ServicioOfertasUsuario s){
        this.servicioOfertasUsuario=s;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servicioOfertasUsuario=new OfertasLista();// horribleee
        ofertas=servicioOfertasUsuario.getOfertas();
        crearRecycler(ofertas);
        crearSpinner();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_ofertas, container, false);

        return rootView;
    }



    private void crearRecycler( List<Oferta>ofertas){



        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        mAdapter = new MyAdapter(ofertas,contexto);
        mAdapter.ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReservaOfertaActivity.class);


                TextView codigo=(TextView) v.findViewById(R.id.oferta_id);

                intent.putExtra("id_oferta",Long.parseLong(codigo.getText().toString()));

                getActivity().startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * crear el desplegable con los distintos deportes para que el usuario pueda filtrar por deporte
     * */
    private void crearSpinner(){

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.deportes_spinner);

        ArrayList<String> deportes=new ArrayList<String>();
        List<Deporte>deportesLista =servicioOfertasUsuario.getDeportes();
        deportes.add("todos");
        for (int i=1;i<deportesLista.size();i++)
            deportes.add(deportesLista.get(i).getNombre());


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,deportes);
        //createFromResource(this, deportes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String deporte =  parent.getItemAtPosition(position).toString();
                if(!deporte.equals("todos")) {
                   List<Oferta> ofertasDep = servicioOfertasUsuario.getOfertasDeporte(servicioOfertasUsuario.getDeporte(deporte));
                    crearRecycler(ofertasDep);
                   // Toast.makeText(getActivity(), "eleemnto sele " + position, Toast.LENGTH_LONG).show();
                }
                else{

                    crearRecycler(ofertas);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
