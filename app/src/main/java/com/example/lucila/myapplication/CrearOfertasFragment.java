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
import android.widget.TextView;

import com.example.lucila.myapplication.beans.Oferta;

import java.util.Calendar;
import java.util.regex.Pattern;

public class CrearOfertasFragment
        extends Fragment
        implements View.OnClickListener {

    private OnCrearOfertaListener mListener;

    private TextView editTextFecha, editTextHora;
    private EditText precioHabitual, precioFinal;
    private AutoCompleteTextView deporteTextView;

    //Valores ingresados porp el usuario
    private int diaLeido, mesLeido, anioLeido, horaLeida, minLeido;
    private float precioHabLeido, precioFinalLeido;
    private Calendar fechaCalendario;

    //Textos de error
    private TextView errorFecha, errorHora, errorPrecioHab, errorPrecioFinal;

    //Patron de un número float
    private Pattern patronFloat;

    //Usado por el adapter del AutoCompleteTextField
    //Para sugerir deportes cargados
    private String[] deportes;

    public CrearOfertasFragment() {}

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
            deportes = new String[0];
        patronFloat= Pattern.compile("[0-9]*\\.?[0-9]+");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout= inflater.inflate(R.layout.fragment_crear_ofertas, container, false);

        //Agrego el listener al botón para crear la oferta
        Button botonCrear= (Button) fragmentLayout.findViewById(R.id.boton_crear_ofertas_fragment);
        botonCrear.setOnClickListener(this);

        //Mantengo el text view de la fecha y la hora
        editTextFecha = (TextView) fragmentLayout.findViewById(R.id.textView_fecha);
        editTextHora = (TextView) fragmentLayout.findViewById(R.id.textView_hora);

        //Agrego los listeners a los paneles de fecha y hora
        fragmentLayout.findViewById(R.id.panel_fecha).setOnClickListener(this);
        fragmentLayout.findViewById(R.id.panel_hora).setOnClickListener(this);

        //Anexo las sugerencias al TextView
        deporteTextView= (AutoCompleteTextView) fragmentLayout.findViewById(R.id.autocomplete_deporte);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, this.deportes);
        deporteTextView.setAdapter(adapter);
        deporteTextView.setDropDownHeight(3);

        precioFinal= (EditText) fragmentLayout.findViewById(R.id.edittext_precioFinal);
        precioHabitual= (EditText) fragmentLayout.findViewById(R.id.edittext_precioHabitual);

        errorFecha= (TextView) fragmentLayout.findViewById(R.id.error_fecha_crearOferta);
        errorHora= (TextView) fragmentLayout.findViewById(R.id.error_hora_crearOferta);
        errorPrecioFinal= (TextView) fragmentLayout.findViewById(R.id.error_precioFinal_crearOferta);
        errorPrecioHab= (TextView) fragmentLayout.findViewById(R.id.error_precioHab_crearOferta);

        return fragmentLayout;
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

    public void onClick(View view) {
        if (mListener != null) {
            if(view.getId() == R.id.boton_crear_ofertas_fragment) {
                if(validacionEntrada()) {
                    Oferta oferta = new Oferta(fechaCalendario,
                            "hola",
                            precioFinalLeido,
                            precioHabLeido);
                    resetErrores();
                    mListener.onCrearOferta(oferta);
                }
            }
            else if (view.getId() == R.id.panel_fecha)
                mListener.mostrarDialogoFecha();
            else
                mListener.mostrarDialogoHora();
        }
    }

    public void setFecha(String fecha, int dia, int mes, int anio) {
        this.editTextFecha.setText(fecha);
        diaLeido= dia;
        mesLeido= mes;
        anioLeido= anio;
    }

    public void setHora(String horaS, int hora, int minuto) {
        this.editTextHora.setText(horaS);
        horaLeida= hora;
        minLeido= minuto;
    }

    public interface OnCrearOfertaListener {
        void onCrearOferta(Oferta oferta);
        void mostrarDialogoHora();
        void mostrarDialogoFecha();
    }

    /**
     * Chequea que las entradas sean válidas para crear una oferta
     * @return true - si la validación es correcta.
     */
    private boolean validacionEntrada() {
        boolean correcta= true;
        String precioHab, precioFin;
        precioHab= precioHabitual.getText().toString();
        precioFin= precioFinal.getText().toString();

        //Si el precio habitual es un float definido en el patron.
        if(precioHab.matches(patronFloat.pattern())) {
            //Lo convierto a tal
            precioHabLeido= Float.parseFloat(precioHab);
            if(precioFinalLeido < 0f) {
                //No es un precio válido
                errorPrecioHab.setText("El precio debe ser positivo");
                errorPrecioHab.setVisibility(View.VISIBLE);
                correcta = false;
            }
            errorPrecioHab.setVisibility(View.INVISIBLE);
        }
        else {
            errorPrecioHab.setText("El precio ingresado no es válido");
            errorPrecioHab.setVisibility(View.VISIBLE);
            correcta= false;
        }

        //Lo mismo sucede con el precio final
        if(precioFin.matches(patronFloat.pattern())) {
            precioFinalLeido= Float.parseFloat(precioFin);
            if(precioFinalLeido < 0f) {
                errorPrecioFinal.setText("El precio debe ser positivo");
                errorPrecioFinal.setVisibility(View.VISIBLE);
                correcta = false;
            }
            errorPrecioFinal.setVisibility(View.INVISIBLE);
        }
        else {
            errorPrecioFinal.setText("El precio ingresado no es válido");
            errorPrecioFinal.setVisibility(View.VISIBLE);
            correcta= false;
        }

        if(anioLeido == 0) {
            //No ingreso fecha
            errorFecha.setText("Ingrese una fecha");
            errorFecha.setVisibility(View.VISIBLE);
            correcta= false;
        }
        else
            errorFecha.setVisibility(View.INVISIBLE);

        if(horaLeida == 0) {
            //No ingreso hora
            errorHora.setText("Ingrese una hora");
            errorHora.setVisibility(View.VISIBLE);
            correcta= false;
        }
        else
            errorHora.setVisibility(View.INVISIBLE);

        //Los valores de fecha como la hora ya son chequeadas cuando se genera el diálogo
        //No obstante hay que revisar que sean posteriores a la fecha y hora actual
        if((anioLeido != 0)&&(horaLeida != 0)) {
            Calendar fechaActual = Calendar.getInstance();
            Calendar fechaElegida = Calendar.getInstance();
            fechaElegida.set(anioLeido, mesLeido, diaLeido, horaLeida, minLeido);
            if (fechaActual.compareTo(fechaElegida) >= 0) {
                errorFecha.setText("La fecha y hora debe ser la misma o posterior a la actual");
                errorFecha.setVisibility(View.VISIBLE);
                correcta = false;
            }
            else
                errorFecha.setVisibility(View.INVISIBLE);
        }
        return correcta;
    }

    private void resetErrores() {
        errorFecha.setVisibility(View.INVISIBLE);
        errorHora.setVisibility(View.INVISIBLE);
        errorPrecioFinal.setVisibility(View.INVISIBLE);
        errorPrecioHab.setVisibility(View.INVISIBLE);
    }
}

