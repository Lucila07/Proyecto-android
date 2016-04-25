package com.example.lucila.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText user, pass;
    private CheckBox recordarmeChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if(yaLogueado()) {
            //TODO login permanente
        }
        else {
            //Obtengo componentes a utilizar en esta aplicación
            user= (EditText) findViewById(R.id.edit_text_usuario);
            pass= (EditText) findViewById(R.id.edit_text_contra);
            recordarmeChk= (CheckBox) findViewById(R.id.checkbox_recordame);

        }
        //TODO Server conection
    }

    public void login(View view) {
        //TODO Server verification
        TextView msjError= (TextView) findViewById(R.id.edit_text_error);
        String usuario, contra;
        usuario= user.getText().toString();
        contra= pass.getText().toString();
        if(usuario.isEmpty() || contra.isEmpty()) //Sanitización de entrada
            msjError.setVisibility(EditText.VISIBLE);
        else
            if(!validar(usuario,contra)) { //Si falla la validación
                msjError.setVisibility(EditText.VISIBLE);
                pass.setText("");
            }
            else { //El user se loguea correctamente
                msjError.setVisibility(EditText.INVISIBLE);
                //TODO iniciar la actividad de menu principal
            }

    }

    public void registrarse(View view) {
        //TODO iniciar la activadad de registrarse
        Intent registrarse= new Intent(this,RegistrarseActivity.class);
        startActivity(registrarse);
    }

    //Métodos privados
    private boolean validar(String usuario, String contra) {
        //TODO validación con el server
        return false;
    }

    private boolean yaLogueado() {
        return false;
    }
}
