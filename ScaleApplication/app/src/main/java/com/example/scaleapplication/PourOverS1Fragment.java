package com.example.scaleapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

import com.jjoe64.graphview.GraphView;

public class PourOverS1Fragment extends Fragment {
    GraphView graph;
    int stepNumber = 0;
    int totalSteps = 9;
    String coffee, water;
    int bloomingWater, restOfWater;
    float flowRate, previousWeight = 0.f;
    float totalLast1sec = 0.f;
    boolean isCountDownRunning = true, startSeekProgress = true;
    final int handlerState = 10;
    BluetoothConnection bluetoothConnection;


    //private LineGraphSeries<DataPoint> series;
    //private int lastX = 0;
    public PourOverS1Fragment() {
        super(R.layout.fragment_pour_over_s1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        coffee = getArguments().getString("coffee");
        water = getArguments().getString("water");
        bloomingWater = Integer.parseInt(coffee) * 2;
        restOfWater = Integer.parseInt(water) - bloomingWater;
        flowRate = restOfWater / 1200.f; // 120 - flow in 2 minutes c, 10 - calculate progress for 0.1s
        View rootView = inflater.inflate(R.layout.fragment_pour_over_s1, container, false);
        TextView titleTextView = rootView.findViewById(R.id.titleTextView);
        TextView contentTextView = rootView.findViewById(R.id.contentTextView);
        TextView subtitleTextView = rootView.findViewById(R.id.subtitleTextView);
        TextView weightTextView = rootView.findViewById(R.id.weightTextView);
        TextView timeTextView = rootView.findViewById(R.id.timeTextView);
        TextView slowTextView = rootView.findViewById(R.id.slowTextView);
        TextView fastTextView = rootView.findViewById(R.id.fastTextView);
        TextView goodTextView = rootView.findViewById(R.id.goodTextView);
        ImageView imageView = rootView.findViewById(R.id.pourOverImage);
        Button nextButton = rootView.findViewById(R.id.next_button);
        Button backButton = rootView.findViewById(R.id.back_button);
        ProgressBar pouringSeekBar = rootView.findViewById(R.id.pouringSeekBar); // initiate the progress bar
        pouringSeekBar.setMax((int) (flowRate * 100 * 2)); //set the maximum value of the seekbar as double of the flowRate
       // pouringSeekBar.getProgressDrawable().setColorFilter(R.style.SeekBarColor, PorterDuff.Mode.MULTIPLY);
        setText(titleTextView, subtitleTextView, contentTextView, stepNumber, totalSteps, imageView);
        backButton.setVisibility(View.INVISIBLE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.setVisibility(View.VISIBLE);
                stepNumber++;
                setVisibility(weightTextView, stepNumber, "weight");
                setVisibility(timeTextView, stepNumber, "time");
                if(stepNumber == 4){
                    bluetoothConnection.write("t");
                }
                if (stepNumber == 5) {
                    setCountDown(timeTextView, 40);
                    bluetoothConnection.write("c");
                }
                if (stepNumber == 7) {
                    setCountDown(timeTextView, 120);
                    isCountDownRunning = true;
                    pouringSeekBar.setVisibility(View.VISIBLE);
                    slowTextView.setVisibility(View.VISIBLE);
                    fastTextView.setVisibility(View.VISIBLE);
                    goodTextView.setVisibility(View.VISIBLE);
                }
                else{
                    pouringSeekBar.setVisibility(View.INVISIBLE);
                    slowTextView.setVisibility(View.INVISIBLE);
                    fastTextView.setVisibility(View.INVISIBLE);
                    goodTextView.setVisibility(View.INVISIBLE);
                }
                if(stepNumber == 8){
                    nextButton.setVisibility(View.INVISIBLE);
                }
                setText(titleTextView, subtitleTextView, contentTextView, stepNumber, totalSteps, imageView);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepNumber--;
                if(stepNumber != 8){
                    nextButton.setVisibility(View.VISIBLE);
                }
                if(stepNumber!=7){
                        pouringSeekBar.setVisibility(View.INVISIBLE);
                        slowTextView.setVisibility(View.INVISIBLE);
                        fastTextView.setVisibility(View.INVISIBLE);
                        goodTextView.setVisibility(View.INVISIBLE);
                }
                else{

                }
                //graph.setVisibility(View.INVISIBLE);
                if (stepNumber == 0) {
                    backButton.setVisibility(View.INVISIBLE);
                }
                setVisibility(weightTextView, stepNumber, "weight");
                setVisibility(timeTextView, stepNumber, "time");
                setText(titleTextView, subtitleTextView, contentTextView, stepNumber, totalSteps, imageView);
            }
        });

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            TextView weightTextView;
            float weightDiff;
            String readMessage;

            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(android.os.Message msg) {
                weightTextView = rootView.findViewById(R.id.weightTextView);
                if (msg.what == handlerState) {
                    readMessage = String.format("%s", msg.obj);
                    weightTextView.setText(readMessage + "g");
                    if (stepNumber == 7 && startSeekProgress) {
                        startSeekProgress = false;
                        /*series.appendData(new DataPoint(lastX++, Double.parseDouble(readMessage)), true, 10);
                        series.setBackgroundColor(Color.argb(5, 73, 33, 5));
                        graph.addSeries(series);*/
                        new CountDownTimer(120000, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(!isCountDownRunning){
                                    cancel();
                                    startSeekProgress = true;
                                }
                                weightDiff = Float.parseFloat(readMessage) - previousWeight;
                                totalLast1sec -= totalLast1sec / 10;
                                totalLast1sec += weightDiff;
                               // iterations++;
                               // sum = sum + weightDiff;
                                if (weightDiff > (flowRate * 2 ))
                                    weightDiff = flowRate * 2 ;
                                //pouringSeekBar.setProgress((int) (weightDiff * 100));
                                pouringSeekBar.setProgress((int) (totalLast1sec * 10));
                                Log.i("ProgressBar progress", Integer.toString(pouringSeekBar.getProgress()));
                                previousWeight = Float.parseFloat(readMessage);
                            }
                            @Override
                            public void onFinish() {

                            }
                        }.start();
                    }
                }
            }
        };
        bluetoothConnection = new BluetoothConnection();
        bluetoothConnection.setHandler(mHandler);
        bluetoothConnection.initiateBluetoothProcess();
        bluetoothConnection = BluetoothConnection.getInstance();
        /*graph = (GraphView) rootView.findViewById(R.id.idGraphView);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(restOfWater);
        viewport.setMaxX(lastX);
        viewport.setScrollable(true);

        series = new LineGraphSeries<>();
        series.setDrawBackground(true);
        series.setColor(0x492105);
        series.setThickness(4);*/
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            bluetoothConnection.cancelConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setText(TextView titleTextView, TextView subtitleTextView, TextView contentTextView, int stepNumber, int totalSteps, ImageView imageView) {
        int[] image = {R.drawable.step1,
                R.drawable.dripper,
                R.drawable.filter,
                R.drawable.coffee,
                R.drawable.pouring,
                R.drawable.blooming,
                R.drawable.arrow,
                R.drawable.pouring,
                R.drawable.coffee_mug};
        String[] subtitle = new String[]{getString(R.string.subtitle1),
                getString(R.string.subtitle2),
                getString(R.string.subtitle3),
                getString(R.string.subtitle4),
                getString(R.string.subtitle5),
                getString(R.string.subtitle6),
                getString(R.string.subtitle7),
                getString(R.string.subtitle8),
                getString(R.string.subtitle9)};
        String[] content = new String[]{getString(R.string.content1, water),
                getString(R.string.content2),
                getString(R.string.content3),
                getString(R.string.content4, coffee),
                getString(R.string.content5, bloomingWater),
                getString(R.string.content6),
                getString(R.string.content7, restOfWater),
                getString(R.string.content8, flowRate*10),
                getString(R.string.content9)};
        imageView.setImageDrawable(getResources().getDrawable(image[stepNumber]));
        subtitleTextView.setText(subtitle[stepNumber]);
        contentTextView.setText(content[stepNumber]);
        titleTextView.setText(getString(R.string.title, stepNumber + 1, totalSteps));
    }

    public void setVisibility(TextView textView, int stepNumber, String type) {
        textView.setVisibility(View.INVISIBLE);
        if ((stepNumber == 3 || stepNumber == 4) && type.equals("weight")) {
            textView.setVisibility(View.VISIBLE);
        }
        if ((stepNumber == 5 || stepNumber == 7) && type.equals("time")) {
            textView.setVisibility(View.VISIBLE);
        }
             /*if(stepNumber == 7){
                 graph.setVisibility(View.VISIBLE);
             }*/
    }

    public void setCountDown(TextView textView, int seconds) {
        seconds = seconds * 1000;
            new CountDownTimer(seconds, 1000) {
                String time;

                public void onTick(long millisUntilFinished) {
                    time = String.format(Locale.getDefault(), "%02d:%02d", (millisUntilFinished / 1000) / 60, (millisUntilFinished / 1000) % 60);
                    if (stepNumber == 5 || stepNumber == 7)
                        textView.setText(time);
                    else{
                        cancel();
                        textView.setText("00:00");
                        isCountDownRunning = false;
                    }
                }
                public void onFinish() {
                }
            }.start();
    }
}