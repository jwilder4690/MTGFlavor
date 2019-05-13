package com.example.mtgflavorsampler;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import androidx.lifecycle.LiveData;

/*
    This class utilizes AsyncTasks<Params, Progress, Result> in order to handle operations that will
    not be instantaneous (Interacting with the web or our database). This prevents our UI from hanging
    or crashing while we fetch items.
 */

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
        Log.i("DEBUG", "I am in repo");
        return allCards;
    }

    public CardData getCurrentCard(){
        //TODO: figure out what to do if null, as AsyncTask may not be complete yet.
        // Need to use temp until new card is fetched
        if(currentCard == null){
            fetchCard();
        }
        return currentCard;
    }

    /*
        TODO: OnCreate, Fetch a card from the web and make it your current card.
        TODO: On request (button) Fetch a new card from the web and replace current card.
        Possibly insert will be used to add current card to database.
     */

    public void fetchCard(){
        // This will fetch a new card from the web and update current card.
        new FetchCardAsyncTask(currentCard).execute(webservice);
    }

    private static class FetchCardAsyncTask extends AsyncTask<Webservice, Void, CardData>{
        //better to make taskCard public or do it this way? Maybe protected? IDK
        private CardData taskCard;

        private  FetchCardAsyncTask(CardData taskCard){
            taskCard = taskCard;
        }

        @Override
        protected CardData doInBackground(Webservice... webs){
            return webs[0].loadCard();
        }

        @Override
        protected void onPostExecute(CardData result){
            taskCard = result;
        }
    }

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
