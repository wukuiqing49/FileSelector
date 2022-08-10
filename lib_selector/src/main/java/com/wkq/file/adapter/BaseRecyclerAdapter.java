package com.wkq.file.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> mItems;
    protected Context mContext;

    public BaseRecyclerAdapter(Context context) {
        this.mContext = context;
        mItems = new ArrayList<>();
    }

    public List<T> getList() {
        return mItems;
    }

    public void addItem(T item) {
        addItem(mItems.size(), item);
    }

    public void addItem(int index, T item) {
        if (item == null) return;
        mItems.add(index, item);
        try {
            notifyItemInserted(index);
        } catch (Exception e) {
        }
    }

    public void addItems(List<T> items) {
        if (items == null) return;
        int position = mItems.size() - 1;
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(int indext, List<T> items) {
        if (items == null) return;
        this.mItems.addAll(indext, items);
        notifyItemRangeInserted(indext, items.size());
    }

    public boolean containsAll(List<T> items) {
        return mItems.containsAll(items);
    }

    public void updateItem(T tasks, int position) {
        if (tasks == null) return;
        mItems.set(position, tasks);
        try {
            notifyItemChanged(position);
        } catch (Exception e) {
        }
    }

    public void updateItems(List<T> items) {
        if (items == null) return;
        this.mItems.clear();
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    public int indexOf(T item) {
        if (item == null || mItems == null || mItems.size() <= 0) return -1;
        return mItems.indexOf(item);
    }

    public void removeItem(int index) {
        mItems.remove(index);
        notifyItemRemoved(index);
    }

    public void removeAllItems() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void moveItem(T item, int fromPosition, int toPosition) {
        if (fromPosition > toPosition) {
            Collections.rotate(mItems.subList(toPosition, fromPosition + 1), 1);
        } else {
            Collections.rotate(mItems.subList(fromPosition, toPosition + 1), -1);
        }
        notifyItemMoved(fromPosition, toPosition);
        mItems.set(toPosition, item);
        notifyItemChanged(toPosition);
    }

    public void getView(int position, RecyclerView.ViewHolder viewHolder, int type, T item) {
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }


}
