package com.example.scaleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

public class RemScaleActivity extends AppCompatActivity {
    private int seconds;
    boolean isRunning = false;
    final int handlerState = 10;
    BluetoothConnection bluetoothConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rem_scale);
        ImageButton startButton = findViewById(R.id.runButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = true;
                bluetoothConnection.write("s");
            }
        });
        runTimer();
        Button tareButton = findViewById(R.id.tareButton);
        tareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.write("t"); // call tare function from arduino
            }
        });
    }
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        TextView weightTextView;
        @Override
        public void handleMessage(android.os.Message msg) {
            weightTextView = findViewById(R.id.scaleWeightTextView);
            if (msg.what == handlerState) {
                String readMessage = (String) msg.obj;
                weightTextView.setText(String.format("%sg", readMessage));
            }
            if(isRunning) {
                try {

                } catch (Exception ex) {
                    ex.getStackTrace();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothConnection = new BluetoothConnection();
        bluetoothConnection.setHandler(mHandler);
        bluetoothConnection.initiateBluetoothProcess();
        bluetoothConnection = BluetoothConnection.getInstance();

    }


    private void runTimer() {
        // Get the text view.

        final TextView timeView = findViewById(R.id.timerTextView);
        // Creates a new Handler
        final Handler handler
                = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run()
            {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%02d:%02d",
                                minutes, secs);

                timeView.setText(time);
                if(isRunning) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickReset(View view){
        isRunning = false;
        seconds = 0;
        bluetoothConnection.write("r");
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
    public void onClickBack(View view){
        Intent intent = new Intent(RemScaleActivity.this, MainActivity.class);
        startActivity(intent);
    }
}