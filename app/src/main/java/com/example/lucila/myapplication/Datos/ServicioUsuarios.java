package com.example.lucila.myapplication.Datos;

import com.example.lucila.myapplication.Entidades.Usuario;

/**
 * Created by tino on 15/05/2016.
 */
public interface ServicioUsuarios {

    /**
     * retorna el usuario que esta registrado en la app
     * **/
    public  Usuario getUsuarioLogueado();

    /*
    * Recibe el emial y la contrasena y si existen en el servidor retorna verdadero
    * caso contrario retorna falso
    * */
    public  void verificarExistencia(Usuario user);


    public void crearUsuario(String nombreUsuario, String mail, String id, String telefono);
}
