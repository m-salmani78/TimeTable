package com.example.timetable.todoList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.timetable.R;
import com.example.timetable.datamodel.Item;
import com.example.timetable.ui.main.ReminderListFragment;

import java.util.List;

public class ItemListAdaptor extends RecyclerView.Adapter<ItemListAdaptor.ItemViewHolder> {
    private static final String TAG = "ItemListAdaptor";
    private List<Item> items;
    private Context context;
    private ReminderDatabaseOpenHelper databaseOpenHelper;
    private ReminderListFragment fragment;
    private boolean deleteMode = false;
    private int deletionItemNum = 0;

    public boolean isDeleteMode() {
        return deleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        if (deleteMode) {
            YoYo.with(Techniques.ZoomIn).duration(200L).playOn(fragment.deleteBar);
            fragment.deleteBar.setVisibility(View.VISIBLE);
        } else {
            YoYo.with(Techniques.ZoomOutUp).duration(300L).playOn(fragment.deleteBar);
            for (Item item : items) {
                item.setDeleted(false);
            }
            deletionItemNum = 0;
            notifyDataSetChanged();
        }
    }

    public void doDelete() {
        for (Item item : items) {
            if (item.isDeleted())
                databaseOpenHelper.deleteItem(item.getId());
        }
        databaseOpenHelper.getItems(items);
        notifyDataSetChanged();
    }

    public ItemListAdaptor(List<Item> items, Context context
            , ReminderDatabaseOpenHelper databaseOpenHelper, ReminderListFragment fragment) {
        this.items = items;
        this.context = context;
        this.databaseOpenHelper = databaseOpenHelper;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        final Item item = items.get(position);
        boolean flag = item.isDeleted();
        setItemBackGround(holder, flag);

        String subject = position + ". " + item.getSubject();
        holder.subject_txt.setText(subject);
        holder.title.setText(item.getComment());

        if (item.isDone() == Item.STATUS_DONE) {
            holder.rd_btn_done.setChecked(true);
        } else if (item.isDone() == Item.STATUS_UNDONE) {
            holder.rd_btn_undone.setChecked(true);
        } else {
            holder.rd_btn_done.setChecked(false);
            holder.rd_btn_undone.setChecked(false);
        }
        holder.time.setText(item.getTimeBegin().toString());
        String durationStr = item.getDuration() + " min";
        holder.duration.setText(durationStr);

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!deleteMode) setDeleteMode(true);
                if (!item.isDeleted()) {
                    setItemDeletionMode(holder, item, true);
                }
                return true;
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode) {
                    setItemDeletionMode(holder, item, !item.isDeleted());
                } else {
                    //TODO
                }
            }
        });

        holder.rd_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "task " + position + "is checked", Toast.LENGTH_SHORT).show();
                databaseOpenHelper.setIsDone(item.getId(), Item.STATUS_DONE);
                item.setDone(Item.STATUS_DONE);
            }
        });
        holder.rd_btn_undone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "task " + position + "is checked", Toast.LENGTH_SHORT).show();
                databaseOpenHelper.setIsDone(item.getId(), Item.STATUS_UNDONE);
                item.setDone(Item.STATUS_UNDONE);
            }
        });
    }

    public void chooseAll(boolean flag) {
        for (Item item : items) {
            item.setDeleted(flag);
        }
        if(!flag) {
            deletionItemNum = 0;
        }else {
            deletionItemNum=items.size();
        }
        String str = deletionItemNum + "";
        fragment.deletionItemsNum.setText(str);
        notifyDataSetChanged();
    }

    private void setItemBackGround(ItemViewHolder holder, boolean flag) {
        if (flag) {
            holder.view.setVisibility(View.VISIBLE);
            holder.layout.setSelected(true);
        } else {
            holder.view.setVisibility(View.INVISIBLE);
            holder.layout.setSelected(false);
        }
    }

    private void setItemDeletionMode(ItemViewHolder holder, Item item, boolean deletedItem) {
        setItemBackGround(holder, deletedItem);
        item.setDeleted(deletedItem);
        if (deletedItem) {
            deletionItemNum++;
        } else {
            deletionItemNum--;
            if (deletionItemNum == 0) setDeleteMode(false);
        }
        String str = deletionItemNum + "";
        fragment.deletionItemsNum.setText(str);
//        Toast.makeText(context, "deleted items =" + deletionItemNum, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subject_txt;
        TextView time;
        TextView duration;
        RadioGroup radioGroup;
        RadioButton rd_btn_done, rd_btn_undone;
        //        CardView cardView;
        LinearLayout layout;
        View view;
//        CheckBox delete_cb;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            YoYo.with(Techniques.ZoomIn).duration(400L).playOn(itemView);
            title = itemView.findViewById(R.id.item_title);
            time = itemView.findViewById(R.id.time_begin);
            duration = itemView.findViewById(R.id.time_duration);
            subject_txt = itemView.findViewById(R.id.item_cb_header);
            radioGroup = itemView.findViewById(R.id.radio_group);
            rd_btn_done = itemView.findViewById(R.id.radioButton_done);
            rd_btn_undone = itemView.findViewById(R.id.radioButton_undone);
            view = itemView.findViewById(R.id.chosen_item_view);
            layout = itemView.findViewById(R.id.items_layout);
//            delete_cb = itemView.findViewById(R.id.delete_cb);
        }
    }
}
