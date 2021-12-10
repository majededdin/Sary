package com.majed.sary.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.majed.sary.data.model.service.ItemBanner
import com.majed.sary.databinding.ItemBannerBinding
import com.majed.sary.ui.activities.BaseActivity
import com.majed.sary.utils.extentions.showToastAsShort

class BannerItemAdapter(private val context: BaseActivity<*>) :
    RecyclerView.Adapter<BannerItemAdapter.ViewHolder>() {

    private var items = ArrayList<ItemBanner>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        with(items[position]) {
            imgSliderImage.loadImage(image)
        }
    }

    fun setItems(list: List<ItemBanner>) {
        this.items = list as ArrayList<ItemBanner>
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.imgSliderImage.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            context.showToastAsShort(items[bindingAdapterPosition].deep_link)
        }
    }
}