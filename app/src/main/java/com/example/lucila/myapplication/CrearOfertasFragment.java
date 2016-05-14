package com.example.lucila.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lucila.beans.Oferta;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrearOfertasFragment.OnCrearOfertaListener} interface
 * to handle interaction events.
 * Use the {@link CrearOfertasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearOfertasFragment extends Fragment implements View.OnClickListener {

    private OnCrearOfertaListener mListener;

    private EditText fecha;

    public CrearOfertasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrearOfertasFragment.
     */
    public static CrearOfertasFragment newInstance() {
        CrearOfertasFragment fragment = new CrearOfertasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout= inflater.inflate(R.layout.fragment_crear_ofertas, container, false);
        Button botonCrear= (Button) fragmentLayout.findViewById(R.id.boton_crear_ofertas_fragment);
        botonCrear.setOnClickListener(this);

        botonCrear = (Button) fragmentLayout.findViewById(R.id.boton_fecha);
        botonCrear.setOnClickListener(this);

        fecha = (EditText) fragmentLayout.findViewById(R.id.textView_fecha);
        return fragmentLayout;
    }

    public void onClick(View view) {
        if (mListener != null) {
            if(view.getId() == R.id.boton_crear_ofertas_fragment) {
                Oferta of = new Oferta();
                mListener.onCrearOferta(of);
            }
            else {
                mListener.mostrarDialogo();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCrearOfertaListener) {
            mListener = (OnCrearOfertaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCrearOfertaListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setFecha(String fecha) {
        this.fecha.setText(fecha);
    }

    public interface OnCrearOfertaListener {
        void onCrearOferta(Oferta oferta);
        void mostrarDialogo();
    }
}
