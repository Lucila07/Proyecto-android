package com.example.lucila.myapplication;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class RegistrarseActivity extends AppCompatActivity {

    private static final String VACIO= "";

    private EditText campoUser,
                     campoContra,
                     campoTel,
                     campoUbi,
                     campoRepetirContra,
                     campoEMail;

    private TextView errorCampoEMail,
                     errorCampoUser,
                     errorCampoContra,
                     errorCampoRepetirContra,
                     errorCampoTel;

    private Pattern patronEMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse_activity);

        //Usado para validar la estructura de los e-mails ingresado por el usuario.
        patronEMail= Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        //Obtengo los EditText
        campoUser= (EditText) findViewById(R.id.user_textfield_registrarse);
        campoContra= (EditText) findViewById(R.id.contra_textfield_registrarse);
        campoRepetirContra= (EditText) findViewById(R.id.repetir_contra_textfield_registrarse);
        campoTel= (EditText) findViewById(R.id.telefono_textfield_registrarse);
        campoUbi= (EditText) findViewById(R.id.ubicacion_textfield_registrarse);
        campoEMail= (EditText) findViewById(R.id.email_textfield_registrarse);

        //Obtengo los TextView
        errorCampoEMail= (TextView) findViewById(R.id.error_email_textfield_registrarse);
        errorCampoContra= (TextView) findViewById(R.id.error_contra_textfield_registrarse);
        errorCampoRepetirContra= (TextView) findViewById(R.id.error_repetir_contra_textfield_registrarse);
        errorCampoUser= (TextView) findViewById(R.id.error_usuario_textfield_registrarse);
        errorCampoTel= (TextView) findViewById(R.id.telefono_textfield_registrarse);
    }

    public void obtenerUbicacion(View view) {
        //TODO intent implícito al gps para obtener la ubicacion
    }

    public void registrarse(View view) {
        boolean paso= validacionEntrada();
        if(paso) {
            //TODO agregar a la base de datos el nuevo usuario y redireccioón al menú ppal.
            resetearCampos();
            esconderErrores();
        }
    }

    //Valida los datos de los input text. Retorna true si son válidos
    private boolean validacionEntrada() {
        boolean pasa= true;
        String user= campoUser.getText().toString();
        if(user.contentEquals(VACIO)) {
            errorCampoUser.setText(R.string.error_usuario);
            errorCampoUser.setVisibility(View.VISIBLE);
            pasa = false;
        }

        String contra= campoContra.getText().toString();
        if(contra.length() < 4) {
            errorCampoContra.setText(R.string.error_contra);
            errorCampoContra.setVisibility(View.VISIBLE);
            pasa = false;
        }

        String Rcontra= campoRepetirContra.getText().toString();
        if(!contra.equals(Rcontra)) {
            errorCampoRepetirContra.setText(R.string.error_repetir_contra);
            errorCampoRepetirContra.setVisibility(View.VISIBLE);
            pasa= false;
        }

        String email= campoEMail.getText().toString();
        if(!email.matches(patronEMail.pattern())) {
            errorCampoEMail.setText(R.string.error_email);
            errorCampoEMail.setVisibility(View.VISIBLE);
            pasa=false;
        }
        return pasa;
    }

    private void resetearCampos() {
        campoEMail.setText(VACIO);
        campoUser.setText(VACIO);
        campoContra.setText(VACIO);
        campoRepetirContra.setText(VACIO);
        campoTel.setText(VACIO);
        campoUbi.setText(VACIO);
    }

    private void esconderErrores() {
        errorCampoEMail.setVisibility(View.INVISIBLE);
        errorCampoRepetirContra.setVisibility(View.INVISIBLE);
        errorCampoUser.setVisibility(View.INVISIBLE);
        errorCampoUser.setVisibility(View.INVISIBLE);
        errorCampoTel.setVisibility(View.INVISIBLE);
    }
}
