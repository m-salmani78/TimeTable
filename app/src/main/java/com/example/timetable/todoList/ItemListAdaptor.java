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

import java.util.List;

public class ItemListAdaptor extends RecyclerView.Adapter<ItemListAdaptor.ItemViewHolder> {
    private List<Item> items;
    private Context context;

    public ItemListAdaptor(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.subject_txt.setText(item.getSubject());
        holder.title.setText(item.getComment());
        holder.rd_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rd_btn_done.animate().scaleY(1.1f).scaleX(1.1f).setDuration(200);
                holder.rd_btn_undone.animate().scaleY(0.9f).scaleX(0.9f).setDuration(200);
            }
        });
        holder.rd_btn_undone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rd_btn_undone.animate().scaleY(1.1f).scaleX(1.1f).setDuration(200);
                holder.rd_btn_done.animate().scaleY(0.9f).scaleX(0.9f).setDuration(200);
            }
        });
//        holder.date.setText(item.getWeek().toString());
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
        RadioButton rd_btn_done,rd_btn_undone;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
//            date=itemView.findViewById(R.id.item_date);
            subject_txt = itemView.findViewById(R.id.item_cb_header);
            radioGroup=itemView.findViewById(R.id.radio_group);
            rd_btn_done=itemView.findViewById(R.id.radioButton_done);
            rd_btn_undone=itemView.findViewById(R.id.radioButton_undone);
        }
    }
}
