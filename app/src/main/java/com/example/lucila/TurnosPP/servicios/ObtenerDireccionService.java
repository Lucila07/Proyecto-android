package com.example.lucila.turnosPP.servicios;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Obtiene unsa direccion física a traves de una direccion geográfica.
 */
public class ObtenerDireccionService extends IntentService {

    protected ResultReceiver mReceiver;

    public ObtenerDireccionService() {
        super("ObtenerDireccionService");
    }

    public static void startService(Context context, ResultReceiver receiver, Location locacion) {
        Intent intent = new Intent(context, ObtenerDireccionService.class);
        intent.putExtra("locacion", locacion);
        intent.putExtra("receiver", receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mReceiver= intent.getParcelableExtra("receiver");
        //Crea un geodecodificador con el locale por defecto
        Geocoder deco= new Geocoder(this, Locale.getDefault());
        //Obtengo la posicion geografica que me manda la actividad
        Location locacion= intent.getParcelableExtra("locacion");

        List<Address> direcciones= null;

        try {
            direcciones= deco.getFromLocation(locacion.getLatitude(), locacion.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(direcciones == null || direcciones.size() == 0) {
            //Falló la geolocacion
            devolverResultado(1, "Fallo");
        }
        else {
            Address address = direcciones.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            String u;
            //Direccion
            String dir= TextUtils.join(System.getProperty("line.separator"), addressFragments);
            dir= "";
            //Ciudad
            String ciudad= address.getLocality();
            if(dir.isEmpty())
                devolverResultado(0, ciudad);
            else
                devolverResultado(0, dir + " ; " + ciudad);
        }
    }
    private void devolverResultado(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString("resultado", message);
        mReceiver.send(resultCode, bundle);
    }
}
