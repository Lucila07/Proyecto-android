package com.example.lucila.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class RegistrarseActivity extends AppCompatActivity {

    private static final String BLANCO= "";

    private EditText campoUser,
                     campoContra,
                     campoTel,
                     campoUbi,
                     campoRepetirContra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse_activity);

        //Obtengo los EditText
        campoUser= (EditText) findViewById(R.id.user_textfield_registrarse);
        campoContra= (EditText) findViewById(R.id.contra_textfield_registrarse);
        campoRepetirContra= (EditText) findViewById(R.id.repetir_contra_textfield_registrarse);
        campoTel= (EditText) findViewById(R.id.telefono_textfield_registrarse);
        campoUbi= (EditText) findViewById(R.id.ubicacion_textfield_registrarse);
    }

    public void obtenerUbicacion() {
        //TODO intent implícito al gps para obtener la ubicacion
    }

    public void cancelar() {
        resetearCampos();
    }

    public void registrarse() {
        validacionEntrada();
        //TODO agregar a la base de datos el nuevo usuario
    }

    //Valida los datos de los input text.
    private void validacionEntrada() {
        //TODO Validacion de datos de los campos de la pantalla Registrarse
    }

    private void resetearCampos() {
        campoUser.setText(BLANCO);
        campoContra.setText(BLANCO);
        campoRepetirContra.setText(BLANCO);
        campoTel.setText(BLANCO);
        campoUbi.setText(BLANCO);
    }
}
