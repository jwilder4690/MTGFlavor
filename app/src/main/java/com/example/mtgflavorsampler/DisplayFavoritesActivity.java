package com.example.mtgflavorsampler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Observer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class DisplayFavoritesActivity extends AppCompatActivity {
    private FlavorViewModel flavorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_favorites);

        /*
            Menu Bar
            This is the uppermost menu bar. Main activity is set as the parent in the XML file, so
            the setHomeAsUpIndicator will return us to that page.
         */
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_back);
        setTitle("Favorites");

        /*
            Recycler View
            This is the list structure provided by android. This allow us to view our list of favorite
            cards. This structure observes liveData, so any changes to the driving data structure will
            automatically be updated to the screen.
         */
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                flavorViewModel.delete(adapter.getCardAt(viewHolder.getAdapterPosition()));
                Toast.makeText(DisplayFavoritesActivity.this, "Card Removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

}
