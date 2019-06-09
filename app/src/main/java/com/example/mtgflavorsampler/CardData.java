package com.example.mtgflavorsampler;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "card_table")
public class CardData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String flavorText;
    private String artist;
    private String artCropUrl;
    private String cardArtUrl;
    private int color;
    private int favorite = 0; //use later for ordering your faves

    public CardData(String webCard){
        try{
            JSONObject cardJSON = new JSONObject(webCard);
            name = cardJSON.getString("name");
            flavorText = cardJSON.getString("flavor_text");
            artist = cardJSON.getString("artist");

            JSONObject images = cardJSON.getJSONObject("image_uris");
            artCropUrl = images.getString("art_crop");
            //cardArtUrl = images.getString("border_crop");
            cardArtUrl = images.getString("png");

            JSONArray colors = cardJSON.getJSONArray("colors");
            if(colors.length() > 1) color = R.color.Gold;
            else if(colors.length() == 0) color = R.color.Brown;
            else{
                Log.i("DEBUG", "My color is: "+ colors.getString(0));
                switch (colors.getString(0)){
                    case "W": color = R.color.W; break;
                    case "U": color = R.color.U; break;
                    case "B": color = R.color.B; break;
                    case "R": color = R.color.R; break;
                    case "G": color = R.color.G; break;
                    default: color = R.color.red;
                }
            }
        }
        catch (JSONException j) {
            Log.e("Exception", j.toString());
        }
    }

    public CardData(String name, String flavorText, String artist, String artCropUrl, String cardArtUrl, int color) {
        this.name = name;
        this.flavorText = flavorText;
        this.artist = artist;
        this.artCropUrl = artCropUrl;
        this.cardArtUrl = cardArtUrl;
        this.color = color;
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

    public int getColor(){ return color;}

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int change){
        favorite = change;
    }
}
