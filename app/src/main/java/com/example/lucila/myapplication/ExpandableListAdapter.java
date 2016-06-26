package com.example.lucila.myapplication;

import android.content.Context;
import android.graphics.Typeface;
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
    private ImageView imgViewIcon;


    public ExpandableListAdapter(Context context, Oferta[] listDataHeader ) {
        this._context = context;
        this._listDataHeader = listDataHeader;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Oferta oferta =  getGroup(groupPosition);
        Establecimiento establecimiento= MapeoIdEstablecimiento.getInstance().getById(oferta.getIdUserCreador());

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_detalle, null);
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
        return 1;
    }

    @Override
    public Oferta getGroup(int groupPosition) {

        return this._listDataHeader[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
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
        Oferta oferta= getGroup(groupPosition);
        String deporte=oferta.getDeporte().getNombre();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        imgViewIcon=(ImageView) convertView.findViewById(R.id.item_icon);
        //seteo el deporte
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);

        Establecimiento establecimiento= MapeoIdEstablecimiento.getInstance().getById(oferta.getIdUserCreador());
        lblListHeader.setText(deporte+" en "+ establecimiento.getNombre());
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