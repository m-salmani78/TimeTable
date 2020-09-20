package com.example.timetable.todoList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.OneShotPreDrawListener;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.customs.CustomValueAnim;
import com.example.timetable.customs.view.CustomAudioPlayer;
import com.example.timetable.datamodel.Item;

import java.util.List;

public class ItemListAdaptor extends RecyclerView.Adapter<ItemListAdaptor.ItemViewHolder> {
    private static final String TAG = "ItemListAdaptor";
    private List<Item> items;
    private Context context;
    private ReminderDatabaseOpenHelper databaseOpenHelper;
    private boolean deleteMode = false;
    private int selectedItemsNum = 0;
    private OnDeletingItemsListener onDeletingItemsListener;
    private static final int ANIM_DURATION = 500;

    public int getSelectedItemsNum() {
        return selectedItemsNum;
    }

    public void setSelectedItemsNum(int selectedItemsNum) {
        this.selectedItemsNum = selectedItemsNum;
        onDeletingItemsListener.onItemNumChanged(selectedItemsNum, false);
    }

    public boolean isDeleteMode() {
        return deleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        onDeletingItemsListener.onDeleteModeChanged(deleteMode);
        if (!deleteMode) {
            for (Item item : items) {
                item.setSelected(false);
            }
            selectedItemsNum = 0;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public ItemListAdaptor(List<Item> items, Context context
            , ReminderDatabaseOpenHelper databaseOpenHelper, OnDeletingItemsListener onDeletingItemsListener) {
        this.items = items;
        this.context = context;
        this.databaseOpenHelper = databaseOpenHelper;
        this.onDeletingItemsListener = onDeletingItemsListener;
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
        boolean itemSelectionState = item.isSelected();
        setItemBackGround(holder, itemSelectionState);

        String subject = (position + 1) + ". " + item.getSubject();
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
        String durationStr = item.getDuration() + " min";
        holder.duration.setText(durationStr);

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!deleteMode) setDeleteMode(true);
                if (!item.isSelected()) {
                    setItemDeletionMode(holder, item, true);
                }
                return true;
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode) {
                    setItemDeletionMode(holder, item, !item.isSelected());
                } else {
                    if (item.isLayoutExpanded()) {
                        holder.audioPlayer.pause();
                        CustomValueAnim.verticalScaleAnim(holder.childLayout, holder.originalHeight
                                , holder.collapsedHeight, new AnticipateOvershootInterpolator(), ANIM_DURATION);
//                        holder.audioPlayer.setVisibility(View.GONE);
                    } else {
                        if (!holder.audioPlayer.isAudioSetup()) {
                            //setup audio player
                            holder.audioPlayer.setupAudioPlayer("behnam");
                        }
                        CustomValueAnim.verticalScaleAnim(holder.childLayout, holder.collapsedHeight
                                , holder.originalHeight, new AnticipateOvershootInterpolator(), ANIM_DURATION);
//                        holder.audioPlayer.setVisibility(View.VISIBLE);
                    }
                    item.setLayoutExpended(!item.isLayoutExpanded());
                }
            }
        });

        holder.rd_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setDone(1);
                databaseOpenHelper.setIsDone(item.getId(), 1);
                Toast.makeText(context, "task " + position + "is done", Toast.LENGTH_SHORT).show();
            }
        });
        holder.rd_btn_undone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setDone(0);
                databaseOpenHelper.setIsDone(item.getId(), 0);
                Toast.makeText(context, "task " + position + "isn't done", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setItemBackGround(ItemViewHolder holder, boolean flag) {
        if (flag) {
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.view.setVisibility(View.INVISIBLE);
        }
        holder.parentLayout.setSelected(flag);
    }

    private void setItemDeletionMode(ItemViewHolder holder, Item item, boolean selectedItem) {
        setItemBackGround(holder, selectedItem);
        item.setSelected(selectedItem);
        holder.parentLayout.setSelected(selectedItem);
        if (selectedItem) {
            selectedItemsNum++;
        } else {
            selectedItemsNum--;
            if (selectedItemsNum == 0) setDeleteMode(false);
        }
        onDeletingItemsListener.onItemNumChanged(selectedItemsNum, true);
        Toast.makeText(context, "deleted items =" + selectedItemsNum, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title, subject_txt, time, duration;
        RadioGroup radioGroup;
        RadioButton rd_btn_done, rd_btn_undone;
        LinearLayout parentLayout, childLayout;
        CustomAudioPlayer audioPlayer;
        View view;
        int originalHeight, collapsedHeight;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            YoYo.with(Techniques.ZoomIn).duration(400L).playOn(itemView);
            title = itemView.findViewById(R.id.item_title);
            time = itemView.findViewById(R.id.time_begin);
            duration = itemView.findViewById(R.id.time_duration);
            subject_txt = itemView.findViewById(R.id.item_cb_header);
            radioGroup = itemView.findViewById(R.id.radio_group);
            rd_btn_undone = itemView.findViewById(R.id.radioButton_undone);
            rd_btn_done = itemView.findViewById(R.id.radioButton_done);
            view = itemView.findViewById(R.id.chosen_item_view);
            parentLayout = itemView.findViewById(R.id.main_item_layout);
            childLayout = itemView.findViewById(R.id.sub_item_layout);
            audioPlayer = itemView.findViewById(R.id.audio_player);
            originalHeight = 190;
            collapsedHeight = 80;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.audioPlayer.destroyAudioPlayer();
    }

//    @Override
//    public void onViewAttachedToWindow(@NonNull final ItemViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        holder.audioPlayer.setVisibility(View.VISIBLE);
//
//        OneShotPreDrawListener listener=OneShotPreDrawListener.add(holder.childLayout, new Runnable() {
//            @Override
//            public void run() {
//                holder.originalHeight = holder.childLayout.getHeight();
//            }
//        });
//        listener.onPreDraw();
////        holder.audioPlayer.setVisibility(View.GONE);
//        holder.collapsedHeight = 0;
//
//        Log.i(TAG, "ItemViewHolder: original height= "+holder.originalHeight);
//        Log.i(TAG, "ItemViewHolder: collapsed height= "+holder.collapsedHeight);
//    }

    public interface OnDeletingItemsListener {
        void onDeleteModeChanged(boolean deleteMode);

        void onItemNumChanged(int newItemNum, boolean fromUser);
    }
}
