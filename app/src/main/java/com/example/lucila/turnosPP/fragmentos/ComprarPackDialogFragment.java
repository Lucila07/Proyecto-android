package com.example.lucila.turnosPP.fragmentos;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Pack;

public class ComprarPackDialogFragment extends DialogFragment {

    OnDialogInteractionListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder constructor = new AlertDialog.Builder(getActivity());

        //Seteo los mensajes del dialogo
        constructor.setMessage(R.string.mensaje_dialogo_comprarPack)
                .setTitle(R.string.titulo_dialogo_comprarPack);

        //Los botones que va a tener
        constructor.setPositiveButton(R.string.confirmar_dialogo_comprarPack, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                acepto();
            }
        });
        constructor.setNegativeButton(R.string.boton_cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No hay que hacer nada, el sistema quita el dialogo
            }
        });
        //Lo creo
        return constructor.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogInteractionListener) {
            mListener = (OnDialogInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFechaElegidaListener");
        }
    }

    public interface OnDialogInteractionListener {
        void onDialogInteracion();
    }

    //El usuario acepto el dialogo
    private void acepto() {
        if(mListener != null) {
            mListener.onDialogInteracion();
        }
    }
}
