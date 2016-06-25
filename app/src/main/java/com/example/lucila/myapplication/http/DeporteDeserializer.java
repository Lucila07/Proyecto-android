package com.example.lucila.myapplication.http;

import com.example.lucila.myapplication.Entidades.Deporte;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by tino on 22/05/2016.
 */

public class DeporteDeserializer implements JsonDeserializer<Deporte> {

    @Override
    public Deporte deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        long idDeporte = jsonObject.get("id").getAsLong();
        String nombre =jsonObject.get("nombre").getAsString();

        Deporte d= new Deporte(nombre);
        d.setIdDeporte(idDeporte);
        return  d;
    }
}
