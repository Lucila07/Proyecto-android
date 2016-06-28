package com.example.lucila.turnosPP.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Oferta;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class CrearOfertasFragment
        extends Fragment
        implements View.OnClickListener {

    private OnCrearOfertaListener mListener;

    private TextView editTextFecha, editTextHora;
    private EditText precioHabitual, precioFinal;

    //Valores ingresados por el usuario
    private int diaLeido, mesLeido, anioLeido, horaLeida, minLeido;
    private int idDeporteElegido;
    private float precioHabLeido, precioFinalLeido;
    private Calendar fechaCalendario;

    //Oferta a editar
    private Oferta aEditar;

    //Textos de error
    private TextView errorFecha, errorHora, errorPrecioHab, errorPrecioFinal;

    //Patron de un número float
    private Pattern patronFloat;

    //Usado por el adapter del AutoCompleteTextField
    //Para sugerir deportes cargados
    private String[] deportes;

    public CrearOfertasFragment() {}

    public static CrearOfertasFragment newInstance(String[] deportes, boolean editar, Oferta oferta) {
        CrearOfertasFragment fragment = new CrearOfertasFragment();
        Bundle args = new Bundle();
        args.putSerializable("listaDeportes", deportes);
        if(editar)
            args.putSerializable("ofertaEditar",oferta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deportes = getArguments().getStringArray("listaDeportes");
        aEditar= (Oferta) getArguments().getSerializable("ofertaEditar");
        patronFloat= Pattern.compile("[0-9]*\\.?[0-9]+");
        fechaCalendario= Calendar.getInstance();
        anioLeido = -1;
        horaLeida = -1;
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
        Spinner spinner = (Spinner) fragmentLayout.findViewById(R.id.deportes_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.support_simple_spinner_dropdown_item, deportes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        boolean encontre = false;
        int i = 0;
        if(aEditar != null) {
            while (!encontre)
                if (aEditar.getNombreDeporte().equals(deportes[i]))
                    encontre = true;
                else i++;
        }
        final int j= i;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setIdDeporteSeleccionado(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(aEditar != null) {
                    setIdDeporteSeleccionado(j);
                }
                else
                    setIdDeporteSeleccionado(0);
            }
        });

        precioFinal= (EditText) fragmentLayout.findViewById(R.id.edittext_precioFinal);
        precioHabitual= (EditText) fragmentLayout.findViewById(R.id.edittext_precioHabitual);

        errorFecha= (TextView) fragmentLayout.findViewById(R.id.error_fecha_crearOferta);
        errorHora= (TextView) fragmentLayout.findViewById(R.id.error_hora_crearOferta);
        errorPrecioFinal= (TextView) fragmentLayout.findViewById(R.id.error_precioFinal_crearOferta);
        errorPrecioHab= (TextView) fragmentLayout.findViewById(R.id.error_precioHab_crearOferta);

        if(aEditar != null) {
            anioLeido= aEditar.getFecha().get(Calendar.YEAR);
            horaLeida= aEditar.getFecha().get(Calendar.HOUR);
            spinner.setSelection(j);
            SimpleDateFormat formatoFecha= new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoHora= new SimpleDateFormat("hh:mm");
            editTextFecha.setText(formatoFecha.format(aEditar.getFecha().getTime()));
            editTextHora.setText(formatoHora.format(aEditar.getFecha().getTime()));
            precioHabitual.setText(Float.toString(aEditar.getPrecioHabitual()));
            precioFinal.setText(Float.toString(aEditar.getPrecioFinal()));
            fechaCalendario= aEditar.getFecha();
            botonCrear.setText(getString(R.string.boton_editar));
            if(aEditar.getIdComprador() == null) { //Nadie la reservo
                fragmentLayout.findViewById(R.id.boton_eliminar_ofertas_fragment).setVisibility(View.VISIBLE);
                fragmentLayout.findViewById(R.id.boton_eliminar_ofertas_fragment).setOnClickListener(this);
            }
        }

        return fragmentLayout;
    }

    private void setIdDeporteSeleccionado(int id) {
        idDeporteElegido= id;
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
            switch(view.getId()) {
                case R.id.boton_crear_ofertas_fragment: {
                    if(validacionEntrada()) {
                        Oferta oferta = new Oferta();
                        if(aEditar != null) {
                            oferta.setCodigo(aEditar.getCodigo());
                        }
                        oferta.setPrecioFinal(precioFinalLeido);
                        oferta.setPrecioHabitual(precioHabLeido);
                        oferta.setNombreDeporte(deportes[idDeporteElegido]);
                        //El spinner usa numeros del 0 a n
                        //oferta.setIdDeporte(idDeporteElegido + 1);
                        oferta.setFecha(fechaCalendario);
                        resetErrores();
                        if(aEditar != null)
                            mListener.onCrearOferta(oferta, true);
                        else mListener.onCrearOferta(oferta, false);
                    }
                    break;
                }
                case R.id.panel_fecha: {
                    mListener.mostrarDialogoFecha();
                    break;
                }
                case R.id.panel_hora: {
                    mListener.mostrarDialogoHora();
                    break;
                }
                case R.id.boton_eliminar_ofertas_fragment: {
                    mListener.eliminarOferta(aEditar.getCodigo());
                    break;
                }
            }
        }
    }

    public void setFecha(int dia, int mes, int anio) {
        diaLeido= dia;
        mesLeido= mes;
        anioLeido= anio;
        fechaCalendario.set(
                anio,
                mes,
                dia,
                fechaCalendario.get(Calendar.HOUR),
                fechaCalendario.get(Calendar.MINUTE)
        );
        SimpleDateFormat formatoFecha= new SimpleDateFormat("dd/MM/yyyy");
        this.editTextFecha.setText(formatoFecha.format(fechaCalendario.getTime()));
    }

    public void setHora(int hora, int minuto) {
        horaLeida= hora;
        minLeido= minuto;
        fechaCalendario.set(
                fechaCalendario.get(Calendar.YEAR),
                fechaCalendario.get(Calendar.MONTH),
                fechaCalendario.get(Calendar.DAY_OF_MONTH),
                hora,
                minuto
        );
        SimpleDateFormat formatoHora= new SimpleDateFormat("hh:mm");
        this.editTextHora.setText(formatoHora.format(fechaCalendario.getTime()));
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

        if(anioLeido < 0) {
            //No ingreso fecha
            errorFecha.setText("Ingrese una fecha");
            errorFecha.setVisibility(View.VISIBLE);
            correcta= false;
        }
        else
            errorFecha.setVisibility(View.INVISIBLE);

        if(horaLeida < 0) {
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
            if (fechaActual.compareTo(fechaCalendario) >= 0) {
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

    public interface OnCrearOfertaListener {
        void onCrearOferta(Oferta oferta, boolean editar);
        void mostrarDialogoHora();
        void mostrarDialogoFecha();
        void eliminarOferta(int codigo);
    }
}

