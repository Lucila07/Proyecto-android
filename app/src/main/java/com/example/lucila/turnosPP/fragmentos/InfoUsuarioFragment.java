package com.example.lucila.turnosPP.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Establecimiento;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InfoUsuarioFragment extends Fragment {

    private static final String ARG_PARAM1 = "establecimiento";

    private Establecimiento establecimiento;

    //Campos de input
    private EditText mail, telefono, usuario, contra, repetirContra, ubicacion;
    private TextView deportes;

    //Campos de error
    private TextView errorCampoEMail,
            errorCampoUser,
            errorCampoContra,
            errorCampoRepetirContra,
            errorCampoTel;

    //Son necesarios por si hay que ocultarlos
    private LinearLayout panelContra, panelRepetirContra;

    private OnFragmentInteractionListener mListener;

    public InfoUsuarioFragment() {}

    public static InfoUsuarioFragment newInstance(Establecimiento param1) {
        InfoUsuarioFragment fragment = new InfoUsuarioFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            establecimiento= (Establecimiento) getArguments().getSerializable(ARG_PARAM1);
        } else {
            //Por si se usa este fragment en un archivo layout
            establecimiento= new Establecimiento();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View toR= inflater.inflate(R.layout.fragment_info_usuario, container, false);

        //Busco los views
        mail= (EditText) toR.findViewById(R.id.email_textfield_registrarse);
        telefono= (EditText) toR.findViewById(R.id.telefono_textfield_registrarse);
        usuario= (EditText) toR.findViewById(R.id.user_textfield_registrarse);
        contra= (EditText) toR.findViewById(R.id.contra_textfield_registrarse);
        repetirContra= (EditText) toR.findViewById(R.id.repetir_contra_textfield_registrarse);
        ubicacion= (EditText) toR.findViewById(R.id.ubicacion_textfield_registrarse);
        deportes= (TextView) toR.findViewById(R.id.deportes_textfield_registrarse);
        panelContra= (LinearLayout) toR.findViewById(R.id.campo_contra);
        panelRepetirContra= (LinearLayout) toR.findViewById(R.id.campo_repetirContra);
        errorCampoEMail= (TextView) toR.findViewById(R.id.error_email_textfield_registrarse);
        errorCampoContra= (TextView) toR.findViewById(R.id.error_contra_textfield_registrarse);
        errorCampoRepetirContra= (TextView) toR.findViewById(R.id.error_repetir_contra_textfield_registrarse);
        errorCampoUser= (TextView) toR.findViewById(R.id.error_usuario_textfield_registrarse);
        errorCampoTel= (TextView) toR.findViewById(R.id.telefono_textfield_registrarse);

        //Asigno los listeners
        toR.findViewById(R.id.boton_guardarcambios).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        toR.findViewById(R.id.boton_obtenerUbicacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                establecerUbicacion();
            }
        });

        toR.findViewById(R.id.panel_deportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                establecerDeportes();
            }
        });

        return toR;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actualizarUI();
    }

    public void guardarCambios() {
        boolean validado= validarEntrada();
        if (mListener != null && validado) {
            String telString= telefono.getText().toString();
            int tel= 0;
            if(!telString.isEmpty())
                tel= Integer.valueOf(telString);

            Establecimiento cambios= new Establecimiento(
                    establecimiento.getId(),
                    tel,
                    usuario.getText().toString(),
                    establecimiento.getToken(),
                    ubicacion.getText().toString(),
                    mail.getText().toString(),
                    establecimiento.getDeportes()
            );

            mListener.onGuardarCambios(cambios, contra.getText().toString());
        }
    }

    public void establecerUbicacion() {
        if(mListener != null) {
            mListener.onEstablecerUbicacion();
        }
    }

    public void establecerDeportes() {
        if(mListener != null) {
            mListener.onEstablecerDeportes();
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
        //Le pasas los cambios a la actividad encapsulado en el objeto Establecimiento
        void onGuardarCambios(Establecimiento establecimiento, String pass);
        //El usuario toco el boton de ubicacion
        void onEstablecerUbicacion();
        //El usuario toco el TextView de los deportes
        void onEstablecerDeportes();
    }

    public void actualizarDeportesUsuario(List<String> deportes) {
        establecimiento.setDeportes((ArrayList<String>) deportes);
        actualizarUI();
    }

    //Actualiza la ui
    public void actualizarUI() {
        mail.setText(establecimiento.getEmail());

        int tel= establecimiento.getTelefono();
        if(tel != 0)
            telefono.setText(Integer.toString(tel));

        usuario.setText(establecimiento.getNombre());

        if(!establecimiento.getToken().isEmpty()) {
            //Usuario de google no necesita los campos de contraseña
            panelContra.setVisibility(View.GONE);
            panelRepetirContra.setVisibility(View.GONE);
        }

        String deportes= new String();
        for(String deporte : establecimiento.getDeportes()) {
            deportes.concat(deporte);
            deportes.concat(" - ");
        }
        this.deportes.setText(deportes);

        ubicacion.setText(establecimiento.getUbicacion());
    }

    private boolean validarEntrada() {
        boolean pasa= true;

        String user= usuario.getText().toString();
        if(user.contentEquals("")) {
            errorCampoUser.setText(R.string.error_usuario);
            errorCampoUser.setVisibility(View.VISIBLE);
            pasa = false;
        }

        String contra= this.contra.getText().toString();
        if(contra.length() < 4) {
            errorCampoContra.setText(R.string.error_contra);
            errorCampoContra.setVisibility(View.VISIBLE);
            pasa = false;
        }

        String Rcontra= repetirContra.getText().toString();
        if(!contra.equals(Rcontra)) {
            errorCampoRepetirContra.setText(R.string.error_repetir_contra);
            errorCampoRepetirContra.setVisibility(View.VISIBLE);
            pasa= false;
        }

        Pattern patronEMail= Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        String email= mail.getText().toString();
        if(!email.matches(patronEMail.pattern())) {
            errorCampoEMail.setText(R.string.error_email);
            errorCampoEMail.setVisibility(View.VISIBLE);
            pasa=false;
        }


        return pasa;
    }
}
