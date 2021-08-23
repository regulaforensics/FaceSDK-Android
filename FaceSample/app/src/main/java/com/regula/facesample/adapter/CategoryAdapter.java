package com.regula.facesample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.regula.facesample.R;
import com.regula.facesample.data.CategoryDataProvider;
import com.regula.facesample.items.CategoryItem;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    public CategoryAdapter(@NonNull Context context, CategoryDataProvider dataProvider) {
        super(context, R.layout.listview_item, dataProvider.getCategoryItems());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_item, null);
        }

        CategoryItem item = getItem(position);

        if (item != null) {
            TextView titleTv = v.findViewById(R.id.titleTv);
            TextView descriptionTv = v.findViewById(R.id.descriptionTv);

            titleTv.setText(item.getTitle());
            descriptionTv.setText(item.getDescription());
        }

        return v;
    }
}
