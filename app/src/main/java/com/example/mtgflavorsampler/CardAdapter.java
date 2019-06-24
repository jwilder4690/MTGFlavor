package com.example.mtgflavorsampler;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter <CardAdapter.CardHolder> {
    private List<CardData> cards = new ArrayList<>();
    private OnItemClickListener listener;

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
        holder.layoutCardItem.setBackgroundColor(holder.resources.getColor(currentCard.getColor()));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void swapItems(int oldPosition, int newPosition){
        CardData temp = cards.get(newPosition);
        cards.set(newPosition, cards.get(oldPosition));
        cards.set(oldPosition, temp);
        notifyItemMoved(oldPosition, newPosition);
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
        private RelativeLayout layoutCardItem;
        private Resources resources;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_cardName);
            textViewFlavorText = itemView.findViewById(R.id.text_view_flavorText);
            layoutCardItem = itemView.findViewById(R.id.card_item);
            resources = itemView.getResources();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    //Need to verify that our listener has been initialized and that the position is
                    //valid. Could be invalid during delete animation or other reasons.
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(cards.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(CardData card);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
