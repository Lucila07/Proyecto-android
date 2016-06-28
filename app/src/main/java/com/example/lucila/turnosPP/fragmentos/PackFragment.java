package com.example.lucila.turnosPP.fragmentos;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Pack;

public class PackFragment
        extends Fragment
        implements View.OnClickListener{

    private static final String PACK = "objetoPack";
    private static final String ADQUIRIDO = "packAdquirido";

    private Pack mPack;
    private boolean adquirido;

    private OnEleccionPackListener mListener;

    public PackFragment() {/*Constructor necesario*/}

    public static PackFragment newInstance(Pack pack, boolean adquirido) {
        PackFragment fragment = new PackFragment();
        Bundle args = new Bundle();
        args.putSerializable(PACK, pack);
        args.putBoolean(ADQUIRIDO,adquirido);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPack = (Pack) getArguments().getSerializable(PACK);
            adquirido = getArguments().getBoolean(ADQUIRIDO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View toR= inflater.inflate(R.layout.fragment_pack, container, false);

        //creo los textos para los TextViews del fragment.
        Resources resources= getResources();
        String textTipoPack= String.format(resources.getString(R.string.TextView_TipoPack), mPack.getTipo());
        String textPrecioPack= String.format(resources.getString(R.string.TextView_PrecioPack), mPack.getPrecio());
        String textCantidadOfertas= String.format(getResources().getConfiguration().locale,"%d", mPack.getCantidadOfertas());

        //Los asigno a sus respectivos TextViews
        ((TextView)toR.findViewById(R.id.textview_tipoPack)).setText(textTipoPack);
        ((TextView)toR.findViewById(R.id.textview_precioPack)).setText(textPrecioPack);
        ((TextView)toR.findViewById(R.id.textview_descripcionPack)).setText(mPack.getDescripcion());
        ((TextView)toR.findViewById(R.id.textview_cantidadOfertasPack)).setText(textCantidadOfertas);

        //Si es un pack ya comprado, cambio el color de fondo para crear el efecto de un marco
        if(adquirido) {
            int color= ContextCompat.getColor(getContext(),R.color.colorAccentLight);
            toR.findViewById(R.id.FragmentPack_Marco).setBackgroundColor(color);
        } else {
            //Sino seteo un click listener a el fragmento
            toR.setOnClickListener(this);
        }
        return toR;
    }

    public void onClick(View view) {
        if (mListener != null) {
            //Devulevo el pack asociado a este fragment
            mListener.onEleccionPack(mPack);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnEleccionPackListener) {
            mListener = (OnEleccionPackListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEleccionPackListener {
        void onEleccionPack(Pack pack);
    }
}
