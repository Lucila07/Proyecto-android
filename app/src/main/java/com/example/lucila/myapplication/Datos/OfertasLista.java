package com.example.lucila.myapplication.Datos;
import com.example.lucila.myapplication.Entidades.Oferta;

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

    public static List<Oferta> randomList(int count) {
        Random random = new Random();
        List<Oferta> items = new ArrayList<>();

        // Restricción de tamaño
       int numDep = random.nextInt(4);

        while (items.size() < 10) {
            Oferta oferta=new Oferta(deportes[numDep]);
            Date fecha= new Date(2015/5/3);
            Long hora= new Long(15);
            items.add(oferta);
        }

        return new ArrayList<>(items);
    }
}
