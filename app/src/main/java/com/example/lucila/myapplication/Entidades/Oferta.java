package com.example.lucila.myapplication.Entidades;

import java.util.Date;

/**
 * Created by Agustin on 30/04/2016.
 * Representa las ofertas
 * */

 public class Oferta {
    private Long codigo;
    private int deporte;
    private String estado;
    private String fecha;

    private String hora;
    private long idUserComprador;

    private long idUserCreador;

    private int precioHabitual;
    private int precioOferta;

  //  private String ubicacion;

    public Oferta() {

    }

    public long getIdUserCreador() {
        return idUserCreador;
    }

    public void setIdUserCreador(long idUserCreador) {
        this.idUserCreador = idUserCreador;
    }

    public long getIdUserComprador() {
        return idUserComprador;
    }

    public void setIdUserComprador(long idUserComprador) {
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





    public int getDeporte() {
        return deporte;
    }

    public void setDeporte(int deporte) {
        this.deporte = deporte;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
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



    public String getUbicacion() {
        return "bahia blanca";
    }

    public void setUbicacion(String ubicacion) {
       // this.ubicacion = ubicacion;
    }

}
