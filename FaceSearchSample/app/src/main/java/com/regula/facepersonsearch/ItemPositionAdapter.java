package com.regula.facepersonsearch;

import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemPositionAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {
    private int position = -1;

    public int getPosition() {
        return this.position;
    }

    void setPosition(int position) {
        this.position = position;
    }
}
