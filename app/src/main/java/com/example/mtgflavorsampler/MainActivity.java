package com.example.mtgflavorsampler;

import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


public class MainActivity extends AppCompatActivity{
    private FlavorViewModel flavorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                artCropView.setImageBitmap(flavorViewModel.getCurrentArtCrop());
                cardArtView.setImageBitmap(flavorViewModel.getCurrentCardArt());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.list_favorites:
                Intent intent = new Intent( MainActivity.this, DisplayFavoritesActivity.class);
                startActivity(intent);
                return true;
            case R.id.add_to_favorites:
                flavorViewModel.insert();
                Toast.makeText(MainActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



