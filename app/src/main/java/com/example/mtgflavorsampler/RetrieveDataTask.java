package com.example.mtgflavorsampler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
    Generic types for AsyncTask are Params, Progress, and Result.
    May be void.
    publishProgress(Progress...) not used here.
*/
public class RetrieveDataTask extends AsyncTask<Activity, Void, JSONObject> {

    private Exception exception;
    String text;
    Activity mainAct;
    TextView flavorTextView;
    TextView nameTextView;
    TextView artistTextView;
    ImageView artView;

    protected void onPreExecute(){
        text = "Loading";
    }

    //Params will be arg here
    protected JSONObject doInBackground(Activity... act) {
        mainAct = act[0];
        flavorTextView = mainAct.findViewById(R.id.flavorText);
        nameTextView = mainAct.findViewById(R.id.cardName);
        artistTextView = mainAct.findViewById(R.id.artist);
        artView = (ImageView)mainAct.findViewById(R.id.artCrop);

        flavorTextView.setText(text);
        try {
            URL url = new URL("https://api.scryfall.com/cards/random?q=ft%3A\"+\"");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                br.close();

                try{
                    JSONObject card = new JSONObject(stringBuilder.toString());
                    JSONObject images = card.getJSONObject("image_uris");

                    formatImage(images.getString("art_crop"));
                    return card; //this is Result
                }
                catch(JSONException j){
                    flavorTextView.setText(j.toString()+"\n\n"+text);
                    return null;
                }



            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
            return null;
        }
    }

    //Result will be arg here
    protected void onPostExecute(JSONObject response){
        String error = "";
        if(response == null){
            error = "There was an error";
        }
        Log.i("INFO", error);


        formatResult(response);
    }
    
    /*
        Pushes results back to UI
        Data is the full JSON object from scryfall.com.
        This method parses it into relevant parts.
     */
    void formatResult(JSONObject card) {
        try {
            nameTextView.setText(card.getString("name"));
            flavorTextView.setText(card.getString("flavor_text"));
            artistTextView.setText(card.getString("artist"));
        } catch (JSONException j) {
            flavorTextView.setText(j.toString());
        }
    }

    void formatImage(String url){
        try{
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            artView.setImageBitmap(bitmap);
        }
            catch (Exception i){
            flavorTextView.setText(i.toString());
        }

    }



}