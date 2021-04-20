package com.example.scaleapplication;

import android.os.Handler;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ScaleTimer {
    private boolean isRunning;
    private long seconds;
    private long minutes;

    public ScaleTimer(){
        this.seconds = 0;
        this.minutes = 0;

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void startTimer(TextView timeTextView, boolean isRunning) throws InterruptedException {
        final Handler handler
                = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run()
            {
                minutes = (seconds % 3600) / 60;
                seconds = seconds % 60;
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%02d:%02d",
                                minutes, seconds);

                timeTextView.setText(time);
                if(isRunning)
                    seconds++;
                handler.postDelayed(this, 1000);
            }
        });
    }
    public void resetTimer(TextView timeTextView, boolean isRunning){
        if(!isRunning)
        timeTextView.setText(R.string.text_timer);
    }
}
