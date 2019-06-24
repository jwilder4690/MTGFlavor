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
    private MutableLiveData<CardData> currentCard;

    public FlavorViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
        favoriteCards = repository.getAllCards();
        currentCard = repository.getCurrentCard();
        Log.i("DEBUG", "FlavorViewModel constructed.");
    }

    public Bitmap getCurrentArtCrop(){
        return repository.getCurrentArtCrop();
    }

    public Bitmap getCurrentCardArt(){
        return repository.getCurrentCardArt();
    }

    public CardData getCurrentCard(){ return currentCard.getValue(); }

    public LiveData<CardData> viewCard(){
        return currentCard;
    }

    public void requestNewCard(){
        repository.fetchCard();
    }

    public void setCurrentCard(CardData card){
        repository.displayCard(card);
        //currentCard = repository.getCurrentCard();
    }

    //List Operations
    public boolean insert(){
        List<CardData> cards = repository.getList();
        for(int i = 0; i < cards.size(); i++ ){
            //Checks if card of same name exists in database
            Log.i("DEBUG", cards.get(i).getName());
            if(cards.get(i).getName().equalsIgnoreCase(currentCard.getValue().getName())) return false;
        }
        repository.insert();
        return true;
    }

    public void updateRange(int start, int end){
        repository.updateRange(start,end);
    }

    public void update(CardData card){ repository.update(card);}

    public void delete(CardData card){
        repository.delete(card);
    }

    public void swap(int start, int end){
        repository.swap(start, end);
    }

    public LiveData<List<CardData>> getFavoriteCards() {
        return favoriteCards;
    }
}
