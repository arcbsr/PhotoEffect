package com.crop.phototocartooneffect.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.adapters.ItemAdapter;
import com.crop.phototocartooneffect.adapters.ItemAdapterFull;
import com.crop.phototocartooneffect.dialogfragment.ShowAllBottomFragment;
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.repositories.AppResources;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

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
        if (AppSettings.IS_TESTING_MODE) {
            view.findViewById(R.id.demo_view).setVisibility(View.VISIBLE);
            view.findViewById(R.id.root_view).setVisibility(View.GONE);

        }

        View item = view.findViewById(R.id.item1);
        View item2 = view.findViewById(R.id.item2);
        View item3 = view.findViewById(R.id.item3);
        // TODO: Initialize your views here
        view.findViewById(R.id.header_tryButton).setOnClickListener(v -> {
            listener.onItemClick(AppSettings.DEFAULT_ITEM);
        });
        item.findViewById(R.id.show_all_text).setOnClickListener(v -> {

            ShowAllBottomFragment.newInstance(listener, EditingCategories.AITypeFirebaseEDB.FEATUREAI).show(getActivity().getSupportFragmentManager(), "ShowAllBottomFragment");
        });
        item2.findViewById(R.id.show_all_text).setOnClickListener(v -> {

            ShowAllBottomFragment.newInstance(listener, EditingCategories.AITypeFirebaseEDB.FEATUREAI2).show(getActivity().getSupportFragmentManager(), "ShowAllBottomFragment");
        });

        RecyclerView recyclerView = item.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        ItemAdapter adapter = new ItemAdapter(getContext(), listener); // Replace YourAdapter with your actual adapter
        adapter.setData(AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.FEATUREAI));
        recyclerView.setAdapter(adapter);


        RecyclerView recyclerView2 = item2.findViewById(R.id.recyclerView);
        ItemAdapter adapter2 = new ItemAdapter(getContext(), listener); // Replace YourAdapter with your actual adapter
        adapter2.setData(AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.FEATUREAI2));
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        recyclerView2.setAdapter(adapter2);

        RecyclerView recyclerView3 = item3.findViewById(R.id.recyclerView);
        recyclerView3.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView3.setAdapter(new ItemAdapterFull(getContext(), listener));
        view.findViewById(R.id.gallery_button).setOnClickListener(v -> {
            listener.onItemClick(AppSettings.DEFAULT_ITEM);
        });
        ((TextView) item.findViewById(R.id.title_text)).setText(EditingCategories.AITypeFirebaseEDB.FEATUREAI.getTitle() +
                " ( " + AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.FEATUREAI).size() + " )");
        ((TextView) item2.findViewById(R.id.title_text)).setText(
                EditingCategories.AITypeFirebaseEDB.FEATUREAI2.getTitle() +
                        " ( " + AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.FEATUREAI2).size() + " )");
        ((TextView) item3.findViewById(R.id.title_text)).setText(
                EditingCategories.AITypeFirebaseEDB.USERCREATIONS.getTitle() +
                        " ( " + AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.USERCREATIONS).size() + " )");
        if (AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.FEATUREAI).size() < 6) {
            item.findViewById(R.id.show_all_text).setVisibility(View.GONE);
//            item.findViewById(R.id.title_text).setVisibility(View.INVISIBLE);
        }
        if (AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.FEATUREAI2).size() < 6) {
            item2.findViewById(R.id.show_all_text).setVisibility(View.GONE);
//            item.findViewById(R.id.title_text).setVisibility(View.INVISIBLE);
        }
        recyclerView3.post(new Runnable() {
            @Override
            public void run() {

                final ExpansionLayoutCollection expansionLayoutCollection = new ExpansionLayoutCollection();
                expansionLayoutCollection.add(item.findViewById(R.id.expansionLayout));
                expansionLayoutCollection.add(item2.findViewById(R.id.expansionLayout));
                expansionLayoutCollection.add(item3.findViewById(R.id.expansionLayout));
                expansionLayoutCollection.openOnlyOne(true);
                ((ExpansionLayout)item3.findViewById(R.id.expansionLayout)).expand(true);
            }
        });

    }
}