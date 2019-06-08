package com.example.mtgflavorsampler;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private FlavorViewModel flavorViewModel;
    RecyclerView recyclerView;
    Button backButton;


    private OnListFragmentInteractionListener mListener;

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
        backButton = v.findViewById(R.id.back_arrow);

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
        final CardAdapter adapter = new CardAdapter();
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
        new ItemTouchHelper(new ItemTouchCallback(adapter)).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CardData card) {
                /*
                    Updates current card and changes to DisplayFragment in order to view it.
                 */
                flavorViewModel.setCurrentCard(card);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, DisplayFragment.newInstance("a", "b"))
                        .addToBackStack(card.getName())
                        .commit();
            }
        });
    }

    public class ItemTouchCallback extends ItemTouchHelper.Callback {
        private CardAdapter adapter;

        //Used to track where item was dragged from initially to update full list
        private int initialPosition = 0;
        private int currentPostion = 0;
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
            currentPostion = target.getAdapterPosition();

            adapter.swapItems(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            flavorViewModel.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

//        @Override
//        public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
//            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
//        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                Toast.makeText(getActivity(), "Started Dragging", Toast.LENGTH_SHORT).show();
                initialPosition = viewHolder.getAdapterPosition();
                dragging = true;
            }
            if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
                Toast.makeText(getActivity(), "Now Idle", Toast.LENGTH_SHORT).show();
                if(dragging){
                    flavorViewModel.updateRange(initialPosition, currentPostion);
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


    /*
        All below methods were created automatically with the blank fragment template. Some may be
        useful later, but leaving them alone for now.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDisplayFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
