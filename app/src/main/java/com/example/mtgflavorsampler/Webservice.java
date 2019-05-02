package com.example.mtgflavorsampler;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Webservice {
    public Card loadCard(){
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

                return new Card(stringBuilder.toString());


            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
        }
        return new Card();
    }
}
