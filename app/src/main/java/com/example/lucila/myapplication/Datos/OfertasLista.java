package com.example.lucila.myapplication.Datos;
import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tino on 30/04/2016.
 * clase con datos de testing
 */
public class OfertasLista implements servicioOfertasUsuario{
   private List<Oferta> items;
    private    String[] deportes = {
            "Futbol","Voley","Ultimate","Basquet"

    };
    private    List<Deporte>listaDeportes;

    public OfertasLista (){
        listaDeportes= new ArrayList<Deporte>();
        items=getOfertas();

    }




    public  List<Oferta> getOfertas() {
        Random random = new Random();
       int cantidad=random.nextInt(15)+1;
         items = new ArrayList<>();

        // Restricción de tamaño
       int numDep = random.nextInt(4);
        for(int i=0;i<deportes.length;i++){
            Deporte dep= new Deporte(deportes[i]);

            listaDeportes.add(dep);
        }

        while (items.size() < cantidad) {
            Deporte d= new Deporte(deportes[random.nextInt(4)]);
            Oferta oferta=new Oferta(d);
            Date fecha= new Date(2015/5/3);
            Long hora= new Long(15);
            String ubicacion= new String("Bahia");
            oferta.setHora(hora);
            oferta.setFecha(fecha);
            oferta.setUbicacion(ubicacion);

            oferta.setEstado("disponible");
            Long codigo= new Long(random.nextLong()%1000);
            oferta.setCodigo(codigo);
            items.add(oferta);
        }

        return items;
    }


  public  boolean   reservarOferta(Oferta of,Usuario user){
     of.setEstado("reservada");
      return true;
  }

   public  Oferta  getOferta(Long codigo){

       for(int i=0;i<items.size();i++)
           if(items.get(i).getCodigo().equals(codigo))
               return  items.get(i);
     return items.get(0);
   }



    public  List<Deporte> getDeportes(){


        return listaDeportes;

    }
    public  int cantidadDeportes(){

        return listaDeportes.size();
    }

    public Deporte getDeporte(String nombre){

            for(int i=0;i<listaDeportes.size();i++)
                if(listaDeportes.get(i).getNombre().equals(nombre))
                    return listaDeportes.get(i);
        return null;
    }

    public List<Oferta> getOfertasDeporte(Deporte d){
        List<Oferta> ofertas= new ArrayList<Oferta>();
        for(int i=0;i<items.size();i++)
            if(items.get(i).getDeporte().getNombre().equals(d.getNombre()))
                ofertas.add(items.get(i));

        return ofertas;
    }

    @Override
    public Oferta getOfertaCodigo(Long codigo) {
        return null;
    }


    @Override
    public List<Oferta> getOfertasUbicacion(String ubicacion) {
        return null;
    }
}
