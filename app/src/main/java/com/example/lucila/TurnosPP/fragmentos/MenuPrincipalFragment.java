package com.example.lucila.turnosPP.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucila.myapplication.R;

public class MenuPrincipalFragment extends Fragment implements View.OnClickListener{

    private OnBotonMenuClickListener mListener;

    private TextView cantOfertasTextView;
    private TextView recordatorioTextView;

    private int cantOfertas;

    public MenuPrincipalFragment() {
        // Required empty public constructor
    }

    public static MenuPrincipalFragment newInstance(int cantOfertas) {
        MenuPrincipalFragment fragment = new MenuPrincipalFragment();
        Bundle args = new Bundle();
        args.putInt("cantOfertas", cantOfertas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cantOfertas= getArguments().getInt("cantOfertas");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View toR= inflater.inflate(R.layout.fragment_menu_principal, container, false);

        toR.findViewById(R.id.boton_crear_oferta).setOnClickListener(this);
        toR.findViewById(R.id.boton_comprar_ofertas).setOnClickListener(this);
        toR.findViewById(R.id.boton_denunciar_usuario).setOnClickListener(this);
        toR.findViewById(R.id.boton_establecer_ubicacion).setOnClickListener(this);
        toR.findViewById(R.id.boton_ver_creadas).setOnClickListener(this);

        cantOfertasTextView= (TextView) toR.findViewById(R.id.textview_n_ofertas_restantes);
        cantOfertasTextView.setText(String.format(getString(R.string.textview_restantes), cantOfertas));
        recordatorioTextView= (TextView) toR.findViewById(R.id.textView_info_menu_ppal);

        return toR;
    }

    public void onClick(View view) {
        if (mListener != null) {
            mListener.onClickBoton(view);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBotonMenuClickListener) {
            mListener = (OnBotonMenuClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBotonMenuClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setCantidadDeOfertas(int cantidadDeOfertas) {
        String text= String.format(getString(R.string.textview_restantes), cantidadDeOfertas);
        cantOfertasTextView.setText(text);
    }

    public void setRecordatorio(boolean esVisible) {
        recordatorioTextView.setVisibility(
                esVisible?
                        View.VISIBLE:
                        View.INVISIBLE
        );
    }

    /*
     *Interface para el callback de los botones del menu principal
     */
    public interface OnBotonMenuClickListener {
        void onClickBoton(View view);
    }
}
