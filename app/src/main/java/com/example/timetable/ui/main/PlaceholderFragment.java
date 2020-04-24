package com.example.timetable.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.todoList.Item;
import com.example.timetable.todoList.ItemListAdaptor;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<Item> items = new ArrayList<>();
    private ItemListAdaptor listAdaptor;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page1, container, false);

        //recycler view
        for (int i = 0; i < 5; i++) {
            Item item = new Item(false, "mahdi " + i, "szfihlsdsfljbs", new Time(i), Item.Week.FRIDAY);
            items.add(item);
        }
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view);
        listAdaptor = new ItemListAdaptor(items, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listAdaptor);

        return root;
    }

    public void addItem(Item item){
        items.add(item);
        listAdaptor.notifyDataSetChanged();
    }

}