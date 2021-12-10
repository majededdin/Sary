package com.majed.sary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.majed.sary.data.model.enum.DataType
import com.majed.sary.data.model.enum.UIType
import com.majed.sary.data.model.service.Catalog
import com.majed.sary.databinding.LayoutBannerBinding
import com.majed.sary.databinding.LayoutGroupBinding
import com.majed.sary.databinding.LayoutSmartBinding
import com.majed.sary.ui.activities.BaseActivity
import com.majed.sary.utils.extentions.toGone
import com.majed.sary.utils.extentions.toVisible

class StoreDynamicAdapter(private val context: BaseActivity<*>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<Catalog> = ArrayList()

    override fun getItemViewType(position: Int): Int = items[position].getDataType().key

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DataType.SMART.key -> SmartViewHolder(
                LayoutSmartBinding.inflate(inflater, parent, false)
            )

            DataType.GROUP.key -> GroupViewHolder(
                LayoutGroupBinding.inflate(inflater, parent, false)
            )

            else -> BannerViewHolder(LayoutBannerBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (getItemViewType(position)) {
            DataType.SMART.key -> with(holder as SmartViewHolder) {
                bind(items[position])
            }

            DataType.GROUP.key -> with(holder as GroupViewHolder) {
                bind(items[position])
            }

            else -> with(holder as BannerViewHolder) {
                bind(items[position])
            }
        }

    fun setItems(pages: List<Catalog>) {
        items = pages as ArrayList<Catalog>
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class SmartViewHolder(private val binding: LayoutSmartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val adapter: SmartItemAdapter = SmartItemAdapter(context)

        init {
            binding.recyclerSmart.adapter = adapter
        }

        fun bind(catalog: Catalog) {
            if (catalog.show_title) {
                binding.txtTitle.text = catalog.title
                binding.txtTitle.toVisible()
            } else
                binding.txtTitle.toGone()

            binding.recyclerSmart.layoutManager = when (catalog.ui_type) {
                UIType.GRID.type -> GridLayoutManager(context, catalog.row_count)

                UIType.SLIDER.type -> GridLayoutManager(
                    context, catalog.row_count, RecyclerView.HORIZONTAL, false
                )

                else -> LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            adapter.setItems(catalog.getSmartItemList())
        }
    }

    inner class GroupViewHolder(private val binding: LayoutGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val adapter: GroupItemAdapter = GroupItemAdapter(context)

        init {
            binding.recyclerGroup.adapter = adapter
        }

        fun bind(catalog: Catalog) {
            if (catalog.show_title) {
                binding.txtTitle.text = catalog.title
                binding.txtTitle.toVisible()
            } else
                binding.txtTitle.toGone()

            binding.recyclerGroup.layoutManager = when (catalog.ui_type) {
                UIType.GRID.type ->
                    GridLayoutManager(context, catalog.row_count)

                UIType.SLIDER.type -> GridLayoutManager(
                    context, catalog.row_count, RecyclerView.HORIZONTAL, false
                )

                else -> LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            adapter.setItems(catalog.getGroupItemList())
        }
    }

    inner class BannerViewHolder(private val binding: LayoutBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val adapter: BannerItemAdapter = BannerItemAdapter(context)

        init {
            binding.recyclerBanner.adapter = adapter
        }

        fun bind(catalog: Catalog) {
            binding.recyclerBanner.layoutManager = when (catalog.ui_type) {
                UIType.GRID.type ->
                    GridLayoutManager(context, catalog.row_count)

                UIType.SLIDER.type -> GridLayoutManager(
                    context, catalog.row_count, RecyclerView.HORIZONTAL, false
                )

                else -> LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            adapter.setItems(catalog.getBannerItemList())
        }
    }
}