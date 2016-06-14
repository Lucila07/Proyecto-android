package com.example.lucila.turnosPP.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Representa una oferta dentro de la aplicación.
 */
public final class Oferta implements Serializable {

    private int codigo, idEstab;
    private String idComprador;
    private int deporte;
    private float precioHabitual, precioFinal;
    private String estado;
    private Date fecha;
    private String nombreDeporte;

    public Oferta(){}

    public Oferta(int codigo, int idEstab, String idComprador, int idDeporte, float precioHabitual, float precioFinal, String estado, Date fecha) {
        this.codigo = codigo;
        this.idEstab = idEstab;
        this.idComprador = idComprador;
        this.deporte = idDeporte;
        this.precioHabitual = precioHabitual;
        this.precioFinal = precioFinal;
        this.estado = estado;
        this.fecha = fecha;
        nombreDeporte= "";
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getIdEstab() {
        return idEstab;
    }

    public void setIdEstab(int idEstab) {
        this.idEstab = idEstab;
    }

    public String getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(String idComprador) {
        this.idComprador = idComprador;
    }

    public int getIdDeporte() {
        return deporte;
    }

    public void setIdDeporte(int idDeporte) {
        this.deporte = idDeporte;
    }

    public float getPrecioHabitual() {
        return precioHabitual;
    }

    public void setPrecioHabitual(float precioHabitual) {
        this.precioHabitual = precioHabitual;
    }

    public float getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(float precioFinal) {
        this.precioFinal = precioFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Calendar getFecha() {
        Calendar toR= Calendar.getInstance();
        toR.setTime(fecha);
        return toR;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha.getTime();
    }

    public String getNombreDeporte() {
        return nombreDeporte;
    }

    public void setNombreDeporte(String deporte) {
        this.nombreDeporte = deporte;
    }
}
