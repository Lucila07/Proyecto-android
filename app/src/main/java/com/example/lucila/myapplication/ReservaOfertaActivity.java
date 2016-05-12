package com.example.lucila.myapplication;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ReservaOfertaActivity extends AppCompatActivity {
    private TextView textview_codigo;
    private  Long id_oferta;
    private  Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserva_oferta);
        Intent intent=getIntent();
        id_oferta=intent.getExtras().getLong("id_oferta");





    }
   public void setToolbar(){
       //toolbar-------------
       toolbar = (Toolbar) findViewById(R.id.toolbar_reserva); //encontramos la instancia de la toolbar
       setSupportActionBar(toolbar);   //la setamos a la actividad

       if (getSupportActionBar() != null) { // Habilitar up button para volcver atras
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setDisplayShowHomeEnabled(true);//para volver atras
       }

       getSupportActionBar().setTitle("Hace tu reserva");
       getSupportActionBar().setDisplayShowTitleEnabled(true);

       //--------------------

   }
}
