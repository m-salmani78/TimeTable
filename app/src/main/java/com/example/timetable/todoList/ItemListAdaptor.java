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

import com.example.timetable.R;
import com.example.timetable.datamodel.Item;

import java.util.List;

public class ItemListAdaptor extends RecyclerView.Adapter<ItemListAdaptor.ItemViewHolder> {
    private static final String TAG = "ItemListAdaptor";
    private List<Item> items;
    private Context context;
    private ReminderDatabaseOpenHelper databaseOpenHelper;
    private boolean deleteMode = false;
    private int deletionItemNum = 0;

    public boolean isDeleteMode() {
        return deleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        if (!deleteMode) {
            for (Item item : items) {
                item.setDeleted(false);
            }
            deletionItemNum = 0;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public ItemListAdaptor(List<Item> items, Context context, ReminderDatabaseOpenHelper databaseOpenHelper) {
        this.items = items;
        this.context = context;
        this.databaseOpenHelper = databaseOpenHelper;
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

        if (item.isDone() == 1) {
            holder.rd_btn_done.setChecked(true);
        } else if (item.isDone() == 0) {
            holder.rd_btn_undone.setChecked(true);
        } else {
            holder.rd_btn_done.setChecked(false);
            holder.rd_btn_undone.setChecked(false);
        }
        holder.time.setText(item.getTimeBegin().toString());
        String durationStr=item.getDuration() + " min";
        holder.duration.setText(durationStr);

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!deleteMode)setDeleteMode(true);
                if(!item.isDeleted()) {
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

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int state = -1;
                if (checkedId == holder.rd_btn_done.getId()) {
                    Toast.makeText(context, "task " + position + "is checked", Toast.LENGTH_SHORT).show();
                    state = 1;
                } else if (checkedId == holder.rd_btn_undone.getId()) {
                    Toast.makeText(context, "task " + position + "isn't done", Toast.LENGTH_SHORT).show();
                    state = 0;
                }
                databaseOpenHelper.setIsDone(item.getId(), state);
                item.setDone(state);
            }
        });
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
        Toast.makeText(context, "deleted items =" + deletionItemNum, Toast.LENGTH_SHORT).show();
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
