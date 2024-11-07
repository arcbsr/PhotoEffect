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
import com.crop.phototocartooneffect.fragments.MainFragment;
import com.crop.phototocartooneffect.models.MenuItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ShowAllBottomFragment extends BottomSheetDialogFragment {
    private ItemAdapter.OnItemClickListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    public void setListener(ItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    private ShowAllBottomFragment() {
        // Required empty public constructor
    }

    public static ShowAllBottomFragment newInstance(ItemAdapter.OnItemClickListener listener) {
        ShowAllBottomFragment fragment = new ShowAllBottomFragment();
        fragment.setListener(listener);
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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_show_all);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        ItemAdapter adapter = new ItemAdapter(getContext(), new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                if (listener != null) {
                    dismiss();
                    listener.onItemClick(item);
                }
            }
        }); // Replace YourAdapter with your actual adapter
        adapter.setColumnCount(3);
        adapter.setData();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add any additional view setup here
    }
}