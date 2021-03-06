package com.example.lucila.myapplication.http;



import com.example.lucila.myapplication.Datos.MapeoIdDeporte;
import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;


/**
 * Created by tino on 22/05/2016.
 */
public class OfertaDeserializer implements JsonDeserializer<Oferta> {
    private  MapeoIdDeporte mapeo;

    public OfertaDeserializer(MapeoIdDeporte m){

        mapeo=m;
    }
    @Override
    public Oferta deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();
        String idUserComprador;
        long  codigo  = jsonObject.get("codigo").getAsLong();
        int deporteId = jsonObject.get("deporte").getAsInt();
        String estado = jsonObject.get("estado").getAsString();
        String fecha =jsonObject.get("fecha").getAsString();
        String hora =jsonObject.get("hora").getAsString();
        idUserComprador= jsonObject.get("idUserComprador").toString();
        long idUserCreador =jsonObject.get("idUserCreador").getAsLong();

        float auxPH=jsonObject.get("precioHabitual").getAsFloat();
        int precioHabitual =Math.round(auxPH);
        float auxPO=jsonObject.get("precioOferta").getAsFloat();
        int precioOferta =Math.round(auxPO);

        String ubicacion=jsonObject.get("ubicacion").toString();
       if(ubicacion.length()>2)
      ubicacion=ubicacion.substring(1,ubicacion.length()-1);

        Oferta oferta= new Oferta();
        oferta.setCodigo(codigo);
        //obtenemos el deporte del mapeo a partir del id
        Deporte deporte=mapeo.getDeporte(deporteId);

        oferta.setDeporte(deporte); //establecemos el deporte como objeto y no como id

        oferta.setEstado(estado);
        oferta.setFecha(fecha);
        oferta.setHora(hora);

        oferta.setIdUserCreador(idUserCreador);
        oferta.setPrecioHabitual(precioHabitual);
        oferta.setPrecioOferta(precioOferta);
        oferta.setIdUserComprador(idUserComprador);

        if(ubicacion==null||ubicacion.isEmpty())
            oferta.setUbicacion("no disponible");
        else
            oferta.setUbicacion(ubicacion);
        return  oferta;
    }
}
