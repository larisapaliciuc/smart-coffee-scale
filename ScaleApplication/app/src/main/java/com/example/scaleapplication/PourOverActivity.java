package com.example.scaleapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class PourOverActivity extends AppCompatActivity {
    String coffee,water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pour_over);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.ratio_fragment, RatioPourOverFragment.class, null)
                    .commit();
        }
        Button startBrewingButton = findViewById(R.id.button);
        startBrewingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startBrewingButton.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("coffee",coffee);
                bundle.putString("water",water);
                Fragment step1Fragment = new PourOverS1Fragment();
                step1Fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.ratio_fragment, step1Fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    public void getValues(String s1, String s2){
         coffee = s2;
         water = s1;
    }

    public void onClickBack(View view){
        Intent intent = new Intent(PourOverActivity.this, MainActivity.class);
        startActivity(intent);
    }



}