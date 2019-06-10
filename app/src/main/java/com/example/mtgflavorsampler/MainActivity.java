package com.example.mtgflavorsampler;

import androidx.fragment.app.FragmentManager;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

/**
    Main activity 
*/

public class MainActivity extends AppCompatActivity implements DisplayFragment.OnDisplayFragmentInteractionListener, ListFragment.OnListFragmentInteractionListener{
    private FlavorViewModel flavorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flavorViewModel = ViewModelProviders.of(this).get(FlavorViewModel.class);

        if(savedInstanceState == null) {  //if null this is first time creation
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame, DisplayFragment.newInstance("a", "b"))
                    .commit();
            getSupportActionBar().setTitle("MTG Flavor Sampler");
        }
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
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, ListFragment.newInstance("a", "b"))
                        .addToBackStack("faves")
                        .commit();
                return true;
            case R.id.add_to_favorites:
                boolean added = flavorViewModel.insert();
                if(added) {
                    Toast.makeText(MainActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Duplicate not added", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.back_arrow:
                getSupportFragmentManager().popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void requestNewCard(View view){
        flavorViewModel.requestNewCard();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}



