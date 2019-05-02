package com.example.mtgflavorsampler;

import androidx.lifecycle.Observer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlavorViewModel model = ViewModelProviders.of(this).get(FlavorViewModel.class);
        model.getCard().observe(this, new Observer<Card>() {
            @Override
            public void onChanged(Card card) {
                //update UI
            }
        });

    }
}


