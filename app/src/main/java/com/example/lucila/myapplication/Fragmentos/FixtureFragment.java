package com.example.lucila.myapplication.Fragmentos;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucila.myapplication.R;

/**
 * Created by Ratan on 7/9/2015.
 */
public class FixtureFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.finpartido_layout,null);
    }
}
