package com.example.lucila.myapplication.Datos;

import android.os.Parcel;
import android.os.Parcelable;
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
public class MapeoIdDeporte implements Parcelable{

    private   Map<Long,Deporte> mapeo;

    public MapeoIdDeporte(){
             mapeo= new HashMap<Long, Deporte>();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mapeo);

    }

    public static final Parcelable.Creator<MapeoIdDeporte> CREATOR
            = new Parcelable.Creator<MapeoIdDeporte>() {
        public MapeoIdDeporte createFromParcel(Parcel in) {
            return new MapeoIdDeporte(in);
        }

        public MapeoIdDeporte[] newArray(int size) {
            return new MapeoIdDeporte[size];
        }
    };
    public  MapeoIdDeporte(Parcel in){

        mapeo=(Map<Long,Deporte>)in.readValue(Map.class.getClassLoader());
    }

    public  Deporte getDeporte(long id){

       return  mapeo.get(id);
    }

    public  void  insert(Long id, Deporte deporte){

        mapeo.put(id,deporte);
    }

}
