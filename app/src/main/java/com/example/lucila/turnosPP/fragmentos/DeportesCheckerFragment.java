package com.example.lucila.turnosPP.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;


import com.example.lucila.myapplication.R;

import java.io.Serializable;
import java.util.Map;

public class DeportesCheckerFragment extends Fragment {

    private static final String ARG_PARAM1 = "DeportesEstablecimiento";

    //Mapeo que tiene todos los deportes y cual de ellos esta presente en el establecimiento
    //Usado para generar los checks.
    private Map<String, Boolean> deportesEstablecimiento;

    private OnGuardarCambiosListener mListener;

    public DeportesCheckerFragment() {}

    public static DeportesCheckerFragment newInstance(Map<String, Boolean> deportesEstablecimiento) {
        DeportesCheckerFragment fragment = new DeportesCheckerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) deportesEstablecimiento);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            deportesEstablecimiento = (Map<String, Boolean>) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista= inflater.inflate(R.layout.fragment_deportes_checker, container, false);
        LinearLayout layout= (LinearLayout) vista.findViewById(R.id.panel_checks);

        //Agrego todos los checkbox por cada deporte y si el establecimiento los tiene o no
        CheckBox depCheck;
        for(String deporte : deportesEstablecimiento.keySet()) {
            depCheck= new CheckBox(getActivity());
            depCheck.setText(deporte);
            depCheck.setChecked(deportesEstablecimiento.get(deporte));
            //Mantengo la representacion de los deportes del establecimiento dentro del fragment
            //Y la logica de almacenado se la dejo a la actividad que lo use
            depCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    buttonView.setChecked(isChecked);
                    actualizarMapeo(buttonView.getText().toString(), isChecked);
                }
            });
            layout.addView(depCheck);
        }

        Button botonGuardar= (Button) vista.findViewById(R.id.boton_guardarcambios_checks);
        //Agrego el listener de esta manera para que el fragment no lo tenga que implementar explicitamente
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios(v);
            }
        });

        return vista;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGuardarCambiosListener) {
            mListener = (OnGuardarCambiosListener) context;
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

    public interface OnGuardarCambiosListener {
        // Cuando el usuario quiere guardar los cambios, estos deben ser manejados por la actividad
        void onGuardarCambios(Map<String, Boolean> deportes);
    }

    private void actualizarMapeo(String deporte, boolean check) {
        deportesEstablecimiento.put(deporte, check);
    }

    private void guardarCambios(View view) {
        if (mListener != null) {
            mListener.onGuardarCambios(deportesEstablecimiento);
        }
    }
}
