package com.example.lucila.myapplication.Entidades;

import java.util.Date;

/**
 * Created by Agustin on 30/04/2016.
 * Representa las ofertas
 * */

 public class Oferta {

    private Deporte deporte;
    private Date fecha;
    private Long hora;
    private Long codigo;
    private String estado;
    private int idUserCreador;
    private int idUserComprador;
    private int precioHabitual;
    private int precioOferta;

    public int getIdUserCreador() {
        return idUserCreador;
    }

    public void setIdUserCreador(int idUserCreador) {
        this.idUserCreador = idUserCreador;
    }

    public int getIdUserComprador() {
        return idUserComprador;
    }

    public void setIdUserComprador(int idUserComprador) {
        this.idUserComprador = idUserComprador;
    }

    public int getPrecioHabitual() {
        return precioHabitual;
    }

    public void setPrecioHabitual(int precioHabitual) {
        this.precioHabitual = precioHabitual;
    }

    public int getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(int precioOferta) {
        this.precioOferta = precioOferta;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    private String ubicacion;

    public Oferta(Deporte deporte) {
        this.deporte = deporte;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;


    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
