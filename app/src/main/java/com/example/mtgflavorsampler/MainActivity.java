package com.example.mtgflavorsampler;

import androidx.lifecycle.Observer;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


public class MainActivity extends AppCompatActivity{
    private FlavorViewModel flavorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flavorViewModel = ViewModelProviders.of(this).get(FlavorViewModel.class);
        flavorViewModel.getFavoriteCards().observe(this, new Observer<List<CardData>>() {
            @Override
            public void onChanged(List<CardData> favoriteCards) {
                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
            }
        });

    }
}


