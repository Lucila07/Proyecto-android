package com.example.lucila.myapplication;
/**
 * Created by Agustin on 30/04/2016.
 * adaptador para la lista de ofertas
 * */
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lucila.myapplication.Datos.MapeoIdEstablecimiento;
import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Establecimiento;
import com.example.lucila.myapplication.Entidades.Oferta;


import java.util.List;

public class AdapterOfertasPrincipales extends RecyclerView.Adapter<AdapterOfertasPrincipales.ViewHolder>   implements View.OnClickListener{

    private List<Oferta>lista_ofertas;
    public Context contexto;
    private View.OnClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView text_deporte;
        public TextView text_fecha;
        public TextView text_hora;
        public TextView text_ubicacion;
        public TextView id_oferta;
        public CardView cardview_lista;
        public ImageView imagenOferta;
        public  TextView nombre_establecimiento;
        public TextView text_ahorro;

        public ViewHolder(View v) {
            super(v);
            text_deporte = (TextView)v.findViewById(R.id.deporte);
            text_fecha = (TextView)v.findViewById(R.id.fecha);
            text_hora = (TextView)v.findViewById(R.id.hora);
            text_ubicacion = (TextView)v.findViewById(R.id.ubicacion);
            cardview_lista=(CardView)v.findViewById(R.id.cardview_lista);
            imagenOferta =(ImageView)v.findViewById(R.id.imagen_oferta);
            id_oferta=(TextView)v.findViewById(R.id.oferta_id);
            text_ahorro=(TextView)v.findViewById(R.id.tv_ahorro_oferta);
            nombre_establecimiento=(TextView)v.findViewById(R.id.nombre_establecimiento);

        }
    }


    public AdapterOfertasPrincipales(List<Oferta>mis_datos, Context context) {
        contexto=context;
        lista_ofertas=mis_datos;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        v.setOnClickListener(this);//le seteo el clink listener

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Oferta oferta= lista_ofertas.get(position);
        if(oferta.getEstado().equals("disponible")) {
            if (oferta.getDeporte() != null) {

                setImagenOferta(holder, oferta.getDeporte());
                holder.text_deporte.setText(oferta.getDeporte().getNombre());
            } else holder.text_deporte.setText("deporte no establecido");

            holder.text_ubicacion.setText(oferta.getUbicacion());
            holder.text_hora.setText(oferta.getHora().toString());
            holder.text_fecha.setText(oferta.getFecha().toString());
            Integer int_ahorro =new Integer(((oferta.getPrecioHabitual()-oferta.getPrecioOferta())*100)/oferta.getPrecioHabitual());
            String s_ahorro=int_ahorro.toString();
            holder.text_ahorro.setText(s_ahorro);

            holder.id_oferta.setText(lista_ofertas.get(position).getCodigo().toString());
            Establecimiento establecimiento= MapeoIdEstablecimiento.getInstance().getById(oferta.getIdUserCreador());
            if(establecimiento!=null){

                holder.nombre_establecimiento.setText(establecimiento.getNombre());
            }
        }
    }


    @Override
    public int getItemCount() {
        return lista_ofertas.size();
    }

    /**
     * establemcemos el clink listener
     *@param: listener. se asigna  desde fuera del adaptador
     * */

    public void ClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    /**
     * redefinimos el metodo onClink
     * */
    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public void setImagenOferta(ViewHolder holder,Deporte deporte ){

        int recurso=0;
        Log.d("dibujar ofertas","el nombre del deporte "+deporte.getNombre());
        switch (deporte.getNombre()){
            case "futbol":
                recurso= R.drawable.football;
                break;
            case "Futbol":
                recurso= R.drawable.football;
                break;
            case "voley":
                recurso=R.drawable.voley;
                break;
            case "Voley":
                recurso=R.drawable.voley;
                break;
            case "basquet":
                recurso=R.drawable.basquet;
                break;
            case "Basquet":
                recurso=R.drawable.basquet;
                break;
            case "tennis":
                recurso= R.drawable.tenis;
                break;
            case "tenis":
                recurso= R.drawable.tenis;
                break;
            case "Tenis":
                recurso= R.drawable.tenis;
                break;
            case "paddle":
                recurso=R.drawable.paddle;
                break;
            case "Paddle":
                recurso=R.drawable.paddle;
                break;

        }
        holder.imagenOferta.setImageResource(recurso);

    }
}