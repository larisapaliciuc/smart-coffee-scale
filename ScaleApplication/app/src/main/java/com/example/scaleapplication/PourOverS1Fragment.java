package com.example.scaleapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class PourOverS1Fragment extends Fragment {
    int stepNumber = 0;
    int totalSteps = 8;
    String coffee,water;
    int bloomingWater;
    boolean isRunning = true;

    public PourOverS1Fragment() {
        super(R.layout.fragment_pour_over_s1);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        coffee = getArguments().getString("coffee");
        water = getArguments().getString("water");
        bloomingWater = Integer.parseInt(coffee) * 2;
        View rootView = inflater.inflate(R.layout.fragment_pour_over_s1, container, false);
        TextView titleTextView = rootView.findViewById(R.id.titleTextView);
        TextView contentTextView = rootView.findViewById(R.id.contentTextView);
        TextView subtitleTextView = rootView.findViewById(R.id.subtitleTextView);
        TextView weightTextView = rootView.findViewById(R.id.weightTextView);
        TextView timeTextView = rootView.findViewById(R.id.timeTextView);
        ImageView imageView = rootView.findViewById(R.id.pourOverImage);
        setText(titleTextView,subtitleTextView,contentTextView,stepNumber,totalSteps,imageView);
        Button nextButton = rootView.findViewById(R.id.next_button);
        Button backButton = rootView.findViewById(R.id.back_button);
        isRunning = true;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepNumber++;
                setVisibility(weightTextView,stepNumber,"weight");
                setVisibility(timeTextView,stepNumber,"time");
                if(stepNumber == 5){
                    setCountDown(timeTextView,40,isRunning);
                    isRunning = false;
                }
                setText(titleTextView,subtitleTextView,contentTextView,stepNumber,totalSteps,imageView);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepNumber--;
                setVisibility(weightTextView,stepNumber,"weight");
                setVisibility(timeTextView,stepNumber,"time");
                setText(titleTextView,subtitleTextView,contentTextView,stepNumber,totalSteps,imageView);
            }
        });
        return rootView;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setText(TextView titleTextView, TextView subtitleTextView, TextView contentTextView, int stepNumber, int totalSteps, ImageView imageView) {
        int[] image = {R.drawable.step1,
                R.drawable.dripper,
                R.drawable.filter,
                R.drawable.coffee,
                R.drawable.pouring,
                R.drawable.blooming};
        String[] subtitle = new String[]{getString(R.string.subtitle1),
                getString(R.string.subtitle2),
                getString(R.string.subtitle3),
                getString(R.string.subtitle4),
                getString(R.string.subtitle5),
                getString(R.string.subtitle6)};
        String[] content = new String[]{getString(R.string.content1,water),
                getString(R.string.content2),getString(R.string.content3),
                getString(R.string.content4,coffee),
                getString(R.string.content5,bloomingWater),
                getString(R.string.content6)};
        imageView.setImageDrawable(getResources().getDrawable(image[stepNumber]));
        subtitleTextView.setText(subtitle[stepNumber]);
        contentTextView.setText(content[stepNumber]);
        titleTextView.setText(getString(R.string.title,stepNumber+1,totalSteps));
        }
        public void setVisibility(TextView textView, int stepNumber,String type){
            textView.setVisibility(View.INVISIBLE);
            if (stepNumber ==4 && type.equals("weight")){
                textView.setVisibility(View.VISIBLE);
            }
             if(stepNumber == 5 && type.equals("time")) {
                 textView.setVisibility(View.VISIBLE);
             }
        }
        public void setCountDown(TextView textView, int seconds, boolean isRunning) {
            if (isRunning) {
                seconds = seconds * 1000;
                new CountDownTimer(seconds, 1000) {
                    String time;
                    public void onTick(long millisUntilFinished) {
                        time = String.format(Locale.getDefault(), "00:%02d", millisUntilFinished / 1000);
                        textView.setText(time);
                    }

                    public void onFinish() {
                    }
                }.start();
            }
        }
}