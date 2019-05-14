package com.example.mtgflavorsampler;

import android.graphics.Bitmap;
import android.util.Log;

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
    private int favorite; //use later for ordering your faves
    private Bitmap artCrop;
    private Bitmap cardArt;


    public CardData(String webCard){
        try{
            JSONObject cardJSON = new JSONObject(webCard);
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

    public CardData(String name, String flavorText, String artist, String artCropUrl, String cardArtUrl, int favorite) {
        this.name = name;
        this.flavorText = flavorText;
        this.artist = artist;
        this.artCropUrl = artCropUrl;
        this.cardArtUrl = cardArtUrl;
        this.favorite = favorite;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setArtCrop(Bitmap art){
        artCrop = art;
    }

    public void setCardArt(Bitmap art){
        cardArt = art;
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

    public Bitmap getArtCrop() {
        return artCrop;
    }

    public Bitmap getCardArt() {
        return cardArt;
    }

}
