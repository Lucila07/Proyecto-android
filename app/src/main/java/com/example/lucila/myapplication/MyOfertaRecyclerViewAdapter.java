package com.example.lucila.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucila.myapplication.OfertasFragment.OnListFragmentInteractionListener;
import com.example.lucila.myapplication.beans.Oferta;

import java.util.Calendar;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Oferta} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyOfertaRecyclerViewAdapter extends RecyclerView.Adapter<MyOfertaRecyclerViewAdapter.ViewHolder> {

    private final List<Oferta> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyOfertaRecyclerViewAdapter(List<Oferta> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_oferta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Oferta oferta= mValues.get(position);
        //Calendar fecha= oferta.getFecha();
        holder.mItem= oferta;
        holder.mFecha.setText(oferta.fecha);
        holder.mHora.setText(oferta.hora);
        //TODO estado
        holder.mEstado.setText(oferta.estado);
        holder.mDeporte.setText(oferta.idDeporte);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //ViewHolder para el RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFecha;
        public final TextView mHora;
        public final TextView mEstado;
        public final TextView mDeporte;
        public Oferta mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFecha = (TextView) view.findViewById(R.id.card_fecha_oferta);
            mHora = (TextView) view.findViewById(R.id.card_hora_oferta);
            mEstado= (TextView) view.findViewById(R.id.card_estado_oferta);
            mDeporte= (TextView) view.findViewById(R.id.card_deporte_oferta);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mEstado.getText() + "'";
        }
    }
}
