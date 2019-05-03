package com.example.mtgflavorsampler;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "card_table")
public class CardData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private JSONObject cardJSON;
    private String name;
    private String flavorText;
    private String artist;
    private String artCropUrl;
    private String cardArtUrl;
    private int favorite; //use later for ordering your faves



    public CardData(String webCard){
        try{
            cardJSON = new JSONObject(webCard);
            name = cardJSON.getString("name");
            flavorText = cardJSON.getString("flavor_text");
            artist = cardJSON.getString("artist");

            JSONObject images = cardJSON.getJSONObject("image_uris");

            artCropUrl = images.getString("art_crop");
            cardArtUrl = images.getString("png");
        }
        catch (JSONException j) {
            Log.e("Exception", j.toString());
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public String getArtist() {
        return artist;
    }

    public String getArtCropUrl() {
        return artCropUrl;
    }

    public String getCardArtUrl() {
        return cardArtUrl;
    }

    public int getFavorite() {
        return favorite;
    }
}