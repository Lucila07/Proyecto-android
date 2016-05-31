package com.example.lucila.myapplication.Entidades;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tino on 15/05/2016.
 * Esta entidad representa al usuario de la aplicacion que puede reservar ofertas y armar partidos
 */

public class Usuario {

    private String ubicacion;
    private String nombreApellido;

    private String email;
    private String telefono;
    private int faltas;
    private String idUsuario; //id para la bd
    private List<Oferta>ofertasReservadas;
    private Uri urlFoto;

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

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombre) {
        this.nombreApellido = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public  List<Oferta> getOfertasReservadas(){

        return ofertasReservadas;
    }

    public Uri getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(Uri urlFoto) {
        this.urlFoto = urlFoto;
    }
}
