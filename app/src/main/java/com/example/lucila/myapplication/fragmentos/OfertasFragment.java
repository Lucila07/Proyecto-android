package com.example.lucila.myapplication.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucila.myapplication.MyOfertaRecyclerViewAdapter;
import com.example.lucila.myapplication.R;
import com.example.lucila.myapplication.beans.Oferta;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OfertasFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    private ArrayList<Oferta> ofertas;

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OfertasFragment newInstance(Oferta[] ofertas) {
        OfertasFragment fragment = new OfertasFragment();
        Bundle args = new Bundle();
        args.putSerializable("ofertas", ofertas);
        fragment.setArguments(args);
        return fragment;
    }

    public OfertasFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            Oferta[] aux= (Oferta[]) getArguments().get("ofertas");
            ofertas= new ArrayList<Oferta>(Arrays.asList(aux));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oferta_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyOfertaRecyclerViewAdapter(ofertas, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Oferta item);
    }
}
