package com.example.lucila.turnosPP.actividades;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.fragmentos.CrearOfertasFragment;
import com.example.lucila.turnosPP.fragmentos.EstablecerUbicacionFragment;
import com.example.lucila.turnosPP.fragmentos.MenuPrincipalFragment;
import com.example.lucila.turnosPP.fragmentos.OfertasFragment;

import java.util.HashMap;
import java.util.Map;

public class MenuPrincipalActivity
        extends AppCompatActivity
        implements  MenuPrincipalFragment.OnBotonMenuClickListener,
                    CrearOfertasFragment.OnCrearOfertaListener,
                    EstablecerUbicacionFragment.OnEstablecerUbicacionListener {

    private Map<Integer, Class> mapBotonClase;
    private Map<Integer, Fragment> mapBotonFrag;
    private boolean esTablet;
    private boolean enPortaretrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        mapBotonClase= new HashMap<Integer, Class>(7);
        mapBotonClase.put(R.id.boton_crear_oferta, CrearOfertasActivity.class);
        mapBotonClase.put(R.id.boton_comprar_ofertas, LoginActivity.class);
        mapBotonClase.put(R.id.boton_denunciar_usuario, LoginActivity.class);
        mapBotonClase.put(R.id.boton_establecer_ubicacion, EstablecerUbicacionActivity.class);
        mapBotonClase.put(R.id.boton_ver_creadas, OfertasActivity.class);

        esTablet= getResources().getBoolean(R.bool.esTablet);
        enPortaretrato= getResources().getBoolean(R.bool.enPortaretratos);

        if(esTablet) {
            mapBotonFrag = new HashMap<Integer, Fragment>(7);
            mapBotonFrag.put(R.id.boton_crear_oferta, new CrearOfertasFragment());
            mapBotonFrag.put(R.id.boton_comprar_ofertas, new CrearOfertasFragment());
            mapBotonFrag.put(R.id.boton_denunciar_usuario, new CrearOfertasFragment());
            mapBotonFrag.put(R.id.boton_establecer_ubicacion, new EstablecerUbicacionFragment());
            mapBotonFrag.put(R.id.boton_ver_creadas, new OfertasFragment());
        }
        //TODO ver el tema del TextView con la cantidad de pack restantes
    }

    //Métodos onclick de los botones
    public void onClickBoton(View view) {
        int viewID= view.getId();

        if(!esTablet) {
            //Si no es una tablet creo el intent con una actividad.
            Class claseActivity = mapBotonClase.get(viewID);
            Intent sigActividad = new Intent(this, claseActivity);
            startActivity(sigActividad);
        }
        else {
            if(enPortaretrato) {
                //TODO tablet en portaretrato
            }
            //Cambio el fragment en el contenedor.
            FragmentTransaction fragTrans= getSupportFragmentManager().beginTransaction();
            fragTrans.replace(R.id.panel_info_menu_principal, mapBotonFrag.get(viewID));
            fragTrans.commit();
        }
    }


    /*
    * Callbacks de los distintos fragments
    */
    /* CrearOfertaFragment */
    @Override
    public void onCrearOferta(Oferta oferta) {

    }

    @Override
    public void mostrarDialogoHora() {

    }

    @Override
    public void mostrarDialogoFecha() {

    }

    /* EstablecerUbicacionFragment */
    @Override
    public void onObtenerUbicacionListener() {

    }
}
