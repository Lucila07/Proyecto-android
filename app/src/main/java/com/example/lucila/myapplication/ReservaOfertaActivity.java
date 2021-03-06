package com.example.lucila.myapplication;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import  com.example.lucila.myapplication.Datos.*;
import com.example.lucila.myapplication.Entidades.Establecimiento;
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.example.lucila.myapplication.Estado.EstadoApp;
import com.example.lucila.myapplication.http.VerificaConexion;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ReservaOfertaActivity extends AppCompatActivity  implements ServicioOfertasHttp.CallBack,ServicioUsuariosHttp.AccesoUsuarios {



    private  Toolbar toolbar;
    private  Button botonReservar;
    private Oferta oferta;
    private Usuario usuario;
    private ServicioOfertasHttp servicioOfertas;
    private ServicioUsuarios servicioUsuario= ServicioUsuariosHttp.getInstance(this,this); //deberia ser inyectado
    private Activity activity =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reserva_oferta);
        setToolbar();
        Intent intent=getIntent();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        usuario = servicioUsuario.getUsuarioLogueado();
        servicioOfertas= ServicioOfertasHttp.getInstanciaServicio(this,this);


        oferta= EstadoApp.getInstance().getOfertaActual();
        Log.d("oferta recibida "," ubicacion de of"+oferta.getUbicacion()+" precio "+oferta.getPrecioHabitual());


        if(oferta!=null){
            establecerTextos(oferta);

        }
        else{

            Toast.makeText(ReservaOfertaActivity.this, "Error al obtener la oferta de la bd", Toast.LENGTH_LONG).show();
        }

        botonReservar=(Button)findViewById(R.id.boton_reservar);


        botonReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(VerificaConexion.hayConexionInternet(activity)){
                    if (oferta == null) {
                        Toast.makeText(ReservaOfertaActivity.this, "Error al obtener la oferta de la bd", Toast.LENGTH_LONG).show();

                    } else {


                        if (usuario == null) {

                            Toast.makeText(ReservaOfertaActivity.this, "Antes de realizar una reserva debe loguearse", Toast.LENGTH_LONG).show();
                        } else {
                            String telefono = usuario.getTelefono();
                            Log.d("Reserva oferta","el tel es "+usuario.getTelefono());
                            if (telefono==null||telefono.length() < 4) {

                                generarDialogoFallo("Para poder reservar debera ingresar su telefono \n Lo podra hacer  en: 'Mi perfil->editar' ");
                            }
                            else
                            if (!cheaquerDato(usuario.getNombreApellido())) {
                                generarDialogoFallo("Para poder reservar debera ingresar nombre de usuario \n" +
                                        " Lo podra hacer  en: 'Mi perfil->editar' ");
                            }
                            else
                            if (!oferta.getEstado().equals("reservada")) {
                                generarDialogoReserva();
                            } else {

                                generarDialogoFallo("Esta oferta ya esta reservada :( ");
                            }

                        }
                    }
                }
                else {
                    Toast.makeText(ReservaOfertaActivity.this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_reserva); //encontramos la instancia de la toolbar
        setSupportActionBar(toolbar);   //la setamos a la actividad
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //este muestra el boton de volver
        getSupportActionBar().setTitle("Hace tu reserva");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    private void generarDialogoReserva(){

        AlertDialog dialogo= new AlertDialog.Builder(ReservaOfertaActivity.this)
                .setTitle("Reservar oferta")
                .setMessage("Seguro que deseas reservar esta oferta?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        servicioOfertas.reservarOferta(oferta,usuario);
                        Log.d(this.getClass().getSimpleName(), "estado oferta en la activity " + oferta.getEstado());

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void establecerTextos(Oferta oferta){

        TextView fecha= (TextView)findViewById(R.id.reserva_fecha);
        TextView ubicacion= (TextView)findViewById(R.id.reserva_ubicacion);
        TextView hora= (TextView)findViewById(R.id.reserva_hora);
        TextView deporte= (TextView)findViewById(R.id.reserva_deporte);
        TextView establecimiento_nombre= (TextView)findViewById(R.id.reserva_nombre_establecimiento);
        TextView ahorro=(TextView)findViewById(R.id.reserva_ahorro_oferta);
        TextView tv_precioHabitual=(TextView)findViewById(R.id.tv_reserva_precio_h);
        TextView tv_precioO=(TextView)findViewById(R.id.tv_reserva_precio_oferta);
        TextView tv_direccion=(TextView)findViewById(R.id.reserva_direccion);

        Establecimiento establecimiento= MapeoIdEstablecimiento.getInstance().getById(oferta.getIdUserCreador());
        establecimiento_nombre.setText(establecimiento.getNombre());
        String direccion=establecimiento.getDireccion();
        if(direccion!=null)
            tv_direccion.setText(establecimiento.getDireccion().toString());
        else tv_direccion.setText("No disponible");

        Integer precioH=oferta.getPrecioHabitual();
        Integer int_ahorro=0;
        Integer precioOfertado=oferta.getPrecioOferta();
        if( precioH>0)
            int_ahorro =new Integer((precioOfertado*100)/precioH);


        tv_precioHabitual.setText(precioH.toString());
        tv_precioO.setText(precioOfertado.toString());

        String s_ahorro=int_ahorro.toString();
        ahorro.setText(s_ahorro);

        fecha.setText(oferta.getFecha().toString());
        ubicacion.setText(oferta.getUbicacion());
        hora.setText(oferta.getHora().toString());
        deporte.setText(oferta.getDeporte().getNombre());

    }

    private void generarDialogoOk(final Activity activity){
        AlertDialog dialogo= new AlertDialog.Builder(ReservaOfertaActivity.this)
                .setTitle("Oferta reservada con exito")
                .setMessage("Tus datos seran enviados al establecimiento para que puedan contactarse")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(activity, MainActivity.class );
                        intent.putExtra("ofertaReservada",(Parcelable)oferta);
                        activity.startActivity(intent);

                    }}
                ).show();


    }

    private void generarDialogoFallo(String mensaje){
        AlertDialog dialogo= new AlertDialog.Builder(ReservaOfertaActivity.this)
                .setTitle("Reservar")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }}
                ).show();


    }

    private boolean cheaquerDato(String dato){
        if(dato==null||dato.isEmpty()||dato.equals("")||dato.equals(" "))
            return false;
        else return true;
    }

    @Override
    public  void reservaExito(){
        oferta.setEstado("reservada"); //en memoria no en bd, en bd ya esta en este punto
        oferta.setIdUserComprador(servicioUsuario.getUsuarioLogueado().getIdUsuario());//en memoria
        generarDialogoOk(this);

    }
    @Override
    public  void reservaFallo(){

        Toast.makeText(ReservaOfertaActivity.this, "Error al realizar oferta", Toast.LENGTH_LONG).show();
    }

    /**
     * metodo para dibujar la interfaz, en callback que no corresponde inplementarlo aca
     * */
    @Override
    public void dibujarListaOfertas() {

    }


    @Override
    public void cargarMain() {

    }

    @Override
    public void cargarTelefono() {

    }

}
