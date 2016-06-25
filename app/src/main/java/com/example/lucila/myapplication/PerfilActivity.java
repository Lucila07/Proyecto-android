package com.example.lucila.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Usuario;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Lucila on 22/05/2016.
 */
public class PerfilActivity extends AppCompatActivity {

    private  Toolbar toolbar;

    private Usuario usuario;
    private  TextView nombre, telefono, localidad,mail;
    private ImageView fotoPerfil;
    private Button bt_editar_perfil;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_layout);

        setToolbar();
        usuario= ServicioUsuariosHttp.getInstance().getUsuarioLogueado();

        nombre=(TextView)findViewById(R.id.nombreUsuario);
        telefono=(TextView)findViewById(R.id.nroTel);
        localidad=(TextView)findViewById(R.id.nombreLocalidad);
        fotoPerfil=(ImageView)findViewById(R.id.imageView_perfil_usuario);
        mail=(TextView)findViewById(R.id.tv_mail);
        bt_editar_perfil=(Button)findViewById(R.id.bt_editar_perfil);

    if(usuario!=null) {
        nombre.setText(usuario.getNombreApellido());
        telefono.setText(usuario.getTelefono());
        localidad.setText(usuario.getUbicacion());
        mail.setText(usuario.getEmail());
    }
        new Thread(
                new Runnable() {

                    public void run() {


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                  DescargarBitMap descarga= new DescargarBitMap();
                                  if(usuario!=null&&usuario.getUrlFoto()!=null)
                                    descarga.execute(usuario.getUrlFoto().toString());

                                }
                            });

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }

                    }
                }).start();


      //  Log.d("perfil",usuario.getUbicacion()+" "+usuario.getUrlFoto().toString());
        //reservas TODO: hace run atributo reservas para el usuario, y denuncias

        bt_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PerfilActivity.this, EditarPerfilActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setToolbar(){
        //toolbar-------------
      //  toolbar = (Toolbar) findViewById(R.id.toolbar_perfil); //encontramos la instancia de la toolbar
        setSupportActionBar(toolbar);   //la setamos a la actividad


//        if (getSupportActionBar() != null) { // Habilitar up button para volcver atras

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //este muestra el boton de volver
        // getSupportActionBar().setDisplayShowHomeEnabled(true);//para volver atras
        //}


        getSupportActionBar().setTitle("Mi perfil");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //--------------------

    }



  private  class DescargarBitMap extends AsyncTask<String,Integer, Bitmap> {
        Bitmap bm = null;


        protected Bitmap doInBackground(String... url) {
            try {
                Log.d("URL",url[0]);
                URL aURL = new URL(url[0]);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();

            } catch (Exception e) {

            }
            return bm;
        }

        protected void onPostExecute(Bitmap feed) {
           if(bm!=null) {
               fotoPerfil.setImageBitmap(feed);
               Log.d("foto perfil", "se seteo con exito");
           }
        }
    }
}
