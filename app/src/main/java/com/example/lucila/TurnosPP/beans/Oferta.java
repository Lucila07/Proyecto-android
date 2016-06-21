package com.example.lucila.turnosPP.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Representa una oferta dentro de la aplicación.
 */
public final class Oferta implements Serializable {

    private int codigo;
    private int idUserCreador;
    private String idComprador;
    private int deporte;
    private float precioHabitual, precioOferta;
    private String estado;
    private Date fechaHora;
    private String nombreDeporte;

    public Oferta(){}

    public Oferta(int codigo, int idEstab, String idComprador, int idDeporte, float precioHabitual, float precioFinal, String estado, Date fecha) {
        this.codigo = codigo;
        this.idUserCreador = idEstab;
        this.idComprador = idComprador;
        this.deporte = idDeporte;
        this.precioHabitual = precioHabitual;
        this.precioOferta = precioFinal;
        this.estado = estado;
        this.fechaHora = fecha;
        nombreDeporte= "";
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getIdEstab() {
        return idUserCreador;
    }

    public void setIdEstab(int idEstab) {
        this.idUserCreador = idEstab;
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
        return precioOferta;
    }

    public void setPrecioFinal(float precioFinal) {
        this.precioOferta = precioFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Calendar getFecha() {
        Calendar toR= Calendar.getInstance();
        toR.setTime(fechaHora);
        return toR;
    }

    public void setFecha(Calendar fecha) {
        this.fechaHora = fecha.getTime();
    }

    public String getNombreDeporte() {
        return nombreDeporte;
    }

    public void setNombreDeporte(String deporte) {
        this.nombreDeporte = deporte;
    }
}
