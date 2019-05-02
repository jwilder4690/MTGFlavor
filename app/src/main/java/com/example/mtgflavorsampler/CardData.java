package com.example.mtgflavorsampler;

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
    private int favorite;


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
