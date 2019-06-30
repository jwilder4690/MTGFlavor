package com.example.mtgflavorsampler;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


public class ListFragment extends Fragment implements CardAdapter.OnStartDragListener {

    private FlavorViewModel flavorViewModel;
    private ItemTouchHelper itemTouchHelper;
    RecyclerView recyclerView;
    //Button backButton;


    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        //Establish view data members used for this screen
        recyclerView = v.findViewById(R.id.recycler_view);

        return v;
    }

    @Override
    public void onActivityCreated(@NonNull Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        flavorViewModel = ViewModelProviders.of(getActivity()).get(FlavorViewModel.class);

        /*
            Recycler View
            This is the list structure provided by android. This allow us to view our list of favorite
            cards. This structure observes liveData, so any changes to the driving data structure will
            automatically be updated to the screen.
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final CardAdapter adapter = new CardAdapter(this);
        recyclerView.setAdapter(adapter);

        flavorViewModel.getFavoriteCards().observe(getViewLifecycleOwner(), new Observer<List<CardData>>() {
            @Override
            public void onChanged(List<CardData> favoriteCards) {
                adapter.setCards(favoriteCards);
            }
        });

        /*
            Creates an ItemTouchHelper using my custom ItemTouchCallback class and attaches it to the
            recyclerView.
         */
        itemTouchHelper = new ItemTouchHelper(new ItemTouchCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, ListViewerFragment.newInstance(position))
                        .addToBackStack("hi")
                        .commit();
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }


    public class ItemTouchCallback extends ItemTouchHelper.Callback {
        private CardAdapter adapter;

        //Used to track where item was dragged from initially to update full list
        private int initialPosition = 0;
        private int currentPosition = 0;
        private boolean dragging = false;

        public ItemTouchCallback(CardAdapter adapter){
            this.adapter = adapter;
        }
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean isItemViewSwipeEnabled(){
            return true;
        }


        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            //grab before swap for accurate position
            currentPosition = target.getAdapterPosition();

            adapter.swapItems(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            flavorViewModel.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                initialPosition = viewHolder.getAdapterPosition();
                dragging = true;
            }
            if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
                if(dragging){
                    flavorViewModel.updateRange(initialPosition, currentPosition);
                    dragging = false;
                }
            }
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            flavorViewModel.delete(adapter.getCardAt(viewHolder.getAdapterPosition()));
            Toast.makeText(getActivity(), "Card Removed", Toast.LENGTH_SHORT).show();
        }
    }

}
