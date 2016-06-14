package com.example.lucila.turnosPP;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucila.turnosPP.beans.Oferta;
import com.example.lucila.turnosPP.fragmentos.OfertasFragment;
import com.example.lucila.myapplication.R;

import java.util.Calendar;
import java.util.List;

public class MyOfertaRecyclerViewAdapter extends RecyclerView.Adapter<MyOfertaRecyclerViewAdapter.ViewHolder> {

    private final List<Oferta> mValues;
    private final OfertasFragment.OnListFragmentInteractionListener mListener;

    public MyOfertaRecyclerViewAdapter(List<Oferta> items, OfertasFragment.OnListFragmentInteractionListener listener) {
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
        holder.mItem= oferta;
        Calendar fecha= oferta.getFecha();
        String dia, hora;
        dia= fecha.get(Calendar.DAY_OF_MONTH) + "/" +
                fecha.get(Calendar.MONTH) + "/" +
                fecha.get(Calendar.YEAR);
        hora= fecha.get(Calendar.HOUR) + ":" + fecha.get(Calendar.MINUTE);
        holder.mFecha.setText(dia);
        holder.mHora.setText(hora);
        //TODO estado
        holder.mEstado.setText(oferta.getEstado());
        holder.mDeporte.setText(oferta.getNombreDeporte());

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
