package com.majed.sary.ui.adapters

import am.leon.LeonImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.majed.sary.R
import com.majed.sary.data.model.service.Banner
import com.majed.sary.ui.activities.BaseActivity
import com.majed.sary.utils.extentions.showToastAsLong

class MediaPagerAdapter(private val context: BaseActivity<*>) :
    RecyclerView.Adapter<MediaPagerAdapter.ViewHolder>() {

    private var items: MutableList<Banner> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_media, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(items[position]) {
        holder.itemImage.loadImage(image)
//        holder.itemImage.scaleType = ImageView.ScaleType.CENTER_CROP
        holder.itemView.findViewById<View>(R.id.videoImage).visibility = View.GONE
    }

    override fun getItemCount() = items.size

    fun setItems(pages: List<Banner>) {
        items = pages as MutableList<Banner>
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var itemImage: LeonImageView = itemView.findViewById(R.id.image)

        init {
            itemImage.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            context.showToastAsLong(items[bindingAdapterPosition].link)
        }
    }
}