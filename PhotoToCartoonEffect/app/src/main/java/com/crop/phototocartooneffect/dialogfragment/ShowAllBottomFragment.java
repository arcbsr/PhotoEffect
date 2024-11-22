package com.crop.phototocartooneffect.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.adapters.ItemAdapter;
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.repositories.AppResources;

import java.util.ArrayList;
import java.util.List;

public class ShowAllBottomFragment extends BaseBottomFragment {
    private ItemAdapter.OnItemClickListener listener;

    private List<MenuItem> menuItems = new ArrayList<>();
    EditingCategories.AITypeFirebaseEDB aiTypeFirebaseEDB;

    public void setListener(ItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    private ShowAllBottomFragment() {
        // Required empty public constructor
    }

    public static ShowAllBottomFragment newInstance(ItemAdapter.OnItemClickListener listener, EditingCategories.AITypeFirebaseEDB aiTypeFirebaseEDB) {
        ShowAllBottomFragment fragment = new ShowAllBottomFragment();
        fragment.setListener(listener);
        fragment.aiTypeFirebaseEDB = aiTypeFirebaseEDB;
        Bundle args = new Bundle();
        // You can add arguments here if needed
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        if (AppResources.getInstance().getItems(aiTypeFirebaseEDB).size() == 0) {
            dismiss();
            return;
        }
        view.findViewById(R.id.closeIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_show_all);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        ItemAdapter adapter = new ItemAdapter(getContext(), item -> {
            if (listener != null) {
                dismiss();
                listener.onItemClick(item);
            }
        }); // Replace YourAdapter with your actual adapter
        adapter.setColumnCount(2);
        adapter.setPadding(40);
        adapter.setData(AppResources.getInstance().getItems(aiTypeFirebaseEDB));
        recyclerView.setAdapter(adapter);
    }

}