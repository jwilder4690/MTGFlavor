package com.example.mtgflavorsampler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter <CardAdapter.CardHolder> {
    private List<CardData> cards = new ArrayList<>();

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        CardData currentCard = cards.get(position);
        holder.textViewName.setText(currentCard.getName());
        holder.textViewFlavorText.setText(currentCard.getFlavorText());
        holder.textViewPriority.setText(String.valueOf(currentCard.getFavorite()));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setCards(List<CardData> cards){
        this.cards = cards;
        notifyDataSetChanged();
    }

    public CardData getCardAt(int position){
        return cards.get(position);
    }

    class CardHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textViewFlavorText;
        private TextView textViewPriority;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_cardName);
            textViewFlavorText = itemView.findViewById(R.id.text_view_flavorText);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}
