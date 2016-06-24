package com.example.lucila.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.lucila.myapplication.Datos.ItemData;
import com.example.lucila.myapplication.Datos.ServicioOfertasHttp;
import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;

import java.util.HashMap;

public class ReservasUsuarioActivity extends AppCompatActivity implements ServicioOfertasHttp.ReservasUsuarioCallback, ServicioUsuariosHttp.AccesoUsuarios {


    private  Toolbar toolbar;
    private ItemData[] itemsData;
    private  RecyclerView recyclerView;
    private ServicioOfertasHttp servicioOfertasHttp;
    private ServicioUsuariosHttp serviciousario;
    private   RecyclerView.LayoutManager mLayoutManager;
    private TextView ninguna_oferta;
    //.------------------------------------prueba
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
   Oferta[] listDataHeader;
    HashMap<Long, String> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.reservasView);

        servicioOfertasHttp= ServicioOfertasHttp.getInstanciaServicioReservas(this,this);
        serviciousario=ServicioUsuariosHttp.getInstance(this,this);
        Usuario user= serviciousario.getUsuarioLogueado();
        Log.d("reservas hechas","pido las reservas ");
        servicioOfertasHttp.establecerOfertasUsuarioLogueado(user);

        ninguna_oferta=(TextView)findViewById(R.id.ofertas_usuario_ninguna);
*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);
        setToolbar();
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        servicioOfertasHttp= ServicioOfertasHttp.getInstanciaServicioReservas(this,this);
        serviciousario=ServicioUsuariosHttp.getInstance(this,this);
        Usuario user= serviciousario.getUsuarioLogueado();
        Log.d("reservas hechas", "pido las reservas ");
        servicioOfertasHttp.establecerOfertasUsuarioLogueado(user);

        ninguna_oferta=(TextView)findViewById(R.id.ofertas_usuario_ninguna);

        // preparing list data
   //     prepareListData();

      //  listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        //expListView.setAdapter(listAdapter);

    }

    //--------------------------------------------------

    /*private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

*/
    //-------------------------------------------------------

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



    // setting list adapter


    public void exito(Oferta[] ofertaArray) {

       if(ofertaArray.length>0) {
           //  create an adapter
           Log.d("reservas hechas", "creo el adapter cant ofe" + ofertaArray.length);
      //     mLayoutManager = new LinearLayoutManager(this);
        //   recyclerView.setLayoutManager(mLayoutManager);

    //AdapterReservas mAdapter = new AdapterReservas(ofertaArray);
           listDataChild = new HashMap<Long, String>();


           // listDataHeader = new ArrayList<String>();
         //  listDataHeader.add(ofertaArray[0].getUbicacion());
           listDataChild.put(ofertaArray[0].getCodigo(), "detalle");
           listDataChild.put(ofertaArray[1].getCodigo(), "detalle2");
         //  listAdapter = new ExpandableListAdapter(this, ofertaArray, listDataChild);

           listAdapter = new ExpandableListAdapter(this, ofertaArray);
           expListView.setAdapter(listAdapter);
           //  set adapter
           //recyclerView.setAdapter(mAdapter);
           // set item animator to DefaultAnimator
          // recyclerView.setItemAnimator(new DefaultItemAnimator());
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


