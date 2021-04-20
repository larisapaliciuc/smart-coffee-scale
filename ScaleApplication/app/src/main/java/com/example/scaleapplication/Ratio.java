package com.example.scaleapplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class Ratio extends Fragment {

    public Ratio() {
        super(R.layout.fragment_ratio);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =
                inflater.inflate(R.layout.fragment_ratio, container, false);
        // Inflate the layout for this fragment
        TextView ratioTextView = rootView.findViewById(R.id.water_ratio_textbox);
        EditText waterOutputBox = rootView.findViewById(R.id.water_textbox);
        EditText coffeeTextBox = rootView.findViewById(R.id.coffee_textbox);
        waterOutputBox.setText(Integer.toString(Integer.parseInt(coffeeTextBox.getText().toString()) * Integer.parseInt((ratioTextView.getText()).toString())));
        coffeeTextBox.setText(Integer.toString(Integer.parseInt(waterOutputBox.getText().toString()) / Integer.parseInt((ratioTextView.getText()).toString())));
        PourOverActivity m1 = (PourOverActivity) getActivity();
        m1.f1(waterOutputBox.getText().toString(),coffeeTextBox.getText().toString());
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
                    waterOutputBox.setText(Integer.toString(waterNeeded));
                    PourOverActivity m1 = (PourOverActivity) getActivity();
                    m1.f1(waterOutputBox.getText().toString(),coffeeTextBox.getText().toString());
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
                    waterOutputBox.setText(Integer.toString(waterNeeded));
                    PourOverActivity m1 = (PourOverActivity) getActivity();
                    m1.f1(waterOutputBox.getText().toString(),coffeeTextBox.getText().toString());
                }
            }
        });
        /*waterOutputBox.addTextChangedListener(new TextWatcher(){

            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                    if (waterOutputBox.getText().length() != 0 && ratioTextView.getText().length() != 0) {
                        int coffeeNeeded = Integer.parseInt(s.toString()) / Integer.parseInt((ratioTextView.getText()).toString());
                        coffeeTextBox.setText(Integer.toString(coffeeNeeded));
                        isRatioChanged = false;
                    } else {
                        return;
                    }
            }
        });*/
        return rootView;
    }

}