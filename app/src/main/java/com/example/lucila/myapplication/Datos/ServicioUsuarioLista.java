package com.example.lucila.myapplication.Datos;

import com.example.lucila.myapplication.Entidades.Usuario;

/**
 * Created by tino on 15/05/2016.
 */
public class ServicioUsuarioLista  {

    public Usuario getUsuarioLogueado(){

        Usuario user= new Usuario("Agustin");
        user.setTelefono("454467");
        user.setIdUsuario("123");
        return user;
    }

    public boolean validarUsuario(String email, String pass) {
        return false;
    }
}
