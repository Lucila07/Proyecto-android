package com.example.lucila.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucila.myapplication.Datos.ServicioOfertasHttp;
import com.example.lucila.myapplication.Datos.ServicioUsuarios;
import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;

public class NuevoUsuario extends AppCompatActivity implements ServicioUsuariosHttp.AccesoUsuarios{

    private ServicioUsuarios servicioUsuarios;
    private EditText editText;
    private Button boton_crear_usuario,bt_omitir;

    private String nombreUsuario,mail,id,telefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        servicioUsuarios= ServicioUsuariosHttp.getInstance(this,this);
        Intent intent=getIntent();

        nombreUsuario=intent.getExtras().getString("nombre");
        mail=intent.getExtras().getString("mail");
        id=intent.getExtras().getString("id");

        editText=(EditText)findViewById(R.id.ed_telefono);

        boton_crear_usuario= (Button)findViewById(R.id.boton_crear_usuario);
        boton_crear_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                telefono=editText.getText().toString();
                if(telefono.isEmpty()||telefono.equals("")||telefono.equals(" ")||telefono.length()<4||telefono==null)
                    generarDialogoTelefono();

                servicioUsuarios.crearUsuario(nombreUsuario,mail,id,telefono);
                Log.d("nuevo usuario", "se creo el usuario");
            }
        });

        bt_omitir=(Button)findViewById(R.id.bt_omitir);
        bt_omitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generarDialogoTelefono();
                cargarMain();
            }
        });
    }



    @Override
    public void cargarMain() {

        Toast.makeText(NuevoUsuario.this,"Gracias por registrarse! ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(NuevoUsuario.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void cargarTelefono() {

        Toast.makeText(NuevoUsuario.this,"Ha ocurrido un error al registrase, intentelo nuevamente ", Toast.LENGTH_SHORT).show();
    }


    private void generarDialogoTelefono(){
        AlertDialog dialogo= new AlertDialog.Builder(NuevoUsuario.this)
                .setTitle("Ingresar")
                .setMessage("Para poder reservar debera ingresar su telefono \n Lo podra hacer luego en: 'Mi perfil->editar' ")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {



                    }}
                ).show();


    }
}
