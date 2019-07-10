package com.justindwilder.tastymtg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
    This class represents the data stored inside the database. 
*/

@Entity(tableName = "card_table")
public class CardData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String flavorText;
    private String artist;
    private String artCropPath;
    private String cardArtPath;
    private String artCropUrl;
    private String cardArtUrl;
    private String webUrl;
    private int color; //used for background color
    private int favorite = 0; //use later for ordering your faves

    public CardData(String webCard){
        try{
            JSONObject cardJSON = new JSONObject(webCard);
            //If dual face card, images and flavor text will be held in one of the faces
            if(cardJSON.getString("layout").equals("transform")){
                JSONArray faceArray = cardJSON.getJSONArray("card_faces");
                cardJSON = faceArray.getJSONObject(0);
            }

            name = cardJSON.getString("name");
            flavorText = cardJSON.getString("flavor_text");
            artist = cardJSON.getString("artist");
            webUrl = cardJSON.getString("scryfall_uri");

            JSONObject images = cardJSON.getJSONObject("image_uris");
            artCropUrl = images.getString("art_crop");
            cardArtUrl = images.getString("png");

            /*
                Color is stored in the JSON as an array of colors with 0,1, or more as possibilities. 
                This section assigns the corresponding color from the colors.xml to the object. 
            */
            JSONArray colors = cardJSON.getJSONArray("colors");
            if (colors.length() > 1) color = R.color.Gold;
            else if (colors.length() == 0) color = R.color.Brown;
            else {
                switch (colors.getString(0)) {
                    case "W":
                        color = R.color.W;
                        break;
                    case "U":
                        color = R.color.U;
                        break;
                    case "B":
                        color = R.color.B;
                        break;
                    case "R":
                        color = R.color.R;
                        break;
                    case "G":
                        color = R.color.G;
                        break;
                    default:
                        color = R.color.U;
                }
            }
        }
        catch (JSONException j) {
            //Toast here?
        }
    }


    public CardData(String name, String flavorText, String artist, String artCropUrl, String cardArtUrl, int color, String webUrl) {
        this.name = name;
        this.flavorText = flavorText;
        this.artist = artist;
        this.artCropUrl = artCropUrl;
        this.cardArtUrl = cardArtUrl;
        this.color = color;
        this.webUrl = webUrl;
    }

    /**
     *  Use as default card.
     */
    @Ignore
    public CardData(){
        name = "Omniscience";
        flavorText = "\"The things I once imagined would be my greatest achievements were only the first steps toward a future I can only begin to fathom.\"\n— Jace Beleren";
        artist = "Jason Chan";
        artCropUrl = "https://img.scryfall.com/cards/art_crop/en/m19/65.jpg?1531451394";
        cardArtUrl = "https://img.scryfall.com/cards/png/en/m19/65.png?1531451394";
        webUrl = "https://scryfall.com/card/m13/63/omniscience?utm_source=api";
        color = R.color.U;
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

    public String getFlavorText() { return flavorText; }

    public String getWebUrl(){ return webUrl;}

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

    public String getArtCropPath() {
        return artCropPath;
    }

    public String getCardArtPath() {
        return cardArtPath;
    }

    public void setFavorite(int change){
        favorite = change;
    }

    public void setArtCropPath(String path){
        this.artCropPath = path;
    }

    public void setCardArtPath(String path) {
        this.cardArtPath = path;
    }

    public String getSafeName(){
        String safeName = name;
        safeName = safeName.replaceAll(" ", "_");
        return safeName;
    }

    public String toString(){
        String output = "";
        output += "ID: "+id + "\n";
        output += "Name: "+name + "\n";
        output += "FlavorText: "+flavorText + "\n";
        output += "Artist: "+artist + "\n";
        output += "ArtCropPath: "+artCropPath + "\n";
        output += "CardArtPath: "+cardArtPath + "\n";
        output += "ArtCropURL: "+artCropUrl + "\n";
        output += "CartArtURL: "+cardArtUrl + "\n";
        output += "WebURL: "+webUrl + "\n";
        output += "Color: "+color + "\n";
        output += "Favorite: "+favorite + "\n";
        return output;
    }

}
