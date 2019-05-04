package com.example.mtgflavorsampler;

import android.app.Application;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class FlavorViewModel extends AndroidViewModel{
    private  CardRepository repository;
    private LiveData<List<CardData>> favoriteCards;

    public FlavorViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
        favoriteCards = repository.getAllCards();
    }

    public void insert(CardData card){
        repository.insert(card);
    }

    public void delete(CardData card){
        repository.delete(card);
    }

    public LiveData<List<CardData>> getFavoriteCards() {
        return favoriteCards;
    }
}
