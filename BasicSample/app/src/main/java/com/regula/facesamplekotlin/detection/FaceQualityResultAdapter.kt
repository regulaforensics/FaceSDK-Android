package com.regula.facesamplekotlin.detection

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.regula.facesamplekotlin.R


class FaceQualityResultAdapter(private val context: Context, var list: List<IFaceQualityItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_IMAGE = 0
        const val VIEW_HEADER = 1
        const val VIEW_ITEM = 2
    }

    private inner class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.headerTitleTextView)
        fun bind(position: Int) {
            val item = list[position] as GroupQualityResultItem
            title.text = item.getTitle()
        }
    }

    private inner class ImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageViewFace)
        fun bind(position: Int) {
            val item = list[position]
            item.getImage()?.let {
                val decodedString: ByteArray = Base64.decode(it, Base64.NO_WRAP)
                image.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        decodedString,
                        0,
                        decodedString.size
                    )
                )
            }
        }
    }

    private inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.titleTv)
        var description: TextView = itemView.findViewById(R.id.descriptionTv)
        fun bind(position: Int) {
            val item = list[position] as FaceQualityResultItem
            title.text = item.getTitle()
            description.text = item.getDescription()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_IMAGE) {
            return ImageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_header_image, parent, false)
            )
        }
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
        if (list[position].isHeader()) {
            (holder as HeaderViewHolder).bind(position)
        } else if (list[position].getImage() != null) {
            (holder as ImageViewHolder).bind(position)
        } else {
            (holder as ItemViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].isHeader())
            return VIEW_HEADER;
        if (list[position].getImage() != null)
            return VIEW_IMAGE;
        return VIEW_ITEM;
    }
}
