package com.example.lucila.myapplication.Entidades;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tino on 15/05/2016.
 */
public class Deporte implements Parcelable{
    private long idDeporte;
    private String nombre;
    //private int idFoto;


    public Deporte(String nombre){
        this.nombre=nombre;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(idDeporte);
        dest.writeString(nombre);
    }
    public static final Parcelable.Creator<Deporte> CREATOR
            = new Parcelable.Creator<Deporte>() {
        public Deporte createFromParcel(Parcel in) {
            return new Deporte(in);
        }

        public Deporte[] newArray(int size) {
            return new Deporte[size];
        }
    };
    public Deporte(Parcel in){
        this.idDeporte=(long)in.readLong();
        this.nombre=(String)in.readString();
    }

    public long getIdDeporte() {
        return idDeporte;
    }

    public void setIdDeporte(long idDeporte) {
        this.idDeporte = idDeporte;
    }

    /*  public int getIdFoto() {
            return idFoto;
        }

        public void setIdFoto(int idFoto) {
            this.idFoto = idFoto;
        }
        */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
