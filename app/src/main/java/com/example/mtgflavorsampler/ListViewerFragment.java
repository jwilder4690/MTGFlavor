package com.example.mtgflavorsampler;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewerFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;
    private FlavorViewModel flavorViewModel;
    private int position;

    public ListViewerFragment() {
        // Required empty public constructor
    }

    public static ListViewerFragment newInstance(int position) {
        ListViewerFragment fragment = new ListViewerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_viewer, container, false);

        if(getArguments() != null){
            position = getArguments().getInt(ARG_POSITION);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        collectionPagerAdapter = new CollectionPagerAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);
        Log.i("DEBUG", "Position: "+position);

        //View pager initialization is not instantaneous, so settingItem must be done in runnable to wait
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position);
            }
        });

        Log.i("DEBUG", "I am here tho: " +viewPager.getCurrentItem());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flavorViewModel = ViewModelProviders.of(getActivity()).get(FlavorViewModel.class);

        flavorViewModel.getFavoriteCards().observe(getViewLifecycleOwner(), new Observer<List<CardData>>() {
            @Override
            public void onChanged(List<CardData> favoriteCards) {
                collectionPagerAdapter.setCards(favoriteCards);
            }
        });
    }
}

