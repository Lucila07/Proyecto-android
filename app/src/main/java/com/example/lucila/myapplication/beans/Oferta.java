package com.example.lucila.myapplication.beans;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Representa una oferta dentro de la aplicación.
 */
public final class Oferta implements Serializable {

    public String idOf,idEstab, idComprador,idDeporte, precioHabitual, precioFinal, estado, fecha, hora;
    /*
    private String fecha;
    private String deporte;
    private float precioFinal, precioHabitual;
*/
    public Oferta() {}

    public Oferta(String idOf, String idEstab, String idComprador, String idDeporte, String precioHabitual, String precioFinal, String fecha, String hora) {
/*
        this.fecha= fecha;
        this.deporte = idDeporte;
        this.precioFinal = Float.parseFloat(precioFinal);
        this.precioHabitual = Float.parseFloat(precioHabitual);*/
    }

    public static Oferta newInstance() {
        Oferta toR= new Oferta();
        return toR;
    }
/*
    public Calendar getFecha() {
        return fecha;
    }

    public String getDeporte() {
        return deporte;
    }

    public float getPrecioFinal() {
        return precioFinal;
    }

    public float getPrecioHabitual() {
        return precioHabitual;
    }
    */
}
