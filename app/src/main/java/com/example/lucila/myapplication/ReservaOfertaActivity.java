package com.example.lucila.myapplication;
import android.app.Activity;
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
import com.example.lucila.myapplication.Entidades.Oferta;
import com.example.lucila.myapplication.Entidades.Usuario;


public class ReservaOfertaActivity extends AppCompatActivity  implements ServicioOfertasHttp.CallBack {
    private TextView textview_codigo;
    private  Long id_oferta;
    private  Toolbar toolbar;
    private  Button botonReservar;
    private Oferta oferta;
    private Usuario usuario;
    private ServicioOfertasHttp servicioOfertas;
    private ServicioUsuarios servicioUsuario= new ServicioUsuarioLista(); //deberia ser inyectado
    private ReservaOfertaActivity actividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actividad=this;
        setContentView(R.layout.reserva_oferta);
        setToolbar();
        Intent intent=getIntent();
        id_oferta=intent.getExtras().getLong("id_oferta");
       // servicioOfertas=intent.getExtras().getParcelable("ServicioHttp");
        servicioOfertas= ServicioOfertasHttp.getInstanciaServicio(this,this);
        servicioUsuario= new ServicioUsuarioLista();
        //oferta= servicioOfertas.getOfertaCodigo(id_oferta);
        oferta= (Oferta)intent.getExtras().getParcelable("oferta");

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

                if (oferta == null) {
                    Toast.makeText(ReservaOfertaActivity.this, "Error al obtener la oferta de la bd", Toast.LENGTH_LONG).show();

                }
                else
                {
                    usuario = servicioUsuario.getUsuarioLogueado();//TODO generar los usuarios


                    if (usuario == null)
                    {

                        Toast.makeText(ReservaOfertaActivity.this, "Antes de realizar una reserva debe loguearse", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if (usuario.getTelefono() == 0) {

                            Toast.makeText(ReservaOfertaActivity.this, "Antes de realizar una reserva de ingresar el telefono", Toast.LENGTH_LONG).show();
                        }
                        if (!oferta.getEstado().equals("reservada")) {
                            generarDialogoReserva();
                        } else {

                            Toast.makeText(ReservaOfertaActivity.this, "Oferta ya reservada", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        });

    }


    public void setToolbar(){
        //toolbar-------------
        toolbar = (Toolbar) findViewById(R.id.toolbar_reserva); //encontramos la instancia de la toolbar
        setSupportActionBar(toolbar);   //la setamos a la actividad


//        if (getSupportActionBar() != null) { // Habilitar up button para volcver atras

            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //este muestra el boton de volver
           // getSupportActionBar().setDisplayShowHomeEnabled(true);//para volver atras
        //}


        getSupportActionBar().setTitle("Hace tu reserva");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //--------------------

    }

    private void generarDialogoReserva(){

        AlertDialog dialogo= new AlertDialog.Builder(ReservaOfertaActivity.this)
                .setTitle("Reservar oferta")
                .setMessage("Seguro que deseas rervar esta oferta?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                     boolean resultado=servicioOfertas.reservarOferta(oferta,usuario);
                        Log.d(this.getClass().getSimpleName(), "estado oferta en la activity " + oferta.getEstado());
                      //  resultado= oferta.getEstado().equals("reservada");
                     /*   if(resultado){
                         Toast.makeText(ReservaOfertaActivity.this, "Reserva hecha con exito. Sus datos seran enviados al establecimiento", Toast.LENGTH_SHORT).show();
                         generarDialogoOk();
                         oferta.setIdUserComprador(servicioUsuario.getUsuarioLogueado().getIdUsuario());
                      }
                        else {

                          Toast.makeText(ReservaOfertaActivity.this, "Error al realizar oferta", Toast.LENGTH_LONG).show();

                      }*/
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

    fecha.setText(oferta.getFecha().toString());
    ubicacion.setText(oferta.getUbicacion());
    hora.setText(oferta.getHora().toString());
   // deporte.setText(oferta.getDeporte().getNombre());

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

    @Override
    public  void reservaExito(){
        oferta.setEstado("reservada");
        oferta.setIdUserComprador(servicioUsuario.getUsuarioLogueado().getIdUsuario());
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


}
