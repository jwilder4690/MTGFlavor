package com.example.mtgflavorsampler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Webservice {
    /*
        Url needs to be coded using URL percent encoding for special characters, but may use the fulltext
        search string provided by scryfall.
     */
    private final String SCRYFALL_SEARCH = "https://api.scryfall.com/cards/random?q=ft%3A%2F.%2F";

    public Webservice() {
    }

    //Does this need to be the AsyncTask or can I just call it from an AsyncTask?
    public CardData loadCard(){
        try {
            URL url = new URL(SCRYFALL_SEARCH);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                br.close();
                return new CardData(stringBuilder.toString());


            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
        }
        //TODO: need some kind of handling for this situation
        return new CardData("");
    }

//    private void setBitmaps(CardData card){
//        try{
//            Bitmap bitmapArtCrop = BitmapFactory.decodeStream((InputStream) new URL(card.getArtCropUrl()).getContent());
//            Bitmap bitmapCardArt = BitmapFactory.decodeStream((InputStream) new URL(card.getCardArtUrl()).getContent());
//            card.setArtCrop(bitmapArtCrop);
//            card.setCardArt(bitmapCardArt);
//        }
//        catch (MalformedURLException e){
//            Log.e("EXCEPTION", "Invalid url provided by api.");
//        }
//        catch (IOException i){
//            Log.e("EXCEPTION", "IO failed.");
//        }
//    }
}
