package com.example.lucila.myapplication.Datos;

import android.support.annotation.NonNull;

import com.example.lucila.myapplication.Entidades.Deporte;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by tino on 22/05/2016.
 * esta  clase se utiliza para obtener un deporte a partir del id
 */
public class MapeoIdDeporte {

    private   Map<Long,Deporte> mapeo;

    public MapeoIdDeporte(){
             mapeo= new HashMap<Long, Deporte>();

    }

    public  Deporte getDeporte(long id){

       return  mapeo.get(id);
    }

    public  void  insert(Long id, Deporte deporte){

        mapeo.put(id,deporte);
    }

}
