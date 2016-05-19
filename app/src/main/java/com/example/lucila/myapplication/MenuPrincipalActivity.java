package com.example.lucila.myapplication;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MenuPrincipalActivity extends AppCompatActivity {

    private Map<Integer, Class> mapBotonClase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        mapBotonClase= new HashMap<Integer, Class>(7);
        mapBotonClase.put(R.id.boton_crear_oferta, CrearOfertasActivity.class);
        mapBotonClase.put(R.id.boton_comprar_ofertas, LoginActivity.class);
        mapBotonClase.put(R.id.boton_denunciar_usuario, LoginActivity.class);
        mapBotonClase.put(R.id.boton_establecer_ubicacion, EstablecerUbicacionActivity.class);
        mapBotonClase.put(R.id.boton_ver_creadas, LoginActivity.class);

        //TODO ver el tema del TextView con la cantidad de pack restantes
    }

    //Métodos onclick de los botones
    public void click(View view) {
        Class claseActivity= mapBotonClase.get(view.getId());
        Intent sigActividad= new Intent(this, claseActivity);
        startActivity(sigActividad);
    }
}
