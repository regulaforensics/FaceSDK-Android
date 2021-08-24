package com.regula.facesample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.regula.facesample.R;
import com.regula.facesample.data.CategoryDataProvider;
import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.ICategoryItem;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;

    private final List<ICategoryItem> items;
    private final WeakReference<Context> contextRef;

    public CategoryAdapter(@NonNull Context context, CategoryDataProvider dataProvider) {
        this.items = dataProvider.getCategoryItems();
        this.contextRef = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = contextRef.get();
        if (viewType == SECTION_VIEW) {
            return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_title, parent, false));
        }
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_element, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).isHeader()) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        Context context = contextRef.get();
        if (context == null) {
            return;
        }

        ICategoryItem item = items.get(position);

        if (SECTION_VIEW == getItemViewType(position)) {

            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;
            sectionHeaderViewHolder.headerTitleTextView.setText(item.getTitle());
            return;
        }

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        CategoryItem categoryItem = (CategoryItem) item;
        itemViewHolder.titleTv.setText(categoryItem.getTitle());
        itemViewHolder.descriptionTv.setText(categoryItem.getDescription());
        itemViewHolder.itemView.setOnClickListener(v -> categoryItem.onItemSelected(contextRef.get()));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTv, descriptionTv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
        }
    }

    public static class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitleTextView;

        public SectionHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleTextView = (TextView) itemView.findViewById(R.id.headerTitleTextView);
        }
    }
}
