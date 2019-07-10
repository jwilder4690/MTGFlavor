package com.justindwilder.tastymtg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

/**
 * This is the main activity and starting point for this application. This application was designed
 * with the single activity, multiple fragments architecture. This allows the fragments to share the
 * same single ViewModel, having consistent data across all views and pages within the app.
 *
 * The layout consists of a frame which holds the fragment for the current page of navigation.
 */
public class MainActivity extends AppCompatActivity{
    private FlavorViewModel flavorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flavorViewModel = ViewModelProviders.of(this).get(FlavorViewModel.class);

        if(savedInstanceState == null) {  //if null this is first time creation
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame, new DisplayFragment())
                    .commit();
            getSupportActionBar().setTitle("Tasty MTG");
        }
    }

    /**
     * Method creates the toolbar navigation layout.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_favorites, menu);
        return true;
    }

    /**
     * Method handles the button presses that originate from the options bar.
     * @param item Menu icon which was pressed.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.list_favorites:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new ListFragment())
                        .addToBackStack("faves")
                        .commit();
                return true;
            case R.id.home_button:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new DisplayFragment())
                        .addToBackStack("home")
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method for requesting new card. This method submits the request to the viewModel, as well as
     * resets the scrollView and the favorite button icon.
     * @param view
     */
    public void requestNewCard(View view){
        flavorViewModel.requestNewCard();
        ImageButton favoriteButton = findViewById(R.id.add_to_favorites);
        favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);
    }

    /**
     * Method for adding a card to favorites. If insertion is successful, icon in favorite button is
     * updated to filled heart. Toast message will pop up and tell the user if successful or not.
     * Insertion will only fall if card name is same as card in current favorites list, per logic in
     * repository.
     * @param view
     */
    public void addToFavorites(View view){
        boolean added = flavorViewModel.insert();
        if(added) {
            Toast.makeText(MainActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
            ImageButton favoriteButton = view.findViewById(R.id.add_to_favorites);
            favoriteButton.setImageResource(R.drawable.ic_favorite);
        }
        else{
            Toast.makeText(MainActivity.this, "Duplicate not added", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method starts intent to go to the Scryfall webpage for this card.
     * @param view
     */
    public void goToWeb(View view){
        CardData myCard = flavorViewModel.viewCard().getValue();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myCard.getWebUrl()));
        startActivity(browserIntent);
        Toast.makeText(MainActivity.this, "Going to Scryfall", Toast.LENGTH_SHORT).show();
    }
}



