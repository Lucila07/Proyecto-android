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

/**
 * Created by Lucila on 22/05/2016.
 */

public class AdapterReservas extends RecyclerView.Adapter<AdapterReservas.ViewHolder> {

    private ItemData[] itemsData;

    public AdapterReservas( ItemData[] itemsData ) {

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
        viewHolder.txtViewTitle.setText(itemsData[position].getDeporte());
        viewHolder.imgViewIcon.setImageResource(itemsData[position].getImageUrl());
        viewHolder.detalle.setText(itemsData[position].getLugar() + "\n" + itemsData[position].getFecha() + "\n" + itemsData[position].getHora());





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


}
