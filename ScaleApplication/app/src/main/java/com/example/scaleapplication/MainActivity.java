package com.example.scaleapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickRemoteScale(View view){
        Intent intent = new Intent(MainActivity.this, RemScaleActivity.class);
        startActivity(intent);
    }

    public void onClickPourOver(View view){
        Intent intent = new Intent(MainActivity.this, PourOverActivity.class);
        startActivity(intent);
    }


}