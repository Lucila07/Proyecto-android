package com.example.lucila.turnosPP.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Pack;
import com.example.lucila.turnosPP.constantes.Constantes;
import com.example.lucila.turnosPP.fragmentos.ComprarPackDialogFragment;
import com.example.lucila.turnosPP.fragmentos.PackFragment;

import java.io.Serializable;
import java.util.Collection;

public class ComprarPackActivity
        extends AppCompatActivity
        implements PackFragment.OnEleccionPackListener, ComprarPackDialogFragment.OnDialogInteractionListener {

    private int idPackComprado;
    private Collection<Pack> mPacks;
    private Pack packElegido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_pack);

        //Recupero los packs
        if(savedInstanceState != null) {
            idPackComprado= savedInstanceState.getInt(Constantes.ID_PACK_COMPRADO);
            mPacks= (Collection<Pack>) savedInstanceState.getSerializable(Constantes.PACKS_DISPONIBLES);
            packElegido= (Pack) savedInstanceState.getSerializable(Constantes.PACK_ELEGIDO);
        } else {
            idPackComprado= getIntent().getIntExtra(Constantes.ID_PACK_COMPRADO, 1);
            mPacks= (Collection<Pack>) getIntent().getSerializableExtra(Constantes.PACKS_DISPONIBLES);
        }

        //Por cada Pack instancio un fragmento del mismo
        FragmentTransaction fTrans= getSupportFragmentManager().beginTransaction();
        boolean estaVacio= fTrans.isEmpty();
        Fragment fAux;
        boolean fueComprado;
        int packId;
        for(Pack pack : mPacks) {
            packId= pack.getId();
            //El pack 1 no debería mostrarse (NullPack)
            if(packId != 1) {
                fueComprado = packId == idPackComprado;
                fAux = PackFragment.newInstance(pack, fueComprado);
                //Agrego el nuevo Fragment con un Tag igual a su id
                if(estaVacio)
                    fTrans.add(R.id.Contenedor_ComprarPackActivity, fAux, String.valueOf(packId));
                else {
                    fTrans.replace(R.id.Contenedor_ComprarPackActivity, fAux, String.valueOf(packId));
                    estaVacio= true;
                }
            }
        }
        fTrans.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constantes.PACKS_DISPONIBLES,(Serializable) mPacks);
        outState.putSerializable(Constantes.PACK_ELEGIDO, packElegido);
        outState.putInt(Constantes.ID_PACK_COMPRADO, idPackComprado);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onEleccionPack(Pack pack) {
        //Me guardo el pack que eligio en caso de que acepte la compra
        packElegido= pack;
        //Crea un dialogo para confirmar la compra
        new ComprarPackDialogFragment().show(getSupportFragmentManager(),"DialogoComprar");
    }

    @Override
    public void onDialogInteracion() {
        //El usuario acepto la compra
        if(packElegido != null)
            crearIntentResultado(packElegido);
        else //Ocurrio un error y tiene que volver a elegir
            Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
    }

    //region PRIVATE_METHODS
    private void crearIntentResultado(Pack pack) {
        //Creo el Intent que contiene el pack elegido
        Intent resultado= new Intent();
        resultado.putExtra(Constantes.PACK_ELEGIDO, pack);
        setResult(RESULT_OK, resultado);
        finish();
    }
    //endregion
}
