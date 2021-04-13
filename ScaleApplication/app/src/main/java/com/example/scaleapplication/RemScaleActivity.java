package com.example.scaleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class RemScaleActivity extends AppCompatActivity {
    private int seconds;
    TextView textView;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rem_scale);
        runTimer();
    }

    public void onClickStart(View view) {

        isRunning = true;
    }
    private void runTimer() {

        // Get the text view.
        final TextView timeView
                = (TextView)findViewById(
                R.id.timer_text);

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
                if(isRunning)
                    seconds++;
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickReset(View view)
    {
        isRunning = false;
        seconds = 0;
    }
    public void onClickBack(View view){
        Intent intent = new Intent(RemScaleActivity.this, MainActivity.class);
        startActivity(intent);
    }
}