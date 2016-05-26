package com.example.lucila.myapplication.Datos;

import com.example.lucila.myapplication.Entidades.Usuario;

/**
 * Created by tino on 25/05/2016.
 */
public class ServicioUsuariosHttp implements  ServicioUsuarios{

    @Override
    public Usuario getUsuarioLogueado() {
        return null;
    }

    @Override
    public boolean validarUsuario(String email, String pass) {
        return true;
    }
}
