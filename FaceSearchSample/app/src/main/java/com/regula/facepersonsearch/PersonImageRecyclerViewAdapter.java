package com.regula.facepersonsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.regula.facepersonsearch.databinding.FragmentItemBinding;
import com.regula.facesdk.model.results.person.PersonImage;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;

public class PersonImageRecyclerViewAdapter extends ItemPositionAdapter<PersonImageRecyclerViewAdapter.ViewHolder> {

    private final List<PersonImage> mValues;
    private final boolean enableLongClick;

    Handler handler = new Handler(Looper.getMainLooper());

    public PersonImageRecyclerViewAdapter(List<PersonImage> items) {
        this(items, false);
    }

    public PersonImageRecyclerViewAdapter(List<PersonImage> items, boolean enableLongClick) {
        mValues = items;
        this.enableLongClick = enableLongClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL newurl = new URL(holder.mItem.getUrl());
                Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                handler.post(()->holder.mIdView.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements
            View.OnLongClickListener,
            View.OnCreateContextMenuListener  {
        public final ImageView mIdView;
        public PersonImage mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.image;
            binding.getRoot().setOnLongClickListener(this);
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select The Action");
            menu.add(0, ItemsAdapter.MENU_DELETE, 0, "DELETE");
        }

        @Override
        public boolean onLongClick(View view) {
            if(enableLongClick){
                setPosition(getAbsoluteAdapterPosition());
                view.showContextMenu();
            }
            return true;
        }
    }
}