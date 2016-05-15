package com.example.lucila.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.lucila.beans.Oferta;


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

    private EditText editTextFecha, editTextHora;

    //Usado por el adapter del AutoCompleteTextField
    //Para sugerir deportes cargados
    private String[] deportes;

    public CrearOfertasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrearOfertasFragment.
     */
    public static CrearOfertasFragment newInstance(String[] deportes) {
        CrearOfertasFragment fragment = new CrearOfertasFragment();
        Bundle args = new Bundle();
        args.putSerializable("listaDeportes", deportes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            deportes= (String[]) savedInstanceState.get("listaDeportes");
        else
            deportes= new String[0];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout= inflater.inflate(R.layout.fragment_crear_ofertas, container, false);

        //Agrego el listener al botón para crear la oferta
        Button botonCrear= (Button) fragmentLayout.findViewById(R.id.boton_crear_ofertas_fragment);
        botonCrear.setOnClickListener(this);

        //Es temporal hasta poder poner el listener al text view de fecha
        botonCrear = (Button) fragmentLayout.findViewById(R.id.boton_fecha);
        botonCrear.setOnClickListener(this);

        botonCrear = (Button) fragmentLayout.findViewById(R.id.boton_hora);
        botonCrear.setOnClickListener(this);

        //Mantengo el edit text de la fecha y la hora
        editTextFecha = (EditText) fragmentLayout.findViewById(R.id.textView_fecha);
        editTextHora = (EditText) fragmentLayout.findViewById(R.id.textView_hora);

        //Anexo las sugerencias al TextView
        AutoCompleteTextView textViewDeporte= (AutoCompleteTextView) fragmentLayout.findViewById(R.id.autocomplete_deporte);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, this.deportes);
        textViewDeporte.setAdapter(adapter);
        //textViewDeporte.setDropDownHeight(3);
        return fragmentLayout;
    }

    public void onClick(View view) {
        if (mListener != null) {
            if(view.getId() == R.id.boton_crear_ofertas_fragment) {
                Oferta of = new Oferta();
                mListener.onCrearOferta(of);
            }
            else {
                if (view.getId() == R.id.boton_fecha) {
                    mListener.mostrarDialogoFecha();
                } else mListener.mostrarDialogoHora();
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
        this.editTextFecha.setText(fecha);
    }

    public void setHora(String hora) { this.editTextHora.setText(hora); }

    public interface OnCrearOfertaListener {
        void onCrearOferta(Oferta oferta);
        void mostrarDialogoHora();
        void mostrarDialogoFecha();
    }
}
