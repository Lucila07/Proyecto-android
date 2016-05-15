package com.example.lucila.myapplication.Datos;

import com.example.lucila.myapplication.Entidades.Usuario;

/**
 * Created by tino on 15/05/2016.
 */
public class ServicioUsuarioLista implements  ServicioUsuarios {

    public Usuario getUsuarioLogueado(){

        Usuario user= new Usuario("Agustin","Koll");
        user.setTelefono(454467);
        return user;
    }
}
