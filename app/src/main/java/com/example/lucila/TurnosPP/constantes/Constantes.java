package com.example.lucila.turnosPP.constantes;

public class Constantes {

    public static final int CODIGO_DETALLE = 100;

    public static final int CODIGO_ACTUALIZACION = 101;

    private static final String PUERTO_HOST = "";

    public static final String IP = "http://hosting.cs.uns.edu.ar/~com109/webServiceAndroid2";

    public static final String GET = IP + PUERTO_HOST + "/respuestasUsuario.php";
    public static final String GET_BY_ID = IP + PUERTO_HOST + "/obtener_meta_por_id.php";
    public static final String UPDATE = IP + PUERTO_HOST + "/ConsultasEstablecimiento.php";
    public static final String DELETE = IP + PUERTO_HOST + "/borrar_meta.php";
    public static final String INSERT = IP + PUERTO_HOST + "/insertar_meta.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

    public static final String GET_OFERTAS_ESTABLECIMIENTO= UPDATE + "?funcion=getOfertasEstablecimiento";

    public static final String LOGIN= IP + PUERTO_HOST + "/ConsultasEstablecimiento.php";

    //Usados por la actividad ComprarPackActivity
    public static final String PACKS_DISPONIBLES = "packs_disponibles";
    public static final String ID_PACK_COMPRADO = "id_pack_comprado";
    public static final String PACK_ELEGIDO = "pack_elegido";
    public static final String JSON_RESPUESTA = "JSONRespuesta";
    public static final String SERVICIO_VOLLEY = "servicio_volley";
    public static final String ARREGLO_DEPORTES_ESTABLECIMIENTO = "arreglo_deportes_establecimiento";
    public static final String OFERTA_EDITAR = "oferta_editar";
    public static final String CREAR_OFERTA_FRAG = "crear_oferta_frag";
}
