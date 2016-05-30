package com.example.lucila.turnosPP.beans;

import java.io.Serializable;

public class Establecimiento implements Serializable {

    private int id, telefono;
    private String nombre, token, ubicacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
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

    public Establecimiento() {}

    public Establecimiento(int id, String token, String nombre, String ubicacion, int telefono) {
        this.id = id;
        this.token = token;
        this.telefono = telefono;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }
}
