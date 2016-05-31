package com.example.lucila.myapplication.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tino on 15/05/2016.
 */
public class Establecimiento {

    private String nombre;
    private String ubicacion;
    private String telefono;
    private  long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Establecimiento() {


    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
