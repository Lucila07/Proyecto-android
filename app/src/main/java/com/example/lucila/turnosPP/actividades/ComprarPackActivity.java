package com.example.lucila.turnosPP.actividades;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lucila.myapplication.R;
import com.example.lucila.turnosPP.beans.Pack;
import com.example.lucila.turnosPP.fragmentos.PackFragment;

import java.io.Serializable;
import java.util.Collection;

public class ComprarPackActivity
        extends AppCompatActivity
        implements PackFragment.OnEleccionPackListener {

    public static final String PACKS_DISPONIBLES = "packs_disponibles";
    public static final String ID_PACK_COMPRADO = "id_pack_comprado";
    public static final String PACK_ELEGIDO = "pack_elegido";

    private int idPackComprado;
    private Collection<Pack> mPacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_pack);

        //Recupero los packs
        if(savedInstanceState != null) {
            idPackComprado= savedInstanceState.getInt(ID_PACK_COMPRADO);
            mPacks= (Collection<Pack>) savedInstanceState.getSerializable(PACKS_DISPONIBLES);
        } else {
            idPackComprado= getIntent().getIntExtra(ID_PACK_COMPRADO, 1);
            mPacks= (Collection<Pack>) getIntent().getSerializableExtra(PACKS_DISPONIBLES);
        }

        //Por cada Pack instancio un fragmento del mismo
        FragmentTransaction fTrans= getSupportFragmentManager().beginTransaction();
        Fragment fAux;
        boolean fueComprado;
        int packId;
        idPackComprado= 2;
        for(Pack pack : mPacks) {
            packId= pack.getId();
            //El pack 0 no debería mostrarse (NullPack)
            if(packId != 1) {
                fueComprado = packId == idPackComprado;
                fAux = PackFragment.newInstance(pack, fueComprado);
                //Agrego el nuevo Fragment con un Tag igual a su id
                fTrans.add(R.id.Contenedor_ComprarPackActivity, fAux, String.valueOf(packId));
            }
        }
        fTrans.commit();
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PACKS_DISPONIBLES,(Serializable) mPacks);
        outState.putInt(ID_PACK_COMPRADO, idPackComprado);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onEleccionPack(Pack pack) {
        //Creo el Intent que contiene el pack elegido
        Intent resultado= new Intent();
        resultado.putExtra(PACK_ELEGIDO, pack);
        setResult(RESULT_OK, resultado);
        finish();
    }
}
