package com.example.timetable.todoList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
        View view= LayoutInflater.from(context).inflate(R.layout.layout_items,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item=items.get(position);
        holder.checkBox.setChecked(item.isDone());
        holder.checkBox.setText(item.getSubject());
        holder.title.setText(item.getComment());
        holder.date.setText(item.getWeek().toString());
    }

    @Override
    public int getItemCount() {
        return items.size() ;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        CheckBox checkBox;
        TextView date;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.item_title);
            date=itemView.findViewById(R.id.item_date);
            checkBox=itemView.findViewById(R.id.item_cb_header);
        }
    }
}
