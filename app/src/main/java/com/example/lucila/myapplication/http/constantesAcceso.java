package com.example.lucila.myapplication.http;
/**
 * Clase que contiene ip, puerto, y url para acceder al servicio web
 *Author: AgusKoll
 */
public class constantesAcceso {


        /**
         * Puerto que utilizas para la conexión.
         */
        private static final String PUERTO_HOST = "85";

        /**
         * Dirección IP
         */
        private static final String IP = "http://127.0.0.1:";
        /**
         * URLs del Web Service
         */
        public static final String GET_OFERTAS = IP + PUERTO_HOST + "/webServiceAndroid2/respuestasUsuario.php?funcion=getOfertas";
        public static final String GET_OFERTA_CODIGO = IP + PUERTO_HOST + "webServiceAndroid2/respuestasUsuario.php?funcion=getOfertaCodigo&codigo=";
        public static final String GET_OFERTA_UBICACION = IP + PUERTO_HOST + "webServiceAndroid2/respuestasUsuario.php?funcion=getOfertasUbicacion&ubicacion=";
        public static final String GET_OFERTA_DEPORTE = IP + PUERTO_HOST + "webServiceAndroid2/respuestasUsuario.php?funcion=getOfertasDeporte&deporte=";
    public static final String RESERVAR_OFERTA = IP + PUERTO_HOST + "webServiceAndroid2/respuestasUsuario.php?funcion=funcion=reservarOferta&codigo=";//123&idUser=1";


    }

