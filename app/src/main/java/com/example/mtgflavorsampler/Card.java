package com.example.mtgflavorsampler;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Card {
    JSONObject cardJSON;
    String name = "Default";
    String flavorText = "Default";
    String artist = "Default";
    String artCropUrl = "Default";
    String cardArtUrl = "Default";
    String cardID = "Default";                     //use scryfallId, pass to activity as an argument

    Card(){

    }

    Card(String webCard){
        try{
            cardJSON = new JSONObject(webCard);
            name = cardJSON.getString("name");
            flavorText = cardJSON.getString("flavor_text");
            artist = cardJSON.getString("artist");
            cardID = cardJSON.getString("id");

            JSONObject images = cardJSON.getJSONObject("image_uris");

            artCropUrl = images.getString("art_crop");
            cardArtUrl = images.getString("png");
        }
        catch (JSONException j) {
            Log.e("Exception", j.toString());
        }
    }
}
