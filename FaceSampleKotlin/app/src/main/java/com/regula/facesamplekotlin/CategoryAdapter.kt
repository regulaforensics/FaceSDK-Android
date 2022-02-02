package com.regula.facesamplekotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.category.ICategoryItem
import com.regula.facesamplekotlin.CategoryAdapter.Companion.VIEW_ITEM as VIEW_ITEM1


class CategoryAdapter(private val context: Context, var list: List<ICategoryItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_HEADER = 1
        const val VIEW_ITEM = 2
    }

    private inner class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.headerTitleTextView)
        fun bind(position: Int) {
            val item = list[position]
            title.setText(item.getTitle())
        }
    }

    private inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.titleTv)
        var description: TextView = itemView.findViewById(R.id.descriptionTv)
        fun bind(position: Int) {
            val item = list[position] as CategoryItem
            title.setText(item.getTitle())
            item.description?.let { description.setText(it) }

            itemView.setOnClickListener { v: View? ->
                item.onItemSelected(
                    context
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_HEADER) {
            return HeaderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_header_title, parent, false)
            )
        }
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_element, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list.get(position).isHeader()) {
            (holder as HeaderViewHolder).bind(position)
        } else {
            (holder as ItemViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list.get(position).isHeader())
            return VIEW_HEADER;
        return VIEW_ITEM1;
    }
}
