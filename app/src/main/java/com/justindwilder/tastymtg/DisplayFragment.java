package com.justindwilder.tastymtg;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 *  Outer display shell. This fragment holds the bottom navigation buttons, the main title, and in the
 *  center it holds a frame for an inner card display (DisplayFavoriteFragment). This allows the
 *  DisplayFavoriteFragment to be more generic and and also be used in our Pager.
 */
public class DisplayFragment extends Fragment {

    private FlavorViewModel flavorViewModel;
    ConstraintLayout mainLayout;

    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_card, container, false);

        //Establish views data members from this screen
        mainLayout = v.findViewById(R.id.card_main_layout);
        ImageView favoriteButton = v.findViewById(R.id.add_to_favorites);
        favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
        ImageView webButton = v.findViewById(R.id.go_to_web);
        webButton.setImageResource(R.drawable.ic_go_to_web);

        return v;
    }

    /**
     * Method observes the currentCard in the viewModel and updates the inner display fragment accordingly.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flavorViewModel = ViewModelProviders.of(getActivity()).get(FlavorViewModel.class);
        //Observes the card in the viewModel, and when it changes creates a new inner fragment to display it
        flavorViewModel.viewCard().observe(getViewLifecycleOwner(), new Observer<CardData>() {
            @Override
            public void onChanged(CardData card) {
                DisplayFavoriteFragment fragment = DisplayFavoriteFragment.newInstance(
                        card.getName(),
                        card.getArtist(),
                        card.getFlavorText(),
                        card.getArtCropPath(),
                        card.getCardArtPath(),
                        card.getColor()
                );
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.display_frame, fragment)
                        .commit();
                mainLayout.setBackgroundColor(getResources().getColor(card.getColor()));
            }
        });

    }
}
