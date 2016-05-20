package com.example.lucila.myapplication.beans;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Representa una oferta dentro de la aplicación.
 */
public final class Oferta implements Serializable {

    private Calendar fecha;
    private String deporte;
    private float precioFinal, precioHabitual;

    public Oferta() {}

    public Oferta(Calendar fecha, String deporte, float precioFinal, float precioHabitual) {
        this.fecha = fecha;
        this.deporte = deporte;
        this.precioFinal = precioFinal;
        this.precioHabitual = precioHabitual;
    }

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
}
