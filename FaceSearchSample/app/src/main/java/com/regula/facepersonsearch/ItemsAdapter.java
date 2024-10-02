package com.regula.facepersonsearch;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemsAdapter extends ItemPositionAdapter<ItemsAdapter.ItemsViewHolder> {
    public static final int MENU_EDIT = 0;
    public static final int MENU_DELETE = 1;
    public static final int MENU_DELETE_FROM_GROUP = 2;
    protected View.OnClickListener onItemClickListener;

    public ItemsAdapter(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.two_line_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.itemView.setLongClickable(true);
    }


    @Override
    public void onViewRecycled(ItemsViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ItemsViewHolder extends
            RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener,
            View.OnCreateContextMenuListener {

        TextView name, id;

        @SuppressLint("ResourceType")
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            TypedValue outValue = new TypedValue();
            itemView.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

            itemView.setBackground(AppCompatResources.getDrawable(itemView.getContext(),
                    outValue.resourceId));
            name = itemView.findViewById(android.R.id.text1);
            id = itemView.findViewById(android.R.id.text2);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            setPosition(getAbsoluteAdapterPosition());
            view.showContextMenu();
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, ItemsAdapter.MENU_EDIT, 0, "Update Group");//groupId, itemId, order, title
            menu.add(0, ItemsAdapter.MENU_DELETE, 0, "Delete Group");
        }

        @Override
        public void onClick(View view) {
            setPosition(getAbsoluteAdapterPosition());

            if(onItemClickListener != null)
                onItemClickListener.onClick(view);
        }
    }
}
