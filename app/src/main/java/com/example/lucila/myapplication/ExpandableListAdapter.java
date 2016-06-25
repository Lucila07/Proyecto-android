package com.example.lucila.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucila.myapplication.Datos.MapeoIdEstablecimiento;
import com.example.lucila.myapplication.Entidades.Establecimiento;
import com.example.lucila.myapplication.Entidades.Oferta;

import static com.example.lucila.myapplication.R.id.item_Dirección;
import static com.example.lucila.myapplication.R.id.item_Télefono;
import static com.example.lucila.myapplication.R.id.item_fecha;
import static com.example.lucila.myapplication.R.id.item_hora;
import static com.example.lucila.myapplication.R.id.item_precioOferta;

/**
 * Created by Lucila on 23/6/2016.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private Oferta[] _listDataHeader; // header titles

    ImageView imgViewIcon;

    // child data in format of header title, child title
   // private HashMap<Long, String> _listDataChild;

    public ExpandableListAdapter(Context context, Oferta[] listDataHeader ) {

        //HashMap<Long, String> listChildData--> paraametro
        this._context = context;
        this._listDataHeader = listDataHeader;
     //   this._listDataChild = listChildData;
    }

   @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return null;
       //this._listDataChild.get(this._listDataHeader[groupPosition].getCodigo());

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Oferta oferta =  getGroup(groupPosition);
        Establecimiento establecimiento= MapeoIdEstablecimiento.getInstance().getById(oferta.getIdUserCreador());
        Log.d("", "En getChildView");
        Log.d("","position");
        Log.d("", String.valueOf(groupPosition));
        Log.d("", "childPosition");
        Log.d("", String.valueOf(childPosition));
        final String childText = (String) getGroup(groupPosition).getUbicacion();
     // final String childText = (String) getChild(groupPosition, 0);

      //  final String childText="Detalle";
        Log.d("","childText");
        Log.d("",childText);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item2, null);
        }

        TextView fecha = (TextView) convertView.findViewById(item_fecha);
        TextView hora = (TextView) convertView.findViewById(item_hora);
        TextView direccionEstablecimiento = (TextView) convertView.findViewById(item_Dirección);
        TextView telefonoEstablecimiento = (TextView) convertView.findViewById(item_Télefono);
        TextView precioOferta = (TextView) convertView.findViewById(item_precioOferta);

        fecha.setText("Fecha: "+oferta.getFecha());
        hora.setText("Hora: "+oferta.getHora());
        direccionEstablecimiento.setText("Dirección: "+establecimiento.getDireccion()+", "+ establecimiento.getUbicacion());
        telefonoEstablecimiento.setText("Teléfono: "+establecimiento.getTelefono());
        precioOferta.setText("Precio de la oferta: $"+oferta.getPrecioOferta());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       // return this._listDataChild.get(_listDataHeader[groupPosition]).size();
            return 1;
    }

    @Override
    public Oferta getGroup(int groupPosition) {
    return this._listDataHeader[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
       // String headerTitle = (String) getGroup(groupPosition);

        Oferta oferta= getGroup(groupPosition);
        String deporte=oferta.getDeporte().getNombre();
        String headerTitle = deporte;
        String  Nombrecancha;
     //   String headerTitle="titulo";
        if (convertView == null) {
                    LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                     convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        // Acomodar q todas esten declaradas aca abajo
        imgViewIcon=(ImageView) convertView.findViewById(R.id.item_icon);
        //seteo el deporte
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
       // TextView cancha = (TextView) convertView.findViewById(R.id.cancha);

        lblListHeader.setTypeface(null, Typeface.BOLD);

        Establecimiento establecimiento= MapeoIdEstablecimiento.getInstance().getById(oferta.getIdUserCreador());
        lblListHeader.setText(headerTitle+" en "+ establecimiento.getNombre());
        setImagenOferta(convertView, deporte);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void setImagenOferta(View holder,String deporte ){

        int recurso=0;
        switch (deporte){
            case "futbol":
                recurso=R.drawable.futbol;
                break;
            case "Futbol":
                recurso=R.drawable.futbol;
                break;

            case "voley":
                recurso=R.drawable.voley;
                break;
            case "Voley":
                recurso=R.drawable.voley;
                break;

            case "basquet":
                recurso=R.drawable.basquet;
                break;
            case "Basquet":
                recurso=R.drawable.basquet;
                break;
            case "tenis":
                recurso=R.drawable.tenis;
                break;
            case "Tenis":
                recurso=R.drawable.tenis;
                break;
            case "paddle":
                recurso=R.drawable.paddle;
                break;
            case "Paddle":
                recurso=R.drawable.paddle;
                break;


        }
       imgViewIcon.setImageResource(recurso);

    }
}