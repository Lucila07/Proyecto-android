package com.example.lucila.myapplication;

import android.content.Intent;
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
    private Button boton_crear_usuario;

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
                servicioUsuarios.crearUsuario(nombreUsuario,mail,id,telefono);
                Log.d("nuevo usuario", "se creo el usuario");
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
}
