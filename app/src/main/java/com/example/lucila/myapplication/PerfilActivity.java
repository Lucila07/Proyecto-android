package com.example.lucila.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Usuario;

/**
 * Created by Lucila on 22/05/2016.
 */
public class PerfilActivity extends AppCompatActivity {

    private  Toolbar toolbar;

    private Usuario usuario;
    private  TextView nombre, telefono, localidad, denuncias, reservas;
    private ImageView fotoPerfil;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_layout);

        setToolbar();
        usuario= ServicioUsuariosHttp.getInstance().getUsuarioLogueado();

        nombre=(TextView)findViewById(R.id.nombreUsuario);
        telefono=(TextView)findViewById(R.id.nroTel);
        localidad=(TextView)findViewById(R.id.nombreLocalidad);
        denuncias=(TextView)findViewById(R.id.nroDenuncias);
        reservas =(TextView)findViewById(R.id.nroRes);
        fotoPerfil=(ImageView)findViewById(R.id.imageView_perfil_usuario);

        nombre.setText(usuario.getNombreApellido());
        telefono.setText(usuario.getTelefono());
        localidad.setText(usuario.getUbicacion());
        fotoPerfil.setImageURI(usuario.getUrlFoto());

        Log.d("perfil",usuario.getUbicacion()+" "+usuario.getUrlFoto().toString());
        //reservas TODO: hace run atributo reservas para el usuario, y denuncias
    }

    public void setToolbar(){
        //toolbar-------------
        toolbar = (Toolbar) findViewById(R.id.toolbar_perfil); //encontramos la instancia de la toolbar
        setSupportActionBar(toolbar);   //la setamos a la actividad


//        if (getSupportActionBar() != null) { // Habilitar up button para volcver atras

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //este muestra el boton de volver
        // getSupportActionBar().setDisplayShowHomeEnabled(true);//para volver atras
        //}


        getSupportActionBar().setTitle("Mi perfil");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //--------------------

    }

}
