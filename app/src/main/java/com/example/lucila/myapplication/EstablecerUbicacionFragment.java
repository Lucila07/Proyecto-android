package com.example.lucila.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EstablecerUbicacionFragment extends Fragment  implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private EditText direccion;

    public EstablecerUbicacionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EstablecerUbicacionFragment newInstance(String param1, String param2) {
        EstablecerUbicacionFragment fragment = new EstablecerUbicacionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View toR= inflater.inflate(R.layout.fragment_establecer_ubicacion, container, false);

        ((Button)toR.findViewById(R.id.boton_establecerUbicacion)).setOnClickListener(this);

        direccion= (EditText) toR.findViewById(R.id.editText_ubicacion);
        return toR;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onObtenerUbicacionListener();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onObtenerUbicacionListener();
    }

    public void setDireccion(String direccion) {
        this.direccion.setText(direccion);
    }
}
