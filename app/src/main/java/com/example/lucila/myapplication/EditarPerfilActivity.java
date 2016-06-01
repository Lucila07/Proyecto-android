package com.example.lucila.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Usuario;

public class EditarPerfilActivity extends AppCompatActivity implements ServicioUsuariosHttp.AccesoUsuarios {

    private  Toolbar toolbar;
    private EditText telefono,nombre;
    private Button boton;
    private Usuario usuario;
    private ServicioUsuariosHttp servicioUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        setToolbar();
        servicioUsuarios=ServicioUsuariosHttp.getInstance();
        usuario=servicioUsuarios.getUsuarioLogueado();

        telefono=(EditText)findViewById(R.id.editar_telefono);
        nombre=(EditText)findViewById(R.id.editar_nombre);

        telefono.setHint(usuario.getTelefono());
        nombre.setHint(usuario.getNombreApellido());

        boton= (Button)findViewById(R.id.bt_guardar_cambios_perfil);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nuevoTel= telefono.getText().toString();
                String nuevoNom=nombre.getText().toString();
                if(!nuevoTel.equals(usuario.getTelefono())||!nuevoNom.equals(usuario.getNombreApellido())){

                    servicioUsuarios.editarPerfil(usuario.getIdUsuario(),nuevoNom,nuevoTel);
                }
                else{

                    Toast.makeText(EditarPerfilActivity.this, "Debes modificar almenos un cambio ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setToolbar(){
        //toolbar-------------
        toolbar = (Toolbar) findViewById(R.id.toolbar_editar_perfil); //encontramos la instancia de la toolbar
        setSupportActionBar(toolbar);   //la setamos a la actividad
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //este muestra el boton de volver
        getSupportActionBar().setTitle("Edita tu perfil");
        getSupportActionBar().setDisplayShowTitleEnabled(true);



    }

    /**
     * caso de exito
     * */
    @Override
    public void cargarMain() {
        Toast.makeText(EditarPerfilActivity.this, "Sus datos fueron editados con exito", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(EditarPerfilActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * caso de fallo
     * */
    @Override
    public void cargarTelefono() {
        Toast.makeText(EditarPerfilActivity.this, "Ocurrio un error al editar los datos", Toast.LENGTH_SHORT).show();
    }
}
