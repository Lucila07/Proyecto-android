package com.example.lucila.myapplication.Datos;

import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;

import java.util.List;

/**
 * Created by tino on 15/05/2016.
 * este servicio es el encargado de administrar las ofertas
 */
public interface ServicioOfertasUsuario {


    /**
     * relaciona una oferta a un usuario
     * @return retorna true si se concreto correctamente la operacion
     *          retorna false si la operacion no pudo ser concretada
     * */
    public boolean reservarOferta(Oferta oferta , Usuario user);

    /**
     * retorna la lista de TODAS las ofertas cuyo estado es para reseervar
     * **/
    public List<Oferta>getOfertas();

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
     * */
    public Oferta getOferta(Long codigo);
}
