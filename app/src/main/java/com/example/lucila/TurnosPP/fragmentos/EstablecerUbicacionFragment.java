package com.example.lucila.turnosPP.fragmentos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lucila.myapplication.R;

public class EstablecerUbicacionFragment extends Fragment implements View.OnClickListener {

    public static final int PERMISO_UBICACION= 1;

    private OnEstablecerUbicacionListener mListener;

    private EditText direccion;
    private String textoDireccion;
    private Button boton_GPS;

    public EstablecerUbicacionFragment() {}

    /*
    * Crea una instancia del fragmento con una direccion actual.
    */
    public static EstablecerUbicacionFragment newInstance(String direccionActual) {
        EstablecerUbicacionFragment fragment = new EstablecerUbicacionFragment();
        Bundle args = new Bundle();
        args.putString("direccion", direccionActual);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recupero la direccion
        if(savedInstanceState != null) {
            textoDireccion= savedInstanceState.getString("direccion");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("direccion", textoDireccion);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View toR= inflater.inflate(R.layout.fragment_establecer_ubicacion, container, false);

        boton_GPS= (Button) toR.findViewById(R.id.boton_establecerUbicacion);
        //Permiso peligroso: ahora android pregunta al usuario dentro de la aplicación.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            //Si es la primera vez que se usa, se le pregunta con un dialogo y se esperar el resultado.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);
        else {
            //Sino, se puede usar.
            boton_GPS.setOnClickListener(this);
        }

        direccion= (EditText) toR.findViewById(R.id.edit_text_ubicacion_fragment);
        direccion.setText(textoDireccion);
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
        if (context instanceof OnEstablecerUbicacionListener) {
            mListener = (OnEstablecerUbicacionListener) context;
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

    public void setDireccion(String direccion) {
        this.direccion.setText(direccion);
        textoDireccion= direccion;
    }

    @Override
    /*
    * El resultado del dialogo se pasa por esta funcion, y permite determinar
    * si el boton de GPS tiene que estar habilitado o no
    */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //Usa el requestCode pasado en el requestPermision()
        switch (requestCode) {
            case PERMISO_UBICACION: {
                //Si el usuario acepto
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boton_GPS.setEnabled(true);
                    boton_GPS.setOnClickListener(this);
                }
                else {
                    boton_GPS.setEnabled(false);
                }
            }
        }
    }

    public interface OnEstablecerUbicacionListener {
        // TODO: Update argument type and name
        void onObtenerUbicacionListener();
    }
}
