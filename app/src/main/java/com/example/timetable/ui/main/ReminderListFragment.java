package com.example.timetable.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.timetable.MainActivity;
import com.example.timetable.R;
import com.example.timetable.todoList.ReminderDatabaseOpenHelper;
import com.example.timetable.datamodel.Item;
import com.example.timetable.todoList.ItemListAdaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReminderListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
//    public static final int RQ_CODE_ADD_ACTIVITY = 301;
    private List<Item> items = new ArrayList<>();
    private ItemListAdaptor listAdaptor;
    private ReminderDatabaseOpenHelper databaseOpenHelper;
    private View cardView, btnCancel, btnDelete;
    private CheckBox cbSelectAll;
    private TextView txtItemNum;

    static ReminderListFragment newInstance() {
        ReminderListFragment fragment = new ReminderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, 1);
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

        //deleting action bar
        cardView = root.findViewById(R.id.deleting_actionbar);
        btnCancel = root.findViewById(R.id.imageBtn_cancel);
        btnDelete = root.findViewById(R.id.imageBtn_delete);
        cbSelectAll = root.findViewById(R.id.select_all_cb);
        txtItemNum = root.findViewById(R.id.items_num_txt);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdaptor.setDeleteMode(false);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Item item : items) {
                    if (item.isSelected()) {
                        databaseOpenHelper.deleteItem(item.getId());
                    }
                }
                databaseOpenHelper.getItems(items);
                listAdaptor.notifyDataSetChanged();
                listAdaptor.setDeleteMode(false);
            }
        });
        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = cbSelectAll.isChecked();
                for (Item item : items) {
                    item.setSelected(isChecked);
                }
                listAdaptor.setSelectedItemsNum(isChecked ? items.size() : 0);
                listAdaptor.notifyDataSetChanged();
            }
        });

        //recycler view
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view);
        listAdaptor = new ItemListAdaptor(databaseOpenHelper.getItems(items), getContext(), databaseOpenHelper
                , new ItemListAdaptor.OnDeleteMode() {
            @Override
            public void onDeleteModeChanged(boolean deleteMode) {
                if (deleteMode) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_in_vertically);
                    cardView.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn).duration(250).playOn(cardView);
                    btnCancel.startAnimation(animation);
                    btnDelete.startAnimation(animation);
                } else {
//                    cardView.setVisibility(View.GONE);
                    YoYo.with(Techniques.ZoomOut).duration(400).playOn(cardView);
                }
            }

            @Override
            public void onItemNumChanged(int newItemNum, boolean fromUser) {
                String str = newItemNum + "";
                txtItemNum.setText(str);
                if (fromUser) {
                    cbSelectAll.setChecked(newItemNum == items.size());
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listAdaptor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        ((MainActivity) Objects.requireNonNull(getContext())).fab.setVisibility(View.INVISIBLE);

                    } else if (scrollY < oldScrollY) {
                        ((MainActivity) Objects.requireNonNull(getContext()))
                                .fab.animate().translationY(0f).scaleX(1).scaleY(1).setDuration(150L);
                        ((MainActivity) getContext()).fab.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        //override back button event
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (listAdaptor.isDeleteMode()) {
                    listAdaptor.setDeleteMode(false);
                } else {
                    Toast.makeText(getContext(), "click back again", Toast.LENGTH_SHORT).show();
//                    Objects.requireNonNull(getActivity()).finish();
                }
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RQ_CODE_ADD_ACTIVITY && resultCode == RESULT_OK) {
//            Item newItem = new Item(-1);
//            newItem.setSubject(data.getStringExtra(AddActivity.SUBJECT_TEXT));
//            newItem.setTimeBegin(Time.valueOf(data.getStringExtra(AddActivity.START_TIME_TEXT) + ":00"));
//            newItem.setDuration(data.getIntExtra(AddActivity.TIME_DURATION, 0));
//            newItem.setComment(data.getStringExtra(AddActivity.COMMENT_TEXT));
//            addItem(newItem);
//        }
//    }
}