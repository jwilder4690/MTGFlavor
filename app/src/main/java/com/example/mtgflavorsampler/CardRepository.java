package com.example.mtgflavorsampler;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import androidx.lifecycle.LiveData;


public class CardRepository {
    private CardDao cardDao;
    private LiveData<List<CardData>> allCards;
    private CardData currentCard;
    private Webservice webservice;

    public CardRepository(Application application){
        CardDatabase database = CardDatabase.getInstance(application);
        cardDao = database.cardDao();
        allCards = cardDao.getAllCards();
    }

    public void insert(CardData card){
        new InsertCardAsyncTask(cardDao).execute(card);
    }

    public void update(CardData card){
        new UpdateCardAsyncTask(cardDao).execute(card);
    }

    public void delete(CardData card){
        new DeleteCardAsyncTask(cardDao).execute(card);
    }

    public LiveData<List<CardData>> getAllCards(){
        return allCards;
    }

    public CardData getCurrentCard(){
        //TODO: figure out what to do if null. Need to use temp until new card is fetched
        return currentCard;
    }

    /*
        TODO: OnCreate, Fetch a card from the web and make it your current card.
        TODO: On request Fetch a new card from the web and replace current card.
        Possibly insert will be used to add current card to database.
     */

//    public void fetchCard(){
//        currentCard = webservice.loadCard();
//    }

    private static class InsertCardAsyncTask extends AsyncTask<CardData, Void, Void>{
        private CardDao cardDao;

        private InsertCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(CardData ... cards){
            cardDao.insert(cards[0]);
            return null;
        }
    }

    private static class UpdateCardAsyncTask extends AsyncTask<CardData, Void, Void>{
        private CardDao cardDao;

        private UpdateCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(CardData ... cards){
            cardDao.update(cards[0]);
            return null;
        }
    }

    private static class DeleteCardAsyncTask extends AsyncTask<CardData, Void, Void>{
        private CardDao cardDao;

        private DeleteCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(CardData ... cards){
            cardDao.delete(cards[0]);
            return null;
        }
    }
}
