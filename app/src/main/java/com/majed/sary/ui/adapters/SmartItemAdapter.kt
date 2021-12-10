package com.majed.sary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.majed.sary.data.model.service.ItemSmart
import com.majed.sary.databinding.ItemSmartBinding
import com.majed.sary.ui.activities.BaseActivity

class SmartItemAdapter(private val context: BaseActivity<*>) :
    RecyclerView.Adapter<SmartItemAdapter.ViewHolder>() {

    private var items = ArrayList<ItemSmart>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemSmartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        with(items[position]) {
            txtAction.text = name
            imgAction.loadImage(image)
        }
    }

    fun setItems(list: List<ItemSmart>) {
        this.items = list as ArrayList<ItemSmart>
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ItemSmartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imgAction.setOnClickListener(null)
        }
    }
}