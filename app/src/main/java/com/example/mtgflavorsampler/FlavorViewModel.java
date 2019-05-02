package com.example.mtgflavorsampler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import androidx.lifecycle.ViewModel;

public class FlavorViewModel extends ViewModel {
    private LiveData<Card> currentCard;
    private CardRepository cardRepo;
    String cardId;



    public void init(int id){
        if(this.currentCard != null){
            return;
        }
        currentCard = new MutableLiveData<Card>();
                = cardRepo.fetchCard();
    }


    public LiveData<Card> getCard(){
        if (currentCard == null){
            currentCard = new MutableLiveData<Card>();
            cardRepo.loadCard();
        }
        return currentCard;
    }


}
