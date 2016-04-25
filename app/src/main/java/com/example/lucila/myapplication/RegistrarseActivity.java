package com.example.lucila.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RegistrarseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse_activity);
    }

    public void obtenerUbicacion() {
        //TODO intent implicito al gps para obtener la ubicacion
    }

    public void cancelar() {

    }

    public void registrarse() {
        validacionEntrada();
        //TODO agregar a la base de datos el nuevo usuario
    }

    //Valida los datos de los input text.
    private void validacionEntrada() {

    }
}
