package com.example.lucila.myapplication.Datos;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tino on 30/04/2016.
 * clase con datos de testing
 */
public class OfertasLista {

    public static final String[] deportes = {
          "Futbol","Voley","Ultimate","Basquet"

    };

    public static List<Oferta> randomList(int cantidad) {
        Random random = new Random();
        List<Oferta> items = new ArrayList<>();

        // Restricción de tamaño
       int numDep = random.nextInt(4);

        while (items.size() < cantidad) {
            Oferta oferta=new Oferta(deportes[numDep]);
            Date fecha= new Date(2015/5/3);
            Long hora= new Long(15);
            String ubicacion= new String("Bahia");
            oferta.setHora(hora);
            oferta.setFecha(fecha);
            oferta.setUbicacion(ubicacion);
            setFoto(oferta);
            Long codigo= new Long(random.nextLong());
            oferta.setCodigo(codigo);
            items.add(oferta);
        }

        return new ArrayList<>(items);
    }

    private static void setFoto(Oferta of){

        switch (of.getDeporte()){
            case "Futbol":
                of.setIdFoto(R.drawable.futbol);
                break;
            case "Ultimate":
                of.setIdFoto(R.drawable.ultimate);
                break;
            case "Voley":
                of.setIdFoto(R.drawable.voley);
                break;
            case "Basquet":
                of.setIdFoto(R.drawable.basquet);
                break;
        }

    }

}
