package com.example.lucila.myapplication.Fragmentos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.example.lucila.myapplication.Datos.ItemData;
import com.example.lucila.myapplication.Datos.ServicioOfertasHttp;
import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.ExpandableListAdapter;
import com.example.lucila.myapplication.R;
import java.util.HashMap;

/**
 * Created by Lucila on 25/6/2016.
 */
public class ReservasUsuarioFragment extends android.support.v4.app.Fragment implements ServicioOfertasHttp.ReservasUsuarioCallback, ServicioUsuariosHttp.AccesoUsuarios {


    private Toolbar toolbar;
    private ItemData[] itemsData;
    private RecyclerView recyclerView;
    private ServicioOfertasHttp servicioOfertasHttp;
    private ServicioUsuariosHttp serviciousario;
    private   RecyclerView.LayoutManager mLayoutManager;
    private TextView ninguna_oferta;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private Oferta[] listDataHeader;
    private HashMap<Long, String> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View v=  inflater.inflate(R.layout.activity_reservas,null);
       // setToolbar(v);
        // get the listview
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);
        servicioOfertasHttp= ServicioOfertasHttp.getInstanciaServicioReservas(this,getActivity());
        serviciousario=ServicioUsuariosHttp.getInstance(this,getActivity());
        Usuario user= serviciousario.getUsuarioLogueado();
        Log.d("reservas hechas", "pido las reservas ");
        servicioOfertasHttp.establecerOfertasUsuarioLogueado(user);
        ninguna_oferta=(TextView)v.findViewById(R.id.ofertas_usuario_ninguna);

        return v;
    }

    public void setToolbar(View v){
         toolbar = (Toolbar) v.findViewById(R.id.toolbar_reservasHechas); //encontramos la instancia de la toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);   //la setamos a la actividad
        
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); //este muestra el boton de volver
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);//para volver atras
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mis Reservas");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setVisibility(View.VISIBLE);
    }


    @Override
    public void exito(Oferta[] ofertaArray) {
        Log.d("", "exito");
        if(ofertaArray.length>0) {
            Log.d("", "hay ofertas si");
            Log.d("","size " +ofertaArray.length);
            Log.d("","ejemplo" +ofertaArray[0].getDeporte().getNombre());
            listDataChild = new HashMap<Long, String>();
            listDataChild.put(ofertaArray[0].getCodigo(), "detalle");
            listDataChild.put(ofertaArray[1].getCodigo(), "detalle2");
            listAdapter = new ExpandableListAdapter(getContext(), ofertaArray);
             expListView.setAdapter(listAdapter);
        }
        else {
            Log.d("", "hay ofertas no");
            ninguna_oferta.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void cargarMain() {

    }

    @Override
    public void cargarTelefono() {

    }


    @Override
    public void fallo() {

    }
}
