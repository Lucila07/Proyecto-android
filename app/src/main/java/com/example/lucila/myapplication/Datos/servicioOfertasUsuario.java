package com.example.lucila.myapplication.Datos;

import android.app.Activity;
import android.os.Parcelable;

import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.ReservaOfertaActivity;

import java.util.List;

/**
 * Created by tino on 15/05/2016.
 * este servicio es el encargado de administrar las ofertas
 */
public interface ServicioOfertasUsuario  {


    /**
     * relaciona una oferta a un usuario
     * @return retorna true si se concreto correctamente la operacion
     *          retorna false si la operacion no pudo ser concretada
     * */
    public  Boolean reservarOferta(Oferta oferta , Usuario user);

    /**
     * retorna la lista de TODAS las ofertas cuyo estado es para reseervar
     * **/
    public  List<Oferta>getOfertas();

    /**
     * retorna la lista de las ofertas por ubicacion cuyo estado es para reseervar
     * */
    public List<Oferta>getOfertasUbicacion(String ubicacion);

    /**
     * retorna la lista de las ofertas por ubicacion cuyo estado es para reseervar
     * */
    public List<Oferta>getOfertasDeporte(Deporte deporte);


    /**
     * retorna una oferta que contenga el codigo
     * @param  codigo del deporte
     * */
    public Oferta getOfertaCodigo(Long codigo);

    /**
     * retorna la lista de deportes disponibles en el sistema
     * */
    public List<Deporte> getDeportes();

    /**
     * retorna la cantidad de deportes que hay creados en el sistemas
     * */
    public  int cantidadDeportes();


    /**
     * retorna el deporte asociado al nombre
     * @para nombre : nombre del deporte
     * */
    public  Deporte getDeporte(String id);

    /*
      realiza la peticion de las ofertas y deportes al servidor
      */
    public void realizarPeticion();
}
