package com.justindwilder.tastymtg;

import android.app.Application;
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
    }

    /**
     * Tracks current card.
     * @return This method returns LiveData, so changes to the current card in the repository will be
     * visible and observable here.
     */
    public LiveData<CardData> viewCard(){
        return currentCard;
    }

    public void requestNewCard(){
        repository.fetchCard();
    }

    /**
     * Method attempts to insert currentCard into database. First checks if card is already a favorite,
     * and if so it ignores insertion to prevent duplicates.
     * @return boolean signifies if insert was successful or not
     */
    public boolean insert(){
        List<CardData> cards = repository.getList();
        for(int i = 0; i < cards.size(); i++ ){
            //Checks if card of same name exists in database (may need to use ID other than name)
            if(cards.get(i).getName().equalsIgnoreCase(currentCard.getValue().getName())) return false;
        }
        repository.insert();
        return true;
    }

    public void updateRange(int start, int end){
        repository.updateRange(start,end);
    }

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
