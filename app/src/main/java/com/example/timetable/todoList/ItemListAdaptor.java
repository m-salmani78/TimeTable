package com.example.timetable.todoList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.datamodel.Item;

import java.util.List;

public class ItemListAdaptor extends RecyclerView.Adapter<ItemListAdaptor.ItemViewHolder> {
    private List<Item> items;
    private Context context;
    private ReminderDatabaseOpenHelper databaseOpenHelper;

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
        holder.subject_txt.setText(item.getSubject());
        holder.title.setText(item.getComment());
        if (item.isDone() == 1){
            holder.rd_btn_done.setChecked(true);
            checkAnimate(holder.rd_btn_done,holder.rd_btn_undone);
        }else if(item.isDone() == 0){
            holder.rd_btn_undone.setChecked(true);
            checkAnimate(holder.rd_btn_undone,holder.rd_btn_done);
        }

        holder.rd_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnimate(holder.rd_btn_done,holder.rd_btn_undone);
                databaseOpenHelper.setIsDone(item.getId(), 1);

            }
        });
        holder.rd_btn_undone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnimate(holder.rd_btn_undone,holder.rd_btn_done);
                databaseOpenHelper.setIsDone(item.getId(), 0);
            }
        });
//        holder.date.setText(item.getWeek().toString());
    }

    private void checkAnimate(View checked,View unChecked){
        checked.animate().scaleY(1.1f).scaleX(1.1f).setDuration(200);
        unChecked.animate().scaleY(0.9f).scaleX(0.9f).setDuration(200);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subject_txt;
        TextView date;
        RadioGroup radioGroup;
        RadioButton rd_btn_done, rd_btn_undone;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
//            date=itemView.findViewById(R.id.item_date);
            subject_txt = itemView.findViewById(R.id.item_cb_header);
            radioGroup = itemView.findViewById(R.id.radio_group);
            rd_btn_done = itemView.findViewById(R.id.radioButton_done);
            rd_btn_undone = itemView.findViewById(R.id.radioButton_undone);
        }
    }
}
