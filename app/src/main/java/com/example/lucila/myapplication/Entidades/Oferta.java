package com.example.lucila.myapplication.Entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Agustin on 30/04/2016.
 * Representa las ofertas
 * */

 public class Oferta implements Parcelable {

    private Long codigo;
    private Deporte deporte;
    private String estado;
    private String fecha;

    private String hora;
    private String  idUserComprador;

    private long idUserCreador;

    private int precioHabitual;
    private int precioOferta;

    private String ubicacion;

    public Oferta() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(codigo);
        dest.writeValue(deporte);
        dest.writeString(fecha);
        dest.writeString(estado);
        dest.writeString(hora);
        dest.writeString(idUserComprador);
        dest.writeLong(idUserCreador);
        dest.writeInt(precioHabitual);
        dest.writeInt(precioOferta);
        dest.writeString(ubicacion);
    }

    public static final Parcelable.Creator<Oferta> CREATOR
            = new Parcelable.Creator<Oferta>() {
        public Oferta createFromParcel(Parcel in) {
            return new Oferta(in);
        }

        public Oferta[] newArray(int size) {
            return new Oferta[size];
        }
    };

    public Oferta(Parcel in){
        this.codigo=in.readLong();
        this.deporte=(Deporte)in.readValue(Deporte.class.getClassLoader());
        this.fecha= (String)in.readString();
        this.estado=(String)in.readString();
        this.hora=(String)in.readString();
        this.idUserComprador=(String) in.readString();
        this.idUserCreador=(int)in.readInt();
        this.precioHabitual=(int)in.readInt();
        this.precioOferta=(int)in.readInt();
        this.ubicacion=(String)in.readString();

    }

    public long getIdUserCreador() {
        return idUserCreador;
    }

    public void setIdUserCreador(long idUserCreador) {
        this.idUserCreador = idUserCreador;
    }

    public String getIdUserComprador() {
        return idUserComprador;
    }

    public void setIdUserComprador(String idUserComprador) {
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





    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
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
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }



}
