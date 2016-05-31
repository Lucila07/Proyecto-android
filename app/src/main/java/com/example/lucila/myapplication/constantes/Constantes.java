package com.example.lucila.myapplication.constantes;

public class Constantes {

    public static final int CODIGO_DETALLE = 100;

    public static final int CODIGO_ACTUALIZACION = 101;

    private static final String PUERTO_HOST = "85";

    private static final String IP = "http://192.168.1.103:";

    public static final String GET = IP + PUERTO_HOST + "/respuestasUsuario.php";
    public static final String GET_BY_ID = IP + PUERTO_HOST + "/obtener_meta_por_id.php";
    public static final String UPDATE = IP + PUERTO_HOST + "/actualizar_meta.php";
    public static final String DELETE = IP + PUERTO_HOST + "/borrar_meta.php";
    public static final String INSERT = IP + PUERTO_HOST + "/insertar_meta.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

    public static final String GET_OFERTAS_ESTABLECIMIENTO= GET + "?funcion=getOfertasPorEstablecimiento";

}