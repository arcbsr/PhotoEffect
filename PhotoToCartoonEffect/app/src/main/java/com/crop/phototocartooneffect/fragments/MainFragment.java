package com.crop.phototocartooneffect.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.adapters.ItemAdapter;
import com.crop.phototocartooneffect.adapters.ItemAdapterFull;
import com.crop.phototocartooneffect.dialogfragment.MenuFragmentDialog;
import com.crop.phototocartooneffect.dialogfragment.ShowAllBottomFragment;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.utils.AppSettings;

public class MainFragment extends Fragment {
    private ItemAdapter.OnItemClickListener listener;

    private MainFragment() {
        // Required empty public constructor
    }

    public void setListener(ItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public static MainFragment newInstance(ItemAdapter.OnItemClickListener listener) {
        MainFragment fragment = new MainFragment();
        fragment.setListener(listener);
        Bundle args = new Bundle();
        // You can add arguments here if needed
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        // TODO: Initialize your views here
        view.findViewById(R.id.header_tryButton).setOnClickListener(v -> {
            listener.onItemClick(AppSettings.DEFAULT_ITEM);
        });
        view.findViewById(R.id.show_all_text).setOnClickListener(v -> {

            ShowAllBottomFragment.newInstance(listener).show(getActivity().getSupportFragmentManager(), "ShowAllBottomFragment");
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_top);
        //        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // Set up a GridLayoutManager with 2 rows (span count) and horizontal orientation
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        ItemAdapter adapter = new ItemAdapter(getContext(), listener); // Replace YourAdapter with your actual adapter
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerView2 = view.findViewById(R.id.recyclerView_2);
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        recyclerView2.setAdapter(adapter);

        RecyclerView recyclerView3 = view.findViewById(R.id.recyclerView_3);
        recyclerView3.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView3.setAdapter(new ItemAdapterFull(getContext(), listener));
    }
}