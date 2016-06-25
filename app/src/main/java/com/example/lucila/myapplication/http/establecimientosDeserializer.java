package com.example.lucila.myapplication.http;


import com.example.lucila.myapplication.Entidades.Establecimiento;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by tino on 31/05/2016.
 */
public class EstablecimientosDeserializer implements JsonDeserializer<Establecimiento> {

    public Establecimiento deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String  nombre = jsonObject.get("nombre").getAsString();
        String ubicacion =jsonObject.get("ubicacion").toString();
        String telefono="";
        String direccion =jsonObject.get("direccion").toString();
        long id=jsonObject.get("id").getAsLong();

        if(jsonObject.get("telefono")!=null)
            telefono =jsonObject.get("telefono").toString();

        Establecimiento establecimiento= new Establecimiento();
        //setters
        establecimiento.setId(id);
        establecimiento.setUbicacion(ubicacion);
        establecimiento.setNombre(nombre);
        establecimiento.setTelefono(telefono);
        establecimiento.setDireccion(direccion);

        return  establecimiento;
    }
}
