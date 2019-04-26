package com.example.mtgflavorsampler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;


public class MainActivity extends AppCompatActivity {
    RetrieveDataTask dataTask = new RetrieveDataTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlavorViewModel model = ViewModelProviders.of(this).get(FlavorViewModel.class);
//        model.getCard().(this, card -> {
//           //updateUI
//        });
    }



}
