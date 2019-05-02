package com.example.mtgflavorsampler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;

public class DisplayCard extends Fragment {
    private static final String ID_KEY = "cid";
    private FlavorViewModel viewModel;


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        String cardId = getArguments().getString(ID_KEY);
        viewModel = ViewModelProviders.of(this).get(FlavorViewModel.class);
        viewModel.init(cardId);
        viewModel.getCard().observe(this, new Observer<Card>() {
            @Override
            public void onChanged(Card card) {
                //update UI
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_main, container, false);
    }
}
