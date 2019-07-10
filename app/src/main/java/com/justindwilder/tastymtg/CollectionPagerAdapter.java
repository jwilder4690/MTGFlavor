package com.justindwilder.tastymtg;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 *  Pager allows for viewing of our favorites list. A fragment is displayed and pager handles swiping
 *  to get next or previous.
 */
public class CollectionPagerAdapter extends FragmentPagerAdapter {
    //Could extend fragmentStateAdapter if pager adapter has performance issues
    private List<CardData> cards = new ArrayList<>();

    public CollectionPagerAdapter(FragmentManager fm){
        super(fm);
    }

    /**
     * This method is called when pager is moved/swiped to a new position. CardData from that position
     * is grabbed and a fragment is created to display that information.
     * @param position
     * @return Fragment displaying all card information.
     */
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
