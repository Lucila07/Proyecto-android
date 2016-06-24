package com.example.lucila.turnosPP.beans;

import java.io.Serializable;

public class Pack implements Serializable {
    private int id;
    private String tipo;
    private int cantidadOfertas;
    private String descripcion;
    private float precio;

    public Pack(int id, String tipo, int cantidadOfertas, String descripcion, float precio) {
        this.id = id;
        this.tipo = tipo;
        this.cantidadOfertas = cantidadOfertas;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCantidadOfertas() {
        return cantidadOfertas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getPrecio() {
        return precio;
    }
}
