package com.justindwilder.tastymtg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class for handling web interactions. This class should never be called from the main UI thread as
 * web operations are not instantaneous and will cause lag in application. Current usage is from within
 * asynchronous tasks inside the repository.
 */
public class Webservice {
    /*
        Url needs to be coded using URL percent encoding for special characters, but may use the fulltext
        search string provided by scryfall.
     */
    private final String SCRYFALL_SEARCH = "https://api.scryfall.com/cards/random?q=ft%3A%2F.%2F";

    public Webservice() {
    }

    /**
     * Method creates a url connection to the scryfall database. The search string is a query for a
     * random card with a flavor text containing the regex for any character. This should cover all
     * MTG cards which have a flavor text.
     * @return CardData created from the JSON information fetched from Scryfall.
     */
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
                urlConnection.disconnect();
                return new CardData(stringBuilder.toString());
            } catch (Exception e){

            }
        }catch (Exception e) {
        }
        return new CardData();
    }
}
