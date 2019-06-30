package com.example.mtgflavorsampler;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//Could extend fragmentStateAdapter if pager adapter has performance issues
public class CollectionPagerAdapter extends FragmentPagerAdapter {
    private List<CardData> cards = new ArrayList<>();

    public CollectionPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        CardData card = cards.get(position);
        DisplayFavoriteFragment fragment = DisplayFavoriteFragment.newInstance(
                card.getName(),
                card.getArtist(),
                card.getFlavorText(),
                card.getArtCropPath(),
                card.getCardArtPath(),
                card.getColor()
        );
        return fragment;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    public void setCards(List<CardData> cards){
        this.cards = cards;
        notifyDataSetChanged();
    }
}
