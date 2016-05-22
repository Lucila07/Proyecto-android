package com.example.lucila.myapplication.http;

import com.example.lucila.myapplication.Entidades.Oferta;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by tino on 22/05/2016.
 */
public class ofertaDeserializer  implements JsonDeserializer<Oferta> {

    @Override
    public Oferta deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
         JsonObject jsonObject = json.getAsJsonObject();
        long idUserComprador=-1;
        long  codigo  = jsonObject.get("codigo").getAsLong();

         int deporte = jsonObject.get("deporte").getAsInt();
         String estado = jsonObject.get("estado").getAsString();
         String fecha =jsonObject.get("fecha").getAsString();
         String hora =jsonObject.get("hora").getAsString();

          String   aux= jsonObject.get("idUserComprador").toString();

        long idUserCreador =jsonObject.get("idUserCreador").getAsLong();
        int precioHabitual =jsonObject.get("precioHabitual").getAsInt();
        int precioOferta =jsonObject.get("precioOferta").getAsInt();

        Oferta oferta= new Oferta();
        oferta.setCodigo(codigo);
        oferta.setDeporte(deporte);
        oferta.setEstado(estado);
        oferta.setFecha(fecha);
        oferta.setHora(hora);

        oferta.setIdUserCreador(idUserCreador);
        oferta.setPrecioHabitual(precioHabitual);
        oferta.setPrecioOferta(precioOferta);
        if(!aux.equals("null")) {
            idUserComprador=Long.parseLong(aux);
            oferta.setIdUserComprador(idUserComprador);
        }
        return  oferta;
    }
}
