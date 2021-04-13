package com.example.scaleapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PourOverActivity extends AppCompatActivity {
   // String originalValue;
    Boolean isRatioChanged = false;
    String coffee;
    String water;
    Fragment step1Fragment = new PourOverS1Fragment();
    Fragment ratioFragment = new Ratio();
    Fragment[] steps ={ratioFragment, step1Fragment};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pour_over);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.steps[0], Ratio.class, null)
                    .commit();
        }
    }
    public void f1(String s1, String s2){
         coffee = s2;
         water = s1;
    }

    public interface OnDataPass {
        public void onDataPass(String data);
    }

    public void onClickBack(View view){
        Intent intent = new Intent(PourOverActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickStartBrewing(View view){
        Button back = findViewById(R.id.back_button);
        Button next = findViewById(R.id.next_button);
        Button start = findViewById(R.id.button);
        start.setVisibility(View.INVISIBLE);
        next.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ratio_fragment, step1Fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onClickStepBack(View view){

    }

}