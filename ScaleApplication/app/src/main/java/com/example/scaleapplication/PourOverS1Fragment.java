package com.example.scaleapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;
import java.util.Random;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class PourOverS1Fragment extends Fragment {
    GraphView graph;
    int seconds, stepNumber = 0;
    int totalSteps = 9;
    String coffee,water;
    int bloomingWater, restOfWater;
    float flowRate;
     boolean isCountDownRunning = true, isTimerRunning = true;
    final int handlerState = 10;
    BluetoothConnection bluetoothConnection;
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    int[] image = {R.drawable.step1,
            R.drawable.dripper,
            R.drawable.filter,
            R.drawable.coffee,
            R.drawable.pouring,
            R.drawable.blooming,
            R.drawable.arrow,
            R.drawable.pouring,};
    String[] subtitle = new String[]{getString(R.string.subtitle1),
            getString(R.string.subtitle2),
            getString(R.string.subtitle3),
            getString(R.string.subtitle4),
            getString(R.string.subtitle5),
            getString(R.string.subtitle6),
            getString(R.string.subtitle7),
            getString(R.string.subtitle8)};
    String[] content = new String[]{getString(R.string.content1,water),
            getString(R.string.content2),
            getString(R.string.content3),
            getString(R.string.content4,coffee),
            getString(R.string.content5,bloomingWater),
            getString(R.string.content6),
            getString(R.string.content7,restOfWater),
            getString(R.string.content8,flowRate)};

    public PourOverS1Fragment() {
        super(R.layout.fragment_pour_over_s1);
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        coffee = getArguments().getString("coffee");
        water = getArguments().getString("water");
        bloomingWater = Integer.parseInt(coffee) * 2;
        restOfWater = Integer.parseInt(water)-bloomingWater;
        flowRate = restOfWater/120.f;
        View rootView = inflater.inflate(R.layout.fragment_pour_over_s1, container, false);
        TextView titleTextView = rootView.findViewById(R.id.titleTextView);
        TextView contentTextView = rootView.findViewById(R.id.contentTextView);
        TextView subtitleTextView = rootView.findViewById(R.id.subtitleTextView);
        TextView weightTextView = rootView.findViewById(R.id.weightTextView);
        TextView timeTextView = rootView.findViewById(R.id.timeTextView);
        ImageView imageView = rootView.findViewById(R.id.pourOverImage);
        Button nextButton = rootView.findViewById(R.id.next_button);
        Button backButton = rootView.findViewById(R.id.back_button);
        setText(titleTextView,subtitleTextView,contentTextView,stepNumber,totalSteps,imageView);
        backButton.setVisibility(View.INVISIBLE);
        isCountDownRunning = true;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.setVisibility(View.VISIBLE);
                stepNumber++;
                setVisibility(weightTextView,stepNumber,"weight");
                setVisibility(timeTextView,stepNumber,"time");
                if(stepNumber == 5){
                    setCountDown(timeTextView,40, isCountDownRunning);
                    bluetoothConnection.write("c");
                    isCountDownRunning = false;
                }
                if(stepNumber == 8){
                    runTimer(timeTextView);
                }
                setText(titleTextView,subtitleTextView,contentTextView,stepNumber,totalSteps,imageView);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepNumber--;
                if (stepNumber == 0){
                    backButton.setVisibility(View.INVISIBLE);
                }
                setVisibility(weightTextView,stepNumber,"weight");
                setVisibility(timeTextView,stepNumber,"time");
                setText(titleTextView,subtitleTextView,contentTextView,stepNumber,totalSteps,imageView);
            }
        });
        Handler mHandler = new Handler(Looper.getMainLooper()){
            TextView weightTextView;
            @Override
            public void handleMessage(android.os.Message msg) {
                weightTextView = rootView.findViewById(R.id.weightTextView);
                if (msg.what == handlerState) {
                    String readMessage = String.format("%s", msg.obj);
                    weightTextView.setText(readMessage + "g");
                    series.appendData(new DataPoint(lastX, Double.parseDouble(readMessage)), true, restOfWater);
                }
            }
        };
        bluetoothConnection = new BluetoothConnection();
        bluetoothConnection.setHandler(mHandler);
        bluetoothConnection.initiateBluetoothProcess();
        bluetoothConnection = BluetoothConnection.getInstance();
        // we get graph view instance
        graph = (GraphView) rootView.findViewById(R.id.idGraphView);
        // data
        series = new LineGraphSeries<>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);
        return rootView;
    }

    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setText(TextView titleTextView, TextView subtitleTextView, TextView contentTextView, int stepNumber, int totalSteps, ImageView imageView) {
        imageView.setImageDrawable(getResources().getDrawable(image[stepNumber]));
        subtitleTextView.setText(subtitle[stepNumber]);
        contentTextView.setText(content[stepNumber]);
        titleTextView.setText(getString(R.string.title,stepNumber+1,totalSteps));
        }
    public void setVisibility(TextView textView, int stepNumber,String type){
            textView.setVisibility(View.INVISIBLE);
            if ((stepNumber == 3 || stepNumber == 4 ) && type.equals("weight")){
                textView.setVisibility(View.VISIBLE);
            }
             if((stepNumber == 5 || stepNumber == 8 )&& type.equals("time")) {
                 textView.setVisibility(View.VISIBLE);
             }
             if(stepNumber == 7){
                 graph.setVisibility(View.VISIBLE);
             }
        }
    public void setCountDown(TextView textView, int seconds, boolean isRunning) {
            if (isRunning) {
                seconds = seconds * 1000;
                new CountDownTimer(seconds, 1000) {
                    String timing;
                    public void onTick(long millisUntilFinished) {
                        timing = String.format(Locale.getDefault(), "00:%02d", millisUntilFinished / 1000);
                        textView.setText(timing);
                    }
                    public void onFinish() {

                    }
                }.start();
            }
        }

    private void runTimer(TextView timeView) {
        final Handler handler
                = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run()
            {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                if(isTimerRunning) {
                    if (minutes != 2 || seconds < 0) {
                    seconds++;
                    }
                    String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                    timeView.setText(time);
                    lastX++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}