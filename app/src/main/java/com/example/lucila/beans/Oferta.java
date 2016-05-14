package com.example.lucila.beans;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Representa una oferta dentro de la aplicación.
 */
public final class Oferta implements Serializable {

    private GregorianCalendar fecha;
    private String deporte;
    private float precioFinal, precioHabitual;

    public Oferta() {}

    public Oferta(GregorianCalendar fecha, String deporte, float precioFinal, float precioHabitual) {
        this.fecha = fecha;
        this.deporte = deporte;
        this.precioFinal = precioFinal;
        this.precioHabitual = precioHabitual;
    }

    public GregorianCalendar getFecha() {
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
