package com.example.lucila.myapplication.Datos;

import com.example.lucila.myapplication.R;

/**
 * Created by Lucila on 22/05/2016.
 */
public class ItemData {


    private int deporte;
    private int imageUrl;
    private String fecha;
    private String lugar;
    private String hora;


    // completar con el resto
    public ItemData(int deporte,String lugar, String fecha, String hora){

        this.deporte = deporte;
        this.lugar=lugar;
        this.fecha=fecha;
        this.hora=hora;
        switch (deporte){
            case 0:{ this.imageUrl= R.drawable.ball;
                break;}//futboll
            case 1:{
                this.imageUrl= R.drawable.tennis;

                break;}//
            case 2:{this.imageUrl= R.drawable.basketball;

                break;}//Basket


            default: this.imageUrl=R.drawable.fcb; // corregir
        }

    }

    public void setDeporte(int deporte) {
        this.deporte = deporte;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDeporte() {
        String deporte="Deporte";
        switch(this.deporte)
        {
            case 0:{deporte="Fútbol"; break;}
            case 1:{deporte="Tennis"; break;}
            case 2:{deporte="Basket"; break;}

        }
        return deporte;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public String getFecha() {
        return fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public String getHora() {
        return hora;
    }
    // getters & setters

}
