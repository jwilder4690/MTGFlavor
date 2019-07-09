package com.justindwilder.tastymtg;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
    This class utilizes AsyncTasks<Params, Progress, Result> in order to handle operations that will
    not be instantaneous (Interacting with the web or our database). This prevents our UI from hanging
    or crashing while we fetch items.
 */

public class CardRepository {
    private CardDao cardDao;
    private LiveData<List<CardData>> allCards;
    private List<CardData> currentList = new ArrayList<>(); //temp, will get populated by getList()
    private MutableLiveData<CardData> currentCard = new MutableLiveData<>();
    public Bitmap currentArtCrop;
    public Bitmap currentCardArt;
    Context context;

    public CardRepository(Application application){
        CardDatabase database = CardDatabase.getInstance(application);
        cardDao = database.cardDao();
        allCards = cardDao.getAllCards();
        currentCard.setValue(new CardData());
        context = application.getApplicationContext();
        currentArtCrop = BitmapFactory.decodeResource(application.getResources(), R.drawable.omniscience);
        currentCardArt = BitmapFactory.decodeResource(application.getResources(), R.drawable.omniscience_card);
        fetchList();
        fetchCard();
    }

    public void insert(){
        CardData newInsert = currentCard.getValue();
        //Starting favorite set to one more than last Card in list. TODO: change to start at top of list. 
        if(currentList.size() > 0) newInsert.setFavorite(currentList.get(0).getFavorite()+1);
        new InsertCardAsyncTask(cardDao).execute(newInsert);
        fetchList();
    }

    /**
        Updates the 
    */
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

    public void setArtCrop(Bitmap image){
        currentArtCrop = image;
    }

    public Bitmap getCurrentCardArt() {
        return currentCardArt;
    }

    public void setCardArt(Bitmap image){
        currentCardArt = image;
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
        setCurrentCard(card);
        try{
            File artPath = new File(card.getArtCropPath());
            Bitmap art =  BitmapFactory.decodeStream(new FileInputStream(artPath));
            setArtCrop(art);
            artPath = new File(card.getCardArtPath());
            art = BitmapFactory.decodeStream(new FileInputStream(artPath));
            setCardArt(art);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
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
        //TODO: remove boolean argument

        new FetchCardAsyncTask(this, context).execute();
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
        private Context context;


        private  FetchCardAsyncTask(CardRepository repository, Context context){
            webservice = new Webservice();
            this.repository = repository;
            this.context = context;
        }

        @Override
        protected CardData doInBackground(CardData... cards){
            CardData loadedCard = webservice.loadCard();
            try{
                InputStream in = (InputStream) new URL(loadedCard.getArtCropUrl()).getContent();
                Bitmap art = BitmapFactory.decodeStream(in);
                loadedCard.setArtCropPath(repository.saveToFile(art, loadedCard.getSafeName()+"_ArtCrop.png", context));
                repository.setArtCrop(art);
                in = (InputStream) new URL(loadedCard.getCardArtUrl()).getContent();
                art = BitmapFactory.decodeStream(in);;
                loadedCard.setCardArtPath(repository.saveToFile(art, loadedCard.getSafeName()+"_CardArt.png", context));
                repository.setCardArt(art);
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
                Log.i("DEBUG", "ID: "+card.getId() +" "+ card.getName()+" Path: "+card.getArtCropPath());
            }

        }
    }

    private static class UpdateCardAsyncTask extends AsyncTask<CardData, Void, CardData>{
        private CardDao cardDao;

        private UpdateCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected CardData doInBackground(CardData ... cards){
            cardDao.update(cards[0]);
            return cards[0];
        }

        @Override
        protected void onPostExecute(CardData result){
            //Log.i("DEBUG", result.toString());
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

    public String saveToFile(Bitmap image, String name, Context context){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("cardImages", Context.MODE_PRIVATE);
        File myPath = new File(directory, name);
        FileOutputStream outputStream;

        try {
            Log.i("DEBUG", "Mypath: "+myPath.getPath());
            outputStream = new FileOutputStream(myPath);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        }catch( FileNotFoundException fe){
            fe.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        return myPath.getPath();
    }
}
