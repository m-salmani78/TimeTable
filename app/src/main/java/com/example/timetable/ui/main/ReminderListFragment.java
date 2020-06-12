package com.example.timetable.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.AddActivity;
import com.example.timetable.MainActivity;
import com.example.timetable.R;
import com.example.timetable.todoList.ReminderDatabaseOpenHelper;
import com.example.timetable.datamodel.Item;
import com.example.timetable.todoList.ItemListAdaptor;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.*;

public class ReminderListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int RQ_CODE_ADD_ACTIVITY = 301;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(scrollY>oldScrollY){
                        ((MainActivity)getContext()).fab.animate().translationY(150f).scaleX(0).scaleY(0).setDuration(200L);
                    }else if(scrollY<oldScrollY){
                        ((MainActivity)getContext()).fab.animate().translationY(0f).scaleX(1).scaleY(1).setDuration(200L);
                    }
                }
            });
        }

        //override back button event
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                listAdaptor.setDeleteMode(false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return root;
    }

    public void addItem(Item item) {
        item.setId((int) databaseOpenHelper.addItem(item));
        items.add(item);
        listAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_CODE_ADD_ACTIVITY && resultCode == RESULT_OK) {
            Item newItem = new Item(-1);
            newItem.setSubject(data.getStringExtra(AddActivity.SUBJECT_TEXT));
            newItem.setTimeBegin(Time.valueOf(data.getStringExtra(AddActivity.START_TIME_TEXT) + ":00"));
            newItem.setDuration(data.getIntExtra(AddActivity.TIME_DURATION, 0));
            newItem.setComment(data.getStringExtra(AddActivity.COMMENT_TEXT));
            newItem.setDone(-1);
            addItem(newItem);
        }
    }
}