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
import android.widget.ListView;
import android.widget.TextView;

import com.example.lucila.myapplication.Datos.*;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.MyAdapter;
import com.example.lucila.myapplication.R;
import com.example.lucila.myapplication.ReservaOfertaActivity;

import java.util.List;


public class OfertasFragment extends Fragment {

    private ListView lista;
    private Context contexto;
    private RecyclerView mRecyclerView;
    private  MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public OfertasFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // initEjemploListSimple();

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crearRecycler();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_ofertas, container, false);
      //  lista=(ListView)rootView.findViewById(R.id.listView);
        //contexto=container.getContext();
        return rootView;
    }

    private void initEjemploListSimple(){
        //lo que estaba
        // ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,alumnos);
        // MyAdapter adapter = new MyAdapter(this.getContext(), R.layout.fragment_ofertas, OfertasLista.randomList(5)); //creo mi adapatador
       // lista.setAdapter(adapter);

    }

    private void crearRecycler(){



        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        List<Oferta>ofertas=OfertasLista.randomList(10);
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
}
