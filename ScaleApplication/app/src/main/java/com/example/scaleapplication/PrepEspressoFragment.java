package com.example.scaleapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PrepEspressoFragment extends Fragment {

    private static final String COFFEE = "coffee";
    private static final String WATER = "water";
    private String getCoffee;
    private String getWater;

    public PrepEspressoFragment() {
        // Required empty public constructor
    }

    public static PrepEspressoFragment newInstance(String coffee, String water) {
        PrepEspressoFragment fragment = new PrepEspressoFragment();
        Bundle args = new Bundle();
        args.putString(COFFEE, coffee);
        args.putString(WATER, water);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getCoffee = getArguments().getString(COFFEE);
            getWater = getArguments().getString(WATER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_prep_espresso, container, false);
        TextView textbox = rootView.findViewById(R.id.textBox);
        textbox.setText(getCoffee);

        // Inflate the layout for this fragment
        return rootView;
    }
}