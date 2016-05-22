package com.example.lucila.myapplication.http;
/**
 * Clase que contiene ip, puerto, y url para acceder al servicio web
 *Author: AgusKoll
 */
public class ConstantesAcceso {


        /**
         * Puerto que utilizas para la conexión.
         */
        private static final String PUERTO_HOST = "85";

        /**
         * Dirección IP
         */
        private static final String IP = "http://192.168.1.106:";
        /**
         * URLs del Web Service
         */
        private static String GET_OFERTAS = IP + PUERTO_HOST + "/webServiceAndroid2/respuestasUsuario.php?funcion=getOfertas";
        private static String GET_OFERTA_CODIGO = IP + PUERTO_HOST + "webServiceAndroid2/respuestasUsuario.php?funcion=getOfertaCodigo&codigo=";
       private static String GET_OFERTA_UBICACION = IP + PUERTO_HOST + "webServiceAndroid2/respuestasUsuario.php?funcion=getOfertasUbicacion&ubicacion=";
       private static  String GET_OFERTA_DEPORTE = IP + PUERTO_HOST + "webServiceAndroid2/respuestasUsuario.php?funcion=getOfertasDeporte&deporte=";
       private  static String RESERVAR_OFERTA = IP + PUERTO_HOST + "webServiceAndroid2/respuestasUsuario.php?funcion=funcion=reservarOferta&codigo=";//123&idUser=1";

       public static   String getURL(String get,String param1){

               switch (get){
                       case "get_ofertas":
                               return GET_OFERTAS;
                       case "get_oferta_codigo":
                               return GET_OFERTA_CODIGO+param1;
                       case "get_oferta_ubicacion":
                               return GET_OFERTA_UBICACION+param1;
                       case "get_oferta_deporte":
                               return GET_OFERTA_DEPORTE+param1;

               }
               return new String();
       }

    public static String getURLRerserva(String codigo, String usuario ){

        return RESERVAR_OFERTA+codigo+"&"+usuario;
    }
    }

