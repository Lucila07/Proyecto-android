package com.example.lucila.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.http.VerificaConexion;

public class EditarPerfilActivity extends AppCompatActivity implements ServicioUsuariosHttp.AccesoUsuarios {

    private  Toolbar toolbar;
    private EditText telefono,nombre;
    private Button boton;
    private Usuario usuario;
    private Activity activity=this;
    private ServicioUsuariosHttp servicioUsuarios;
    private String nuevoTel;
    private  String nuevoNom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        setToolbar();
        servicioUsuarios=ServicioUsuariosHttp.getInstance(this,this);
        usuario=servicioUsuarios.getUsuarioLogueado();

        telefono=(EditText)findViewById(R.id.editar_telefono);
        nombre=(EditText)findViewById(R.id.editar_nombre);

        telefono.setText(usuario.getTelefono());
        nombre.setText(usuario.getNombreApellido());

        nuevoNom=" ";
        nuevoTel=" ";

        boton= (Button)findViewById(R.id.bt_guardar_cambios_perfil);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  nuevoTel= telefono.getText().toString();
                   nuevoNom=nombre.getText().toString();
                  String telViejo=usuario.getTelefono();
                  if(telViejo==null)
                      telViejo=new String(" ");

                if(nuevoTel.equals(telViejo)&&nuevoNom.equals(usuario.getNombreApellido()))
                    generarDialogoFallo( "Debes realizar almenos un cambio ");
                else {

                    if (cheaquerDatoTel(nuevoTel)) {
                        if (cheaquerDato(nuevoNom)) {
                            if (VerificaConexion.hayConexionInternet(activity)) {
                                servicioUsuarios.editarPerfil(usuario.getIdUsuario(), nuevoNom, nuevoTel);
                            }
                            else {
                                Toast.makeText(EditarPerfilActivity.this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            generarDialogoFallo(" No se permiten Nombres vacios, por favor chequea tus datos");
                    }
                    else
                        generarDialogoFallo(" Formato del telefono incorrecto");
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
        usuario.setTelefono(nuevoTel);
        usuario.setNombreApellido(nuevoNom);
        Toast.makeText(EditarPerfilActivity.this, "Sus datos fueron editados con exito", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(EditarPerfilActivity.this, PerfilActivity.class);
        startActivity(intent);
    }

    /**
     * caso de fallo
     * */
    @Override
    public void cargarTelefono() {
        Toast.makeText(EditarPerfilActivity.this, "Ocurrio un error al editar los datos", Toast.LENGTH_SHORT).show();
    }

    private boolean cheaquerDato(String dato){
        if(dato.isEmpty()||dato.equals("")||dato.equals(" ")||dato==null)
            return false;
        else return true;
}
    private boolean cheaquerDatoTel(String tel){
        if(tel==null||tel.isEmpty()||tel.equals("")||tel.equals(" ")||tel.length()<4){
            return false;
        }
        else return true;

    }
    private void generarDialogoFallo(String mensaje){
        AlertDialog dialogo= new AlertDialog.Builder(EditarPerfilActivity.this)
                .setTitle("Reservar")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }}
                ).show();


    }
}
