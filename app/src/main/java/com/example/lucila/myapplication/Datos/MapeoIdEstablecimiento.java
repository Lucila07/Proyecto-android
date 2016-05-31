package com.example.lucila.myapplication.Datos;

import com.example.lucila.myapplication.Entidades.Establecimiento;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tino on 31/05/2016.
 * esta clase representa el mapeo entre un id y un establecimiento
 */
public class MapeoIdEstablecimiento {

    private static Map<Long,Establecimiento> mapeo;

    private static  MapeoIdEstablecimiento instancia;

    private MapeoIdEstablecimiento(){

        mapeo=new HashMap<Long, Establecimiento>();
    }

    public static MapeoIdEstablecimiento getInstance(){

        if(instancia==null){

            instancia= new MapeoIdEstablecimiento();
        }
        return instancia;
    }

    public void put(Long id, Establecimiento establecimiento){

        mapeo.put(id,establecimiento);
    }
    public  Establecimiento getById(Long id){
        return mapeo.get(id);

    }
}
