package com.justindwilder.tastymtg;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;


/**
 * Fragment is the UI shell for the pager class. Holds "My Favorites" title with arrows as hint for
 * swipe navigation. This fragment should be created with the newInstance() with the integer argument
 * for the desired starting position of the pager. The default constructor would start from position
 * 0, but should not cause any errors.
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

    /**
     * Factory method for creating an instance of this fragment.
     * @param position Integer that tells the pager which page to start from.
     * @return
     */
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

        //View pager initialization is not instantaneous, so setting Item must be done in runnable to wait
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position);
            }
        });
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

