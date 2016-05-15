package com.example.lucila.myapplication.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tino on 15/05/2016.
 */
public class Establecimiento {

    private String nombre;
    private String ubicacion;
    private List<Deporte>deportes;
    private int telefono;
    private List<Oferta>ofertasActuales;//ofertas que creo y pueden ser reservadas
    private List<Oferta>ofertasConcreatadas;
    //private int cantidadOfertasDisponibles;//cantidad de ofertas que puede realizar


    public Establecimiento(String nombre, int telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
        ofertasActuales=new ArrayList<Oferta>();
        ofertasConcreatadas= new ArrayList<Oferta>();

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

    public List<Deporte> getDeportes() {
        return deportes;
    }



    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public List<Oferta> getOfertasActuales() {
        return ofertasActuales;
    }

    public  int cantidadOfertasDisponibles(){

        return getOfertasActuales().size();
    }

    public List<Oferta> getOfertasConcreatadas() {
        return ofertasConcreatadas;
    }



    public void agreagarOfertaConcreatada(Oferta oferta) {
        this.ofertasConcreatadas.add( oferta);
        ofertasActuales.remove(oferta);
    }



    public void agregarOfertaActual(Oferta ofertasActuale) {
        ofertasActuales.add( ofertasActuale);
    }
    public void agregarDeporte(Deporte deporte) {
        this.deportes.add( deporte);
    }

    public  void removerDeporte(Deporte deporte){

        this.deportes.remove(deporte);
    }
}
