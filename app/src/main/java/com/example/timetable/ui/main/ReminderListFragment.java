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
import com.example.timetable.todoList.ReminderDatabaseOpenHelper;
import com.example.timetable.datamodel.Item;
import com.example.timetable.todoList.ItemListAdaptor;

import java.util.ArrayList;
import java.util.List;

public class ReminderListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<Item> items = new ArrayList<>();
    private ItemListAdaptor listAdaptor;
    private ReminderDatabaseOpenHelper databaseOpenHelper;

    public static ReminderListFragment newInstance(int index) {
        ReminderListFragment fragment = new ReminderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseOpenHelper = new ReminderDatabaseOpenHelper(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reminder_list, container, false);

        //recycler view
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view);
        listAdaptor = new ItemListAdaptor(databaseOpenHelper.getItems(items), getContext(), databaseOpenHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listAdaptor);

        return root;
    }

    public void addItem(Item item) {
        item.setId((int) databaseOpenHelper.addItem(item));
        items.add(item);
        listAdaptor.notifyDataSetChanged();
    }
}