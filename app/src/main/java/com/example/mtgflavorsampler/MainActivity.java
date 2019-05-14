package com.example.mtgflavorsampler;

import androidx.lifecycle.Observer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


public class MainActivity extends AppCompatActivity{
    private FlavorViewModel flavorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is the code for Faves list, may not ultimately go in main activity
        /*
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        recyclerView.setHasFixedSize(true);

        final CardAdapter adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);
        flavorViewModel.getFavoriteCards().observe(this, new Observer<List<CardData>>() {
            @Override
            public void onChanged(List<CardData> favoriteCards) {
                adapter.setCards(favoriteCards);
            }
        });
        */

        final TextView nameView = findViewById(R.id.text_view_card_name);
        final TextView flavorView = findViewById(R.id.text_view_flavor);
        final TextView artistView = findViewById(R.id.text_view_artist);
        final ImageView artCropView = findViewById(R.id.image_view_art_crop);
        final ImageView cardArtView = findViewById(R.id.image_view_card_art);
        flavorViewModel = ViewModelProviders.of(this).get(FlavorViewModel.class);

        flavorViewModel.viewCard().observe(this, new Observer<CardData>() {
            @Override
            public void onChanged(CardData card) {
                nameView.setText(card.getName());
                flavorView.setText(card.getFlavorText());
                artistView.setText(card.getArtist());
                artCropView.setImageBitmap(card.getArtCrop());
                cardArtView.setImageBitmap(card.getCardArt());
            }
        });
    }

    /*
        Adding a View argument allows this method to be selected as an onClick option in the button
        XML. Alternatively we can add an onClickListener to the onCreate method for this activity.
     */
    public void requestNewCard(View view){
        flavorViewModel.requestNewCard();
    }


}


