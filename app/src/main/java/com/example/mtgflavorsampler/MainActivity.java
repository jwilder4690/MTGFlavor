package com.example.mtgflavorsampler;

import androidx.lifecycle.Observer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity{
    private FlavorViewModel flavorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is the code for Faves list, may not ultimately go in main activity
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        recyclerView.setHasFixedSize(true);

        final CardAdapter adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);

        flavorViewModel = ViewModelProviders.of(this).get(FlavorViewModel.class);
        flavorViewModel.getFavoriteCards().observe(this, new Observer<List<CardData>>() {
            @Override
            public void onChanged(List<CardData> favoriteCards) {
                adapter.setCards(favoriteCards);
            }
        });

    }
}


