package com.example.lucila.turnosPP.beans;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Establecimiento implements Serializable {

    private int id;
    private int telefono;
    private int cantMaxOfertas;
    private String nombre;
    private String token;
    private String ubicacion;
    private String email;
    private ArrayList<String> deportes= new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCantMaxOfertas() {
        return cantMaxOfertas;
    }

    public void setCantMaxOfertas(int cantMaxOfertas) {
        this.cantMaxOfertas = cantMaxOfertas;
    }

    public ArrayList<String> getDeportes() {
        return deportes;
    }

    public void setDeportes(ArrayList<String> deportes) {
        this.deportes = deportes;
    }

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

    public Establecimiento() {
        id= 0;
        telefono= 0;
        cantMaxOfertas= 0;
        nombre= "";
        token= "";
        ubicacion= "";
        email= "";
    }

    public Establecimiento(int id, String token, String nombre, String ubicacion, int telefono) {
        this.id = id;
        this.token = token;
        this.telefono = telefono;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Establecimiento(int id, int telefono, String nombre, String token, String ubicacion, String email, ArrayList<String> deportes) {
        this.id = id;
        this.telefono = telefono;
        this.nombre = nombre;
        this.token = token;
        this.ubicacion = ubicacion;
        this.email = email;
        this.deportes = deportes;
    }
}
