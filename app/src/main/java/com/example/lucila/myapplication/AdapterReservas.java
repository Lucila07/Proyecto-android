package com.example.lucila.myapplication;

import android.support.v7.widget.RecyclerView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucila.myapplication.Datos.ItemData;
import com.example.lucila.myapplication.Entidades.Deporte;
import com.example.lucila.myapplication.Entidades.Oferta;

/**
 * Created by Lucila on 22/05/2016.
 */

public class AdapterReservas extends RecyclerView.Adapter<AdapterReservas.ViewHolder> {

    private Oferta[] itemsData;

    public AdapterReservas( Oferta[] itemsData ) {

        this.itemsData=itemsData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterReservas.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservas_layout, null);

        // create ViewHolder

        AdapterReservas.ViewHolder viewHolder = new AdapterReservas.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterReservas.ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Oferta oferta=itemsData[position];
        viewHolder.txtViewTitle.setText(oferta.getDeporte().getNombre());
        viewHolder.detalle.setText(oferta.getFecha() + "\n" + oferta.getHora());
        setImagenOferta(viewHolder,oferta.getDeporte());



    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;
        public  TextView detalle;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            detalle =(TextView) itemLayoutView.findViewById(R.id.item_detalle);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.length;

    }

    public void setImagenOferta(ViewHolder holder,Deporte deporte ){

        int recurso=0;
        switch (deporte.getNombre()){
            case "futbol":
                recurso=R.drawable.futbol;
                break;
            case "Futbol":
                recurso=R.drawable.futbol;
                break;

            case "voley":
                recurso=R.drawable.voley;
                break;
            case "Voley":
                recurso=R.drawable.voley;
                break;

            case "basquet":
                recurso=R.drawable.basketball;
                break;
            case "Basquet":
                recurso=R.drawable.basketball;
                break;
            case "tenis":
                recurso=R.drawable.tennis;
                break;
            case "Tenis":
                recurso=R.drawable.tennis;
                break;

        }
        holder.imgViewIcon.setImageResource(recurso);

    }
}
