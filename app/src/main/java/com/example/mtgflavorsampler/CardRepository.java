package com.example.mtgflavorsampler;

import javax.security.auth.callback.Callback;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CardRepository {
    private Webservice webservice;
    private Card loadedCard;

    public Card fetchCard(){
        if(loadedCard != null){
            return loadedCard;
        }
        else{
            loadedCard = loadCard();
            return loadedCard;
        }
    }

    public void loadNewCard(){
        loadedCard = webservice.loadCard();
    }

    private Card loadCard(){
        return webservice.loadCard();
    }

//    public LiveData<Card> getCard(int cardId){
//        final MutableLiveData<Card> data = new MutableLiveData<>();
//        webservice.getCard(cardId).enqueue(new Callback<Card>(){
//            public void onResponse(Call<Card> call, Response<Card> response){
//                data.setValue(response.body());
//            }
//        });
//    }
}
