package com.example.mtgflavorsampler;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class FlavorViewModel extends AndroidViewModel{
    private  CardRepository repository;
    private LiveData<List<CardData>> favoriteCards;
    private LiveData<CardData> currentCard;
    private LiveData<Bitmap> currentArtCrop = new MutableLiveData<>();
    private LiveData<Bitmap> currentCardArt = new MutableLiveData<>();

    public FlavorViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
        favoriteCards = repository.getAllCards();
        currentCard = repository.getCurrentCard();
        currentArtCrop = repository.getCurrentArtCrop();
        currentCardArt = repository.getCurrentCardArt();
    }

    public Bitmap getCurrentArtCrop(){
        return currentArtCrop.getValue();
    }

    public Bitmap getCurrentCardArt(){
        return currentCardArt.getValue();
    }

    public LiveData<CardData> viewCard(){
        return currentCard;
    }

    public void requestNewCard(){
        repository.fetchCard();
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
