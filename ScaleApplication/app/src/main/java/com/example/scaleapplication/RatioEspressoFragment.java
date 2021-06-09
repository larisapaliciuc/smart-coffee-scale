package com.example.scaleapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class RatioEspressoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String[] coffeeTypes = {"type 1", "type2"};
    private String mParam1;
    private String mParam2;
    private String coffee, water;

    public RatioEspressoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RatioEspressoFragment newInstance(String param1, String param2) {
        RatioEspressoFragment fragment = new RatioEspressoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =
                inflater.inflate(R.layout.fragment_ratio_espresso, container, false);
        Context context = getActivity().getApplicationContext();
        EditText coffeeGramsEditText = rootView.findViewById(R.id.espresso_coffee_textbox);
        EspressoData espressoData = new EspressoData(context);
        espressoData.saveJsonOnStorage(context);
        coffeeGramsEditText.setText(espressoData.getJsonValue(context,"coffeeGrams"));
        espressoData.setJsonValue(context,"coffeeGrams","15");
        Spinner spin = (Spinner) rootView.findViewById(R.id.coffeeTypeSpinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,coffeeTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);
        // Inflate the layout for this fragment
        TextView ratioTextView = rootView.findViewById(R.id.espresso_water_ratio_textbox);
      //  EditText waterOutputBox = rootView.findViewById(R.id.espresso_water_textbox);
        EditText coffeeTextBox = rootView.findViewById(R.id.espresso_coffee_textbox);
        Button startBrewingButton = rootView.findViewById(R.id.espresso_prep_button);

        startBrewingButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("coffee",coffeeTextBox.getText().toString());
        //    bundle.putString("water",waterOutputBox.getText().toString());
            Fragment prepEspressoFragment = new PrepEspressoFragment();
            prepEspressoFragment.setArguments(bundle);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.ratio_espresso_fragment, prepEspressoFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        //waterOutputBox.setText(Integer.toString(Integer.parseInt(coffeeTextBox.getText().toString()) * Integer.parseInt((ratioTextView.getText()).toString()))); // set water quantity based on coffee and ratio
       // coffeeTextBox.setText(Integer.toString(Integer.parseInt(waterOutputBox.getText().toString()) / Integer.parseInt((ratioTextView.getText()).toString()))); // set coffee ter quantity based on water and ratio
        coffeeTextBox.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {


            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            String originalValue = coffeeTextBox.getText().toString();

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String currentText = s.toString();
                if(coffeeTextBox.getText().length() != 0 ) {

                    int waterNeeded = Integer.parseInt(s.toString()) * Integer.parseInt((ratioTextView.getText()).toString());
                   // waterOutputBox.setText(Integer.toString(waterNeeded));


                }
            }
        });
        ratioTextView.addTextChangedListener(new TextWatcher(){

            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (coffeeTextBox.getText().length() != 0 && ratioTextView.getText().length() != 0) {
                    int waterNeeded = Integer.parseInt(s.toString()) * Integer.parseInt((coffeeTextBox.getText()).toString());
                 //   waterOutputBox.setText(Integer.toString(waterNeeded));
                }
            }
        });
        return rootView;
    }
}