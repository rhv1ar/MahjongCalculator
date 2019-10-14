package com.yoshino.mahjong.calculator.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ParameterFragment extends Fragment {
    private static final String TAG = ParameterFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.v(TAG, "onCreateView");



        //View view = inflater.inflate(R.layout.fragment_parameter, container, false);
        //View view = binding.getRoot();
        //getViews(view);

        return container;
    }

    private void getViews(final View view) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

    }



    /**/




}
