package com.example.lucila.myapplication.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tino on 15/05/2016.
 * Esta entidad representa al usuario de la aplicacion que puede reservar ofertas y armar partidos
 */

public class Usuario {

    private String ubicacion;
    private String nombre;
    private String apellido;
    private String email;
    private int telefono;
    private int faltas;
    private int idUsuario; //id para la bd
    private List<Oferta>ofertasReservadas;

    public Usuario(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
        ofertasReservadas= new ArrayList<>();
    }

    public Usuario(String email){

        this.email=email;
        ofertasReservadas= new ArrayList<>();
    }
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }




}
