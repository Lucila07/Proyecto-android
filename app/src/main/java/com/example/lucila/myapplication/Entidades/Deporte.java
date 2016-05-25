package com.example.lucila.myapplication.Entidades;

/**
 * Created by tino on 15/05/2016.
 */
public class Deporte {


    private String nombre;
    private int idFoto;
    private long idDeporte;
    //private int idFoto;



    public Deporte(String nombre){
        this.nombre=nombre;

    }

    public int getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(int idFoto) {
        this.idFoto = idFoto;
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
