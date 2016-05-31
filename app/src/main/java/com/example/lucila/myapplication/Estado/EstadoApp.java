package com.example.lucila.myapplication.Estado;

import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Establecimiento;
import com.example.lucila.myapplication.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * creado por tino on 31/05/2016.
 * Su funcion es guardar los  deportes, usuario loggueado y establecimientos
 * ya que es informacion que se pide una unica vez durante una secion
 * es un objeto singleton
 */
public class EstadoApp {

    private List<Deporte>deportes;
    private List<Establecimiento>establecimientos;
    private  Usuario usuarioLogueado;
    private static EstadoApp instancia;

    private EstadoApp() {
        this.deportes = new ArrayList<Deporte>();
        this.establecimientos = new ArrayList<Establecimiento>();

    }

    public static  EstadoApp getInstance(){
        if(instancia==null)
            instancia= new EstadoApp();
       return instancia;

    }

    public List<Deporte> getDeportes() {
        return deportes;
    }

    public void setDeportes(List<Deporte> deportes) {
        this.deportes = deportes;
    }

    public List<Establecimiento> getEstablecimientos() {
        return establecimientos;
    }

    public void setEstablecimientos(List<Establecimiento> establecimientos) {
        this.establecimientos = establecimientos;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }


}
