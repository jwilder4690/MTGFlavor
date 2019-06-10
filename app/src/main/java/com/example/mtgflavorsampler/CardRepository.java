package com.example.mtgflavorsampler;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/*
    This class utilizes AsyncTasks<Params, Progress, Result> in order to handle operations that will
    not be instantaneous (Interacting with the web or our database). This prevents our UI from hanging
    or crashing while we fetch items.
 */

public class CardRepository {
    private CardDao cardDao;
    private LiveData<List<CardData>> allCards;
    private List<CardData> currentList = new ArrayList<>(); //temp
    private MutableLiveData<CardData> currentCard = new MutableLiveData<>();
    public Bitmap currentArtCrop;
    public Bitmap currentCardArt;

    public CardRepository(Application application){
        CardDatabase database = CardDatabase.getInstance(application);
        cardDao = database.cardDao();
        allCards = cardDao.getAllCards();
        currentCard.setValue(new CardData());
        fetchList();
        fetchCard();
    }

    public void insert(){
        CardData newInsert = currentCard.getValue();
        if(currentList.size() > 0) newInsert.setFavorite(currentList.get(currentList.size()-1).getFavorite()+1);
        new InsertCardAsyncTask(cardDao).execute(newInsert);
        fetchList();
    }

    public void updateRange(int first, int last){
        int start;
        int end;
        if(first < last){
            start = first;
            end = last;
        }
        else{
            start = last;
            end = first;
        }
        for(int i = start; i <= end; i++){
            update(allCards.getValue().get(i));
        }
    }

    public void swap(int start, int end){
//        Log.i("DEBUG", "Swapping from " + start +" to "+ end);
//        //TODO: Rework this method to loop through, swapping elements between start and end.
//        // Be sure to check if start > or < end, because the algorithm is different when dragging
//        //up versus down. Find generic method?
//
//        int tempId1;
//        int tempId2;
//
//        //Case dragging from top of list down
//        if(start < end){
//            for(int i = start; i < end; i++){
//                tempId1 = allCards.getValue().get(i).getFavorite();
//                tempId2 = allCards.getValue().get(i+1).getFavorite();
//                allCards.getValue().get(i+1).setFavorite(tempId1);
//                allCards.getValue().get(i).setFavorite(tempId2);
//            }
//            for(int i = start; i < end; i++){
//                update(allCards.getValue().get(i));
//            }
//        }
//
//        //Case dragging from bottom of list up
//        if(start > end){
//            for(int i = end; i > start; i--){
//                tempId1 = allCards.getValue().get(i).getFavorite();
//                tempId2 = allCards.getValue().get(i-1).getFavorite();
//                allCards.getValue().get(i-1).setFavorite(tempId1);
//                allCards.getValue().get(i).setFavorite(tempId2);
//            }
//            for(int i = end; i >= start; i--){
//                update(allCards.getValue().get(i));
//            }
//        }
//
//        fetchList();

        int startId = allCards.getValue().get(start).getFavorite();
        int endId =  allCards.getValue().get(end).getFavorite();
        allCards.getValue().get(end).setFavorite(startId);
        //update(allCards.getValue().get(end));
        allCards.getValue().get(start).setFavorite(endId);
        //update(allCards.getValue().get(start));
        fetchList();
    }

    //Keep, may be used to update priority
    public void update(CardData card){
        new UpdateCardAsyncTask(cardDao).execute(card);
    }

    public void delete(CardData card){
        new DeleteCardAsyncTask(cardDao).execute(card);
        fetchList();
    }

    //Use for Live Data
    public LiveData<List<CardData>> getAllCards(){
        return allCards;
    }
    //Use for immediate Data
    public List<CardData> getList(){
        return currentList;
    }

    public void setList(List<CardData> inList){
        currentList = inList;
    }

    public void fetchList(){
        new GetListAsyncTask(cardDao, this).execute();
    }

    public Bitmap getCurrentArtCrop(){
        return currentArtCrop;
    }

    public void setCurrentArtCrop(Bitmap in){
        currentArtCrop = in;
    }

    public void setCurrentCardArt(Bitmap in){
        currentCardArt = in;
    }

    public Bitmap getCurrentCardArt() {
        return currentCardArt;
    }

    public MutableLiveData<CardData> getCurrentCard(){
        //TODO: figure out what to do if null, as AsyncTask may not be complete yet.
        // Need to use temp until new card is fetched
        if(currentCard == null){
            fetchCard();
        }
        return currentCard;
    }

    public void displayCard(CardData card){
        new FetchCardAsyncTask(this, false).execute(card);
    }

    public void setCurrentCard(CardData card){
        currentCard.setValue(card);
    }

    /*
        TODO: OnCreate, Fetch a card from the web and make it your current card.
        TODO: On request (button) Fetch a new card from the web and replace current card.
        Possibly insert will be used to add current card to database.
     */

    public void fetchCard(){
        // This will fetch a new card from the web and update current card.
        new FetchCardAsyncTask(this, true).execute();
    }

    /*
        This class handles accessing information from the web.

        If the fromWeb arg is true, the task will pull a new card from the web service.
        if the fromWeb arg is false, the task will use provided card.

        The images are pulled from the card information and stored in the repository.
     */
    private static class FetchCardAsyncTask extends AsyncTask<CardData, Void, CardData>{
        private CardRepository repository;
        private Webservice webservice;
        private boolean loadFromWeb;

        private  FetchCardAsyncTask(CardRepository repository, boolean fromWeb){
            webservice = new Webservice();
            this.repository = repository;
            this.loadFromWeb = fromWeb;
        }

        @Override
        protected CardData doInBackground(CardData... cards){
            CardData loadedCard;
            if(loadFromWeb) loadedCard = webservice.loadCard();
            else loadedCard = cards[0];
            try{
                InputStream in = (InputStream) new URL(loadedCard.getArtCropUrl()).getContent();
                repository.setCurrentArtCrop(BitmapFactory.decodeStream(in));
                in = (InputStream) new URL(loadedCard.getCardArtUrl()).getContent();
                repository.setCurrentCardArt(BitmapFactory.decodeStream(in));
            }
            catch (MalformedURLException e){
                Log.e("ERROR", "Bad URL from API.");
            }
            catch (Exception e){

            }
            return loadedCard;
        }

        @Override
        protected void onPostExecute(CardData result){
            this.repository.setCurrentCard(result);
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

    private static class GetListAsyncTask extends AsyncTask<Void, Void, List<CardData>>{
        private CardRepository repository;
        private CardDao cardDao;

        private GetListAsyncTask(CardDao cardDao, CardRepository repository){
            this.cardDao = cardDao;
            this.repository = repository;
        }

        @Override
        protected List<CardData> doInBackground(Void... voids){
            return cardDao.getList();
        }

        @Override
        protected void onPostExecute(List<CardData> result){
            this.repository.setList(result);
            for (CardData card: result) {
                Log.i("DEBUG", "ID: "+card.getId() +" "+ card.getName()+" Fave: "+ card.getFavorite());
            }

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
