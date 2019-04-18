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
public class RetrieveDataTask extends AsyncTask<Activity, Void, String> {

    private Exception exception;
    String data;
    Activity mainAct;
    TextView flavorTextView;
    TextView nameTextView;
    ImageView artView;

    protected void onPreExecute(){
        data = "Loading";
    }

    //Params will be arg here
    protected String doInBackground(Activity... act) {
        mainAct = act[0];
        flavorTextView = mainAct.findViewById(R.id.dataField);
        nameTextView = mainAct.findViewById(R.id.cardName);
        artView = (ImageView)mainAct.findViewById(R.id.artCrop);

        flavorTextView.setText(data);
        try {
            URL url = new URL("https://api.scryfall.com/cards/random?q=ft%3A%20");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                br.close();
                data = stringBuilder.toString();
                formatResult(data);
                return data; //this is Result
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
            return null;
        }
    }

    //Result will be arg here
    protected void onPostExecute(String response){
        if(response == null){
            response = "There was an error";
        }
        Log.i("INFO", response);


    }
    
    /*
        Pushes results back to UI
        Data is the full JSON object from scryfall.com.
        This method parses it into relevant parts.
     */
    void formatResult(String data){
        try {
            JSONObject card = new JSONObject(data);
            JSONObject images = card.getJSONObject("image_uris");
            nameTextView.setText(card.getString("name"));
            flavorTextView.setText(card.getString("flavor_text"));

            try{
                if(images.has("art_crop")) {
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(images.getString("art_crop")).getContent());
                    artView.setImageBitmap(bitmap);
                }
                else{
                   Log.d("art","Couldnt find art_crop");
                }
            }
            catch (Exception i){
                flavorTextView.setText(i.toString());
            }

        }
        catch(JSONException j){
            flavorTextView.setText(j.toString()+"\n\n"+data);
        }


    }
}