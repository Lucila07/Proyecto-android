package com.example.lucila.myapplication.Fragmentos;

/**
 * Created by Lucila on 25/6/2016.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.EditarPerfilActivity;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by Lucila on 30/04/2016.
 */
public class PerfilFragment  extends android.support.v4.app.Fragment {


    private  Toolbar toolbar;
    private Usuario usuario;
    private  TextView nombre, telefono, localidad,mail;
    private ImageView fotoPerfil;
    private Button bt_editar_perfil;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v=  inflater.inflate(R.layout.perfil_layout,null);

        usuario= ServicioUsuariosHttp.getInstance().getUsuarioLogueado();


        nombre=(TextView)v.findViewById(R.id.nombreUsuario);
        telefono=(TextView)v.findViewById(R.id.nroTel);
        localidad=(TextView)v.findViewById(R.id.nombreLocalidad);
        fotoPerfil=(ImageView)v.findViewById(R.id.imageView_perfil_usuario);
        mail=(TextView)v.findViewById(R.id.tv_mail);
        bt_editar_perfil=(Button)v.findViewById(R.id.bt_editar_perfil);

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
                intent.setClass(getContext(), EditarPerfilActivity.class);
                startActivity(intent);
            }
        });


        return v;
    }



    private  class DescargarBitMap extends AsyncTask<String,Integer, Bitmap> {
        Bitmap bm = null;


        protected Bitmap doInBackground(String... url) {
            try {
                Log.d("URL", url[0]);
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
