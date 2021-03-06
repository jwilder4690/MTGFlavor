package com.justindwilder.tastymtg;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 *  Fragment for displaying all relevant info and art from a cardData object. Unfortunately fragments
 *  do not easily allow for complex objects as arguments, so the factory method (newInstance) was used
 *  to bundle all of the string information from the cardData as arguments to the fragment.
 *
 *  This fragment was kept generic so that it can be used both on the main screen as an inner fragment
 *  as well as the fragment used by the pager for viewing the favorites list.
 */
public class DisplayFavoriteFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private static final String ARG_ARTIST = "artist";
    private static final String ARG_FLAVOR = "flavor";
    private static final String ARG_AC_PATH = "artCrop";
    private static final String ARG_CA_PATH = "cardArt";
    private static final String ARG_COLOR = "color";

    private String cardName;
    private String artist;
    private String flavorText;
    private String artCropPath;
    private String cardArtPath;
    private int color;


    /**
     * Takes information from CardData and bundles it into arguments for the fragment.
     * @param name
     * @param artist
     * @param flavorText
     * @param artCropPath
     * @param cardArtPath
     * @param color
     * @return Fragment with 6 arguments.
     */
    public static DisplayFavoriteFragment newInstance(String name, String artist, String flavorText, String artCropPath, String cardArtPath, int color){
        DisplayFavoriteFragment fragment = new DisplayFavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_ARTIST, artist);
        args.putString(ARG_FLAVOR, flavorText);
        args.putString(ARG_AC_PATH, artCropPath);
        args.putString(ARG_CA_PATH, cardArtPath);
        args.putInt(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    public DisplayFavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_display_favorite, container, false);

        /*
            Grab reference to the views in this fragment in order to update them.
         */
        TextView nameView = v.findViewById(R.id.text_view_card_name);
        TextView flavorView = v.findViewById(R.id.text_view_flavor);
        TextView artistView = v.findViewById(R.id.text_view_artist);
        ImageView artCropView = v.findViewById(R.id.image_view_art_crop);
        ImageView cardArtView = v.findViewById(R.id.image_view_card_art);
        ScrollView scrollView = v.findViewById(R.id.scrollView);

        /*
            Extracts information from arguments.
         */
        if(getArguments() != null){
            parseArguments();
        }

        /*
            Updates the views with newly parsed information.
         */
        nameView.setText(cardName);
        flavorView.setText(flavorText);
        artistView.setText(artist);
        try{
            File artPath = new File(artCropPath);
            FileInputStream fileInputStream = new FileInputStream(artPath);
            Bitmap art =  BitmapFactory.decodeStream(fileInputStream);
            artCropView.setImageBitmap(art);
            fileInputStream.close();

            artPath = new File(cardArtPath);
            fileInputStream = new FileInputStream(artPath);
            art = BitmapFactory.decodeStream(fileInputStream);
            cardArtView.setImageBitmap(art);
            fileInputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (NullPointerException n){
            //Don't replace image
        } catch (IOException i){

        }
        scrollView.setBackgroundColor(getResources().getColor(color));
        return v;
    }

    private void parseArguments(){
        cardName = getArguments().getString(ARG_NAME);
        artist = getArguments().getString(ARG_ARTIST);
        flavorText = getArguments().getString(ARG_FLAVOR);
        cardArtPath = getArguments().getString(ARG_CA_PATH);
        artCropPath = getArguments().getString(ARG_AC_PATH);
        color = getArguments().getInt(ARG_COLOR);
    }

}
