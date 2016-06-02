package com.example.lucila.myapplication.Fragmentos;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucila.myapplication.Datos.*;
import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.MyAdapter;
import com.example.lucila.myapplication.R;
import com.example.lucila.myapplication.ReservaOfertaActivity;

import java.util.ArrayList;
import java.util.List;




public class OfertasFragment extends Fragment implements ServicioOfertasHttp.CallBack {

    private  ListView lista;
    private  Context contexto;
    private  RecyclerView mRecyclerView;
    private   MyAdapter mAdapter;
    private   RecyclerView.LayoutManager mLayoutManager;
    private   List<Oferta>ofertas;
    private  Spinner spinner;
    private  ServicioOfertasHttp servicioOfertasUsuario;
    private  Activity activity;
    private  List<Deporte>deportesLista;
    private  TextView tv_no_hay_ofertas;
    private Usuario usuarioLog;
    private Button bt_actualizar;
    public   OfertasFragment() {
        // Required empty public constructor
    }
/*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(lista);
        dest.writeValue(contexto);
        dest.writeValue(mRecyclerView);
        dest.writeValue(mAdapter);
        dest.writeValue(mLayoutManager);
        dest.writeList(ofertas);
        dest.writeParcelable((Parcelable) servicioOfertasUsuario,0);

    }
    public static final Parcelable.Creator<OfertasFragment> CREATOR
            = new Parcelable.Creator<OfertasFragment>() {
        public OfertasFragment createFromParcel(Parcel in) {
            return new OfertasFragment(in);
        }

        public OfertasFragment[] newArray(int size) {
            return new OfertasFragment[size];
        }
    };

    public  OfertasFragment(Parcel in){

        this.lista=(ListView)in.readValue(ListView.class.getClassLoader());
        this.contexto=(Context)in.readValue(Context.class.getClassLoader());
        this.mRecyclerView=(RecyclerView)in.readValue(RecyclerView.class.getClassLoader());
        this.mAdapter=(MyAdapter)in.readValue(MyAdapter.class.getClassLoader());
        this.mLayoutManager=(RecyclerView.LayoutManager)in.readValue(RecyclerView.LayoutManager.class.getClassLoader());
        this.ofertas=(List<Oferta>)in.readValue(List.class.getClassLoader());

    }
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        servicioOfertasUsuario= ServicioOfertasHttp.getInstanciaServicio(this,activity);
        usuarioLog= ServicioUsuariosHttp.getInstance().getUsuarioLogueado();

        //if (savedInstanceState==null) {
            Log.d(contexto.getClass().getSimpleName(), "hago requerimiento ");
            servicioOfertasUsuario.realizarPeticion();
        //}
      /*  else{
            Log.d(contexto.getClass().getSimpleName(), " ya habia ofertas");

            ofertas= savedInstanceState.getParcelableArrayList("ofertas");

            deportesLista=savedInstanceState.getParcelableArrayList("deportes");
            crearRecycler(ofertas);
            crearSpinner();

        }*/
    }


    @Override
    public void dibujarListaOfertas(){
    if (servicioOfertasUsuario==null)
    {
        servicioOfertasUsuario = ServicioOfertasHttp.getInstanciaServicio(this, getActivity());
    }

      if(usuarioLog!=null) {
          if (usuarioLog.getUbicacion() == null) {
              ofertas = servicioOfertasUsuario.getOfertas();

          } else {
              ofertas = servicioOfertasUsuario.getOfertasUbicacion(usuarioLog.getUbicacion());
          }
      }
      else{

          ofertas = servicioOfertasUsuario.getOfertas();
      }
        deportesLista =servicioOfertasUsuario.getDeportes();
        crearRecycler(ofertas);
        crearSpinner();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_ofertas, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        spinner = (Spinner)rootView.findViewById(R.id.deportes_spinner);
        contexto=getContext();
        activity=getActivity();
        bt_actualizar=(Button)rootView.findViewById(R.id.bt_actualizar);
        tv_no_hay_ofertas=(TextView)rootView.findViewById(R.id.tv_no_hay_ofertas);

        bt_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicioOfertasUsuario.realizarPeticion();
                Toast.makeText(getActivity(), "Las ofertas fueron actualizadas ", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

  /*  @Override
    public void onPause(){
       super.onPause();
       Log.d(contexto.getClass().getSimpleName(), " en pausa ");

    }
*/
    private void crearRecycler( List<Oferta>ofertas){

  // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
      if (!ofertas.isEmpty()) {

          mRecyclerView.setVisibility(View.VISIBLE);
          tv_no_hay_ofertas.setVisibility(View.GONE);
          mRecyclerView.setHasFixedSize(true);

          // use a linear layout manager
          mLayoutManager = new LinearLayoutManager(getActivity());
          mRecyclerView.setLayoutManager(mLayoutManager);

          // specify an adapter (see also next example)


          mAdapter = new MyAdapter(ofertas, contexto);
          mAdapter.ClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(getActivity(), ReservaOfertaActivity.class);

                  //Intent intent = new Intent( ReservaOfertaActivity.class);
                  TextView textCodigo = (TextView) v.findViewById(R.id.oferta_id);
                  long codigoOferta = Long.parseLong(textCodigo.getText().toString());
                  Oferta oferta = servicioOfertasUsuario.getOfertaCodigo(codigoOferta);
                  //  intent.putExtra("id_oferta",Long.parseLong(codigo.getText().toString()));
                  intent.putExtra("oferta", (Parcelable) oferta);
                  // intent.putExtra("ServicioHttp",(Parcelable) servicioOfertasUsuario);
                  getActivity().startActivity(intent);
              }
          });
          mRecyclerView.setAdapter(mAdapter);
      }
        else{
            mRecyclerView.setVisibility(View.GONE);
            tv_no_hay_ofertas.setVisibility(View.VISIBLE);
      }
    }


    public void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("ofertas",(ArrayList<Oferta>) ofertas);
        outState.putParcelableArrayList("deportes",(ArrayList<Deporte>)deportesLista);
        Log.d(contexto.getClass().getSimpleName(), " onsaveinstance ");
    }


  /*   public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode ==123 ) {
            // Make sure the request was successful
            if (resultCode == 1) {

                // Do something with the contact here (bigger example below)
            }
        }
    }
*/

    /**
     * crear el desplegable con los distintos deportes para que el usuario pueda filtrar por deporte
     * */
    private void crearSpinner(){

      //  Spinner spinner = (Spinner) getActivity().findViewById(R.id.deportes_spinner);

        ArrayList<String> deportes=new ArrayList<String>();

        deportes.add("todos");
        for (int i=0;i<deportesLista.size();i++)
            deportes.add(deportesLista.get(i).getNombre());


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(contexto,android.R.layout.simple_spinner_item,deportes);
        //createFromResource(this, deportes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String deporte =  parent.getItemAtPosition(position).toString();
                if(!deporte.equals("todos")) {
                   List<Oferta> ofertasDep;

                    if(usuarioLog!=null) {
                        if (usuarioLog.getUbicacion() == null) {

                            ofertasDep= servicioOfertasUsuario.getOfertasDeporte(servicioOfertasUsuario.getDeporte(deporte));

                        }
                        else
                        {
                            String ubicacion= usuarioLog.getUbicacion();
                            ofertasDep= servicioOfertasUsuario.getOfertasDeporteEnUbicacion(servicioOfertasUsuario.getDeporte(deporte),ubicacion);
                        }
                    }
                    else{
                        ofertasDep= servicioOfertasUsuario.getOfertasDeporte(servicioOfertasUsuario.getDeporte(deporte));

                    }
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

    /**
     * metodos de la interfaz callback de servicioOfertas
     * */
    @Override
    public void reservaExito() {

    }

    @Override
    public void reservaFallo() {

    }
}
